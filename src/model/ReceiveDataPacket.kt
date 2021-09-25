package model

import java.net.InetSocketAddress

data class ReceiveDataPacket(
    val sender: InetSocketAddress? = null,
    val data: String = ""
)