package service

import model.BasePacket
import model.ReliablePacket
import java.net.InetSocketAddress

interface SendManager {
    fun addPacket(packet : BasePacket)
    fun addUser(userInfo : InetSocketAddress)
    fun onReliablePacket(packet : ReliablePacket)
}