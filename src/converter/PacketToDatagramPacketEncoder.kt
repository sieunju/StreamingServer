package converter

import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageEncoder
import model.BasePacket
import java.net.InetSocketAddress

/**
 * Description : BasePacket to String
 *
 * Created by juhongmin on 9/17/21
 */
class PacketToDatagramPacketEncoder : MessageToMessageEncoder<BasePacket>() {

    private var sender: InetSocketAddress? = null

    override fun encode(ctx: ChannelHandlerContext?, msg: BasePacket?, out: MutableList<Any>?) {
        if (ctx == null || msg == null || out == null) return
        if (sender == null) sender = ctx.channel().localAddress() as InetSocketAddress

        if (msg.recipient != null) {
            val buf = PooledByteBufAllocator.DEFAULT.directBuffer()
            buf.writeBytes(msg.encode().toByteArray())
            out.add(DatagramPacket(buf, msg.recipient, sender))
        }
    }
}