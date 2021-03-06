package impl

import io.netty.channel.socket.nio.NioDatagramChannel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.flowables.ConnectableFlowable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import model.MessagePacket
import model.ReliablePacket
import model.VideoPacket
import model.ViewerInfo
import service.ViewerSendManager
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.*
import kotlin.random.Random

/**
 * 방송 송출자한테 받은 비트맵 데이터들을
 * 뷰어들한테 데이터를 보내기 위한 메니저 클래스
 *
 */
class ViewerSendManagerImpl(
    private val serverChannel: NioDatagramChannel
) : ViewerSendManager{

    private var isRun = false
    private val SAVE_MAX_STREAM = 20
    private val WRITE_DELAY = 10L

    // 비디오 스트림 리스트 한 SAVE_MAX_STREAM 개 정도만 가진다.
    private val videoStreamMap: ConcurrentHashMap<Long, Array<String>> by lazy { ConcurrentHashMap<Long, Array<String>>() }

    // 뷰어들의 맵
    private val viewerClientMap: ConcurrentHashMap<InetSocketAddress, ViewerInfo> by lazy { ConcurrentHashMap<InetSocketAddress, ViewerInfo>() }
    private var currentMiddleTime: Long = -1
    private val viewerSendFlowable: ConnectableFlowable<Long> by lazy {
        Flowable.interval(
            WRITE_DELAY,
            TimeUnit.MILLISECONDS
        )
            .observeOn(Schedulers.single()).publish()
    }
    private var disposable: Disposable? = null

    /**
     * 뷰어한테 비디오 프레임 보내는 처리.
     */
    private fun sendRxPacket(viewer: InetSocketAddress, info: ViewerInfo) {
        Single.create<Boolean> { emitter ->
            val videoStreamList = videoStreamMap[info.getVideoTime()]
            println("Viewer Time ${info.getVideoTime()} VideoStream ${videoStreamList?.size} 뷰어에게 비디오 스트림을 다 보냈나요? ${info.isVideoStreamDone()}")
            if (videoStreamList != null) {
                // 뷰어한테 스트림을 다 보냈다면 다시 보내야할 비디오 스트림 인덱스를 셋팅
                if (info.isVideoStreamDone()) {
                    info.setVideoUidList(videoStreamList)
                }
                val maxSize = videoStreamList.size
                for (idx in videoStreamList.indices) {

                    if (info.isWriteIndex(idx)) {
                        val packet = VideoPacket(
                            time = info.getVideoTime(),
                            currPos = idx,
                            maxSize = maxSize,
                            source = videoStreamList[idx]
                        )
                        packet.recipient = viewer
                        serverChannel.writeAndFlush(packet)
                    }
                }
            } else {
                // 해당 비디오 스트림이 널인경우 다음 프레임 값으로 이동
                info.nextVideoTime()
            }

            emitter.onSuccess(true)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun start() {
        if (!isRun) {
            isRun = true
            if (disposable != null) {
                disposable?.dispose()
                disposable = null
            }
            disposable = viewerSendFlowable
                .subscribe({
                    viewerClientMap.forEach { (viewer: InetSocketAddress, info: ViewerInfo) ->
                        sendRxPacket(viewer, info)
                    }
                }, {

                })
            viewerSendFlowable.connect()
        }
    }

    override fun stop() {
        isRun = false
        disposable?.dispose()
    }

    private fun addViewerCurrTime(time: Long) {
        viewerClientMap.forEach { (_: InetSocketAddress, viewer: ViewerInfo) ->
            viewer.addCurrTime(time)
        }
    }

    /**
     * 비디오 스트림을 추가하는 함수
     * 방송 송출자가 프레임 하나씩 순차적으로 패킷을 보낸다.
     * @param packet 비디오 패킷
     */
    override fun addVideo(packet: VideoPacket) {
        var streamArr = videoStreamMap[packet.time]
        if (streamArr == null) {
            streamArr = Array(packet.maxSize) {
                if (it == packet.currPos) {
                    packet.source
                } else {
                    ""
                }
            }
            videoStreamMap[packet.time] = streamArr
        } else {
            streamArr[packet.currPos] = packet.source
        }

        // 비디오 스트림이 다 채워진 경우 정렬 처리 한다.
        var isFull = true
        for (idx in streamArr.indices) {
            // 하나라도 빈공간이 있는 경우
            if (streamArr[idx].isEmpty()) {
                isFull = false
                break
            }
        }

        if (isFull) {

            if (videoStreamMap.size > SAVE_MAX_STREAM) {
                var minValue = Long.MAX_VALUE
                val iterator = videoStreamMap.keys().iterator()
                while (iterator.hasNext()) {
                    minValue = Math.min(iterator.next(), minValue)
                }
                // 가장 낮은 타임값 삭제
                videoStreamMap.remove(minValue)
            }

            val keyArr = videoStreamMap.keys().toList().toTypedArray()
            Arrays.sort(keyArr)
            currentMiddleTime = keyArr[videoStreamMap.size / 2]

            // Viewer 프레임 추가
            addViewerCurrTime(packet.time)
            // Thread 시작.
            start()
        }
    }

    /**
     * 뷰어들 추가 처리 함수
     * @param viewer 뷰어
     */
    override fun addViewer(viewer: InetSocketAddress) {
        val viewerInfo = ViewerInfo(viewer)
        videoStreamMap[currentMiddleTime]?.let {
            viewerInfo.setVideoUidList(currentMiddleTime, it)
        }
        println("AddViewer ${viewer}")
        viewerClientMap.put(viewer, viewerInfo)
    }

    /**
     * 뷰어로 부터 비디오 스트림을 전달 받은경우.
     * VideoPacket ReliablePacket 키값 규칙
     * @see VideoPacket v-{CaptureTime}-{idx}-{maxSize}
     */
    override fun onReliablePacket(viewer: InetSocketAddress, packet: ReliablePacket) {
        viewerClientMap.get(viewer)?.removeVideoUid(packet.uid)
    }
}