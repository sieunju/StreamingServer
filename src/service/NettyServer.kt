package service

import model.BasePacket
import model.VideoPacket

interface NettyServer {
    fun start(port : Int)
    fun testVideo(packet : VideoPacket)
    fun testSend(packet : BasePacket)
}