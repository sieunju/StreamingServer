package model

import constants.Constants
import constants.Header
import extensions.simpleStrBuilder
import io.netty.buffer.ByteBuf
import java.net.InetSocketAddress


/**
 * Description : 원래는 [0][1] -> HeaderType (2byte)
 * ... -> 나머지 데이터 (Size(2byte) Data(...))
 * 하려고 했으나 비디오 출력을 사용할때 String 으로 해야 하기때문에 그냥 구분자 | 로 처리..
 * 좀더 나은 방안이 있으면 수정할껴
 *
 * Created by juhongmin on 9/17/21
 */
open class BasePacket(private val header: Header) {
    // reliableUid 규칙 {Type (Video, Audio)}|{CaptureTime}|{Packet Idx}|{Packet Size}
    // Type 은 나중에 Caster 유니크한 아이디 값으로 바꿔서 처리
    var reliableUid: String = "" // Uid
    var recipient: InetSocketAddress? = null // 받는 사람

    // "${header.type}${Constants.SEP}${reliableCnt}${Constants.SEP}"
    open fun encode(): String = if (reliableUid.isEmpty()) {
        simpleStrBuilder(header.type)
    } else {
        simpleStrBuilder(header.type, reliableUid)
    }
}

/**
 * |Size (Int) | Data
 */
fun String.strToPacket(): String {
    return "${this.length}${this}"
}

/**
 * |Size (Int) | Data
 * to
 * Data
 */
fun ByteBuf.packetToStr(): String {
    val size = readInt()
    return readBytes(size).toString()
}

fun List<String>.splitIdxString(idx: Int, default: String = ""): String {
    return try {
        get(idx)
    } catch (ex: Exception) {
        default
    }
}

fun List<String>.splitIdxInt(idx: Int, default: Int = 0): Int {
    return try {
        get(idx).toInt()
    } catch (ex: Exception) {
        default
    }
}

fun List<String>.splitIdxLong(idx: Int, default: Long = 0L): Long {
    return try {
        get(idx).toLong()
    } catch (ex: Exception) {
        default
    }
}