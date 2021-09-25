package model

import constants.Constants
import constants.Header
import extensions.simpleStrBuilder

data class ReliablePacket(val uid: String) : BasePacket(Header.RELIABLE) {

    override fun encode() = simpleStrBuilder(Header.RELIABLE.type,uid)

    companion object {
        @JvmStatic
        fun decode(msg: String): ReliablePacket {
            val split = msg.split(Constants.SEP)
            return ReliablePacket(
                split.splitIdxString(1)
            )
        }
    }
}