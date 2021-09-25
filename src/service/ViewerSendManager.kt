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
    fun addViewer(viewer : InetSocketAddress)
    fun onReliablePacket(packet : ReliablePacket)
}