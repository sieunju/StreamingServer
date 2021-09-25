package service

import io.netty.buffer.ByteBuf
import model.MessagePacket
import model.VideoPacket

interface OnReceiveMessage {
    //    fun onAuth(packet: String)
    fun onVideo(packet : VideoPacket)
    fun onMessage(packet : MessagePacket)
}