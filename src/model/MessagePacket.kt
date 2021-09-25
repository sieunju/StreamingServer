package model

import constants.Constants
import constants.Header
import extensions.initUid
import extensions.simpleStrBuilder

data class MessagePacket(
    val time: Long,
    val msg: String
) : BasePacket(Header.MSG) {

    init {
        initUid()
    }

    override fun encode() = simpleStrBuilder(super.encode(), time, msg)

    companion object {
        @JvmStatic
        fun decode(msg: String): MessagePacket {
            val split = msg.split(Constants.SEP)
            return MessagePacket(
                split.splitIdxLong(2),
                split.splitIdxString(3)
            )
        }
    }
}