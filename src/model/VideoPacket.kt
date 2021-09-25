package model

import constants.Constants
import constants.Header
import extensions.initUid
import extensions.simpleStrBuilder

data class VideoPacket(
    val time: Long, // 프레임 시간
    val currPos: Int, // Source 나눴을때 위치값
    val maxSize: Int, // Source 최대값
    val source: String // Video Source
) : BasePacket(Header.VIDEO) {

    init {
        initUid()
    }

    // c|v-1632566803229-1-3|1632566803229|1|3|qwerqwerqwer
    // {Header}|{Uid}|{time}|{currPos}|{maxSize}|{source}
    override fun encode() = simpleStrBuilder(super.encode(), time, currPos, maxSize, source)

    companion object {
        @JvmStatic
        fun decode(msg: String): VideoPacket {
            val split = msg.split(Constants.SEP)
            return VideoPacket(
                split.splitIdxLong(2),
                split.splitIdxInt(3),
                split.splitIdxInt(4),
                split.splitIdxString(5)
            )
        }
    }
}