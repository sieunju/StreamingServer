package constants

enum class Header(val type : Char) {
    RELIABLE('a'),
    AUTH('b'),
    VIDEO('c'),
    MSG('d')
}

object Constants {
    const val PORT = 60000
    const val VIDEO = "v"
    const val AUDIO = "a"
    const val MAX_BUF = 65_535
    const val SEP = "|" // 구분자
    const val RELIABLE_UID = "-"
}

object ClientType {
    const val CASTER = 10 // 방송하는 사람
    const val VIEWER = 20 // 방송을 보는 사람
}