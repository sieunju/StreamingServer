package impl

import io.netty.channel.socket.nio.NioDatagramChannel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import model.ReliablePacket
import model.VideoPacket
import service.ViewerSendManager
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * 방송 송출자한테 받은 비트맵 데이터들을
 * 뷰어들한테 데이터를 보내기 위한 메니저 클래스
 *
 */
class ViewerSendManagerImpl(
    private val serverChannel: NioDatagramChannel
) : ViewerSendManager , Runnable {

    private var isRun = false
    // 비디오 스트림 리스트 한 10개 정도 가지고
    private val videoStreamMap : ConcurrentHashMap<Long,Array<String>> by lazy { ConcurrentHashMap() }
    private val executors: ExecutorService by lazy { Executors.newSingleThreadExecutor() }

    override fun run() {
        while(isRun) {
            runCatching {

            }
        }
    }

    override fun addVideo(packet: VideoPacket) {
        var streamArr = videoStreamMap[packet.time]
        if(streamArr == null) {
            streamArr = Array(packet.maxSize) {
                if(it == packet.currPos) {
                    packet.source
                } else {
                    ""
                }
            }
            videoStreamMap[packet.time] = streamArr
        } else {
            streamArr[packet.currPos] = packet.source
        }
    }

    override fun addViewer(viewer: InetSocketAddress) {

    }

    override fun onReliablePacket(packet: ReliablePacket) {

    }
}