package model

import constants.Constants
import constants.Header
import extensions.initUid
import extensions.simpleStrBuilder
import java.net.InetSocketAddress

/**
 * 인증 키
 * 접속 타입
 */
data class AuthPacket(
    val authKey: String,
    val type: Int
) : BasePacket(Header.AUTH) {

    var sender: InetSocketAddress? = null

    init {
        initUid()
    }

    override fun encode() = simpleStrBuilder(super.encode(), authKey, type)

    companion object {
        @JvmStatic
        fun decode(msg: String): AuthPacket {
            val split = msg.split(Constants.SEP)
            return AuthPacket(
                split.splitIdxString(2),
                split.splitIdxInt(3)
            )
        }
    }
}