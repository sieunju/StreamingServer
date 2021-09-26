package model

import constants.Constants
import constants.Header
import extensions.simpleStrBuilder

/**
 * 패킷을 받으면 보낸쪽으로 받았다는 신호를 보내기 위한 패킷.
 * @param uid 각 데이터 모델마다 키가 다르다.
 * @see AuthPacket {ClientType}-{SysTime}
 * @see VideoPacket v-{CaptureTime}-{idx}-{maxSize}
 * @see MessagePacket {SysTime}
 */
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