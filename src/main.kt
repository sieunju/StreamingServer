import constants.Constants
import impl.NettyServerImpl
import model.VideoPacket
import java.net.InetSocketAddress
import java.text.NumberFormat
import java.util.*
import kotlin.random.Random

fun main(){
    println("============= Hello JServer =============")
    val server = NettyServerImpl()
    while (true) {
        val sc = Scanner(System.`in`)
        when(sc.nextLine()){
            "s" -> {
                Thread {
                    server.start(Constants.PORT)
                }.start()
            }
            "w" -> {
                val time = System.currentTimeMillis()
                val recipient = InetSocketAddress("172.30.3.0",Constants.PORT)
                val cnt = Random.nextInt(10)
                for(idx in 0 until cnt) {

                    val packet = VideoPacket(
                        time,
                        idx,
                        cnt,
                        if(Random.nextBoolean()) Constants.LONG_TXT else "Test...."
                    )

                    packet.recipient = recipient
                    server.testSend(packet)
                }
            }
            "r" -> {
                Runtime.getRuntime().run {
                    val usedMemKB = (totalMemory() - freeMemory()) / 1024
                    val maxMemKB = maxMemory() / 1024
                    val usedMemStr = NumberFormat.getInstance().format(usedMemKB)
                    val maxMemStr = NumberFormat.getInstance().format(maxMemKB)
                    println("Used ${usedMemStr}KB MAX ${maxMemStr}KB")
                }
            }
            else -> {
                println("잘못된 키를 입력했습니다.")
            }
        }
    }
}