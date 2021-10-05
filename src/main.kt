import constants.Constants
import impl.NettyServerImpl
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import model.VideoPacket
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketException
import java.text.NumberFormat
import java.util.*
import kotlin.random.Random

fun main(){
    println("============= Hello JServer =============")
    initRxJava()
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

/**
 * reactivex.exceptions.UndeliverableException 처리 함수.
 */
private fun initRxJava() {
    // reactivex.exceptions.UndeliverableException
    // 참고 링크 https://thdev.tech/android/2019/03/04/RxJava2-Error-handling/
    RxJavaPlugins.setErrorHandler { e ->
        var error = e
        if (error is UndeliverableException) {
            error = e.cause
        }
        if (error is IOException || error is SocketException) {
            // fine, irrelevant network problem or API that throws on cancellation
            return@setErrorHandler
        }
        if (error is InterruptedException) {
            // fine, some blocking code was interrupted by a dispose call
            return@setErrorHandler
        }
        if (error is NullPointerException || error is IllegalArgumentException) {
            // that's likely a bug in the application
            Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(
                Thread.currentThread(),
                error
            )
            return@setErrorHandler
        }
        if (error is IllegalStateException) {
            // that's a bug in RxJava or in a custom operator
            Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(
                Thread.currentThread(),
                error
            )
            return@setErrorHandler
        }
    }
}