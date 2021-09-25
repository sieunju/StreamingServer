import constants.Constants
import impl.NettyServerImpl
import model.VideoPacket
import java.util.*

fun main(){
    println("============= Hello JServer =============")
    val server = NettyServerImpl()
    val packet = VideoPacket(
        time = System.currentTimeMillis(),
        currPos = 1,
        maxSize = 3,
        source = "qwerqwerqwer"
    )
    println(packet.encode())
    while (true) {
        val sc = Scanner(System.`in`)
        when(sc.nextLine()){
            "s" -> {
                Thread {
                    server.start(Constants.PORT)
                }.start()
            }
            else -> {
                println("잘못된 키를 입력했습니다.")
            }
        }
    }
}