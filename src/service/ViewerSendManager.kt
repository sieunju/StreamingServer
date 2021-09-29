package service

import model.ReliablePacket
import model.VideoPacket
import java.net.InetSocketAddress

/**
 * Caster 에서 받은 데이터들을
 * 뷰어들에게 전달할수 있또록 처리 하는 함수
 */
interface ViewerSendManager {
    /**
     * 방송 송출자로 부터 비디오 패킷을 받아 오는 함수
     * @param packet Video Packet
     */
    fun addVideo(packet : VideoPacket)

    /**
     * 뷰어들 추가 처리함수
     * @param viewer 뷰어 소켓 주소
     */
    fun addViewer(viewer : InetSocketAddress)

    /**
     * 뷰어들한테 패킷을 보내고 받았을경우 리턴 받는 ReliablePacket
     * @param viewer 뷰어 소켓 주소
     * @param packet ReliablePacket
     *
     */
    fun onReliablePacket(viewer : InetSocketAddress,packet : ReliablePacket)

    /**
     * 쓰레드 시작
     */
    fun start()

    /**
     * 쓰레드 스탑
     */
    fun stop()
}