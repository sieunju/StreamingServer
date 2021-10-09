package model

import constants.Constants
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * 각 뷰어들마다 적절한 스트리밍을 보내기 위한 데이터 모델 클래스
 */
class ViewerInfo(private val viewerAddress: InetSocketAddress) {

    // 뷰어가 보고 있는 시간
    private var currTime: Long = -1

    // Reliable Packet 으로 받은 맵들
    private val videoIdxSet: ConcurrentHashMap<Int, Boolean> by lazy { ConcurrentHashMap<Int, Boolean>() }
    private val currTimeQue: ConcurrentLinkedQueue<Long> by lazy { ConcurrentLinkedQueue<Long>() }

    fun getVideoTime(): Long {
        if (videoIdxSet.size > 0) {
            return currTime
        } else {
            if (currTimeQue.size > 0) {
                currTime = currTimeQue.poll()
            }
        }
        return currTime
    }

    /**
     * 다음 프레임으로 이동 처리 함수
     */
    fun nextVideoTime(){
        currTime = currTimeQue.poll()
    }

    /**
     * 해당 비디오 스트림을 스킵해도 되는지 유무 판단 처리 함수.
     * @param idx Stream Index
     * @return true 뷰어에 전송해야 함, false 뷰어에 전송 안해도 됨.
     */
    fun isWriteIndex(idx: Int): Boolean {
        // 해당 값이 없는경우 스킵
        return videoIdxSet.containsKey(idx)
    }

    /**
     * 현재 비디오 스트림을 다 받았는지 유무 판단 처리 함수
     * @return true 비디오 스트림 다 받음 false 아직 다 못받음
     */
    fun isVideoStreamDone(): Boolean {
        return videoIdxSet.size == 0
    }

    fun addCurrTime(time: Long) {
        if (currTime < time) {
            currTimeQue.offer(time)
        }
    }

    fun setVideoUidList(streamList: Array<String>) {
        for (idx in streamList.indices) {
            // VideoPacket Index 값만 저장.
            videoIdxSet.put(idx, true)
        }
    }

    fun setVideoUidList(captureTime: Long, streamList: Array<String>) {
        currTime = captureTime
        for (idx in streamList.indices) {
            // VideoPacket Index 값만 저장.
            videoIdxSet.put(idx, true)
        }
    }

    fun removeVideoUid(uid: String) {
        try {
            videoIdxSet.remove(uid.split(Constants.RELIABLE_UID)[2].toInt())
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("removeVideoUid Error $ex")
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is ViewerInfo) {
            this.viewerAddress == other.viewerAddress
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = viewerAddress.hashCode()
        result = 31 * result + currTime.hashCode()
        return result
    }
}