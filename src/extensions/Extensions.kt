package extensions

import constants.Constants
import model.AuthPacket
import model.BasePacket
import model.MessagePacket
import model.VideoPacket


/**
 * 구분자를 추가하여 간단하게 문자열 리턴처리하는 함수
 * @param values Array<Any>
 * @param sep 구분자
 *
 */
fun simpleStrBuilder(vararg values : Any, sep : String = Constants.SEP) : String {
    val str = StringBuilder()
    for(idx in values.indices) {
        str.append(values[idx])
        if(idx < values.lastIndex) {
            str.append(sep)
        }
    }
    return str.toString()
}

/**
 * Uid 설정 처리 하는 함수
 * 각 패킷 구조체 성격에 맞게 설정한다.
 * @see AuthPacket {ClientType}-{SysTime}
 * @see VideoPacket v-{CaptureTime}-{idx}-{maxSize}
 * @see MessagePacket {SysTime}
 */
fun BasePacket.initUid(){
    when(this) {
        is AuthPacket -> {
            reliableUid = simpleStrBuilder(type,authKey,System.currentTimeMillis(),sep = Constants.RELIABLE_UID)
        }
        is VideoPacket -> {
            reliableUid = simpleStrBuilder(Constants.VIDEO, time, currPos, maxSize, sep = Constants.RELIABLE_UID)
        }
        is MessagePacket -> {
            reliableUid = simpleStrBuilder(time)
        }
    }
}