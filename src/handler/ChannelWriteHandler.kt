package handler

import constants.Constants
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import io.netty.channel.socket.DatagramPacket
import io.netty.util.ReferenceCountUtil
import model.BasePacket

/**
 * 데이터가 큰 스트림을 보내는 경우 ByteBuf 에서 OOM 발생
 * 재활용 하는 ByteBuf를 만들어서 처리하는 함수.
 */
class ChannelWriteHandler : ChannelOutboundHandlerAdapter() {

    private val recycleBuf = Unpooled.buffer(Constants.MAX_BUF)

    override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
        if (ctx == null || msg == null) return

        try {
            if (msg is BasePacket) {
                if (msg.recipient != null) {
                    recycleBuf.retain()
                    recycleBuf.resetWriterIndex()
                    recycleBuf.writeBytes(msg.encode().toByteArray())
                    val packet = DatagramPacket(recycleBuf, msg.recipient)
                    ctx.write(packet, promise)
                    ReferenceCountUtil.release(msg)
                }
            } else if (msg is DatagramPacket) {
                ctx.write(msg, promise)
                ReferenceCountUtil.release(msg)
            } else {
                throw ClassCastException("지웒지 않는 구조체입니다.")
            }
        } catch (ex: Exception) {
            println("Write Error $ex")
        }
    }

    override fun flush(ctx: ChannelHandlerContext?) {
        super.flush(ctx)
    }
}