package impl

import constants.Constants
import converter.DatagramToPacketDecoder
import converter.ReliableReceivePacketDecoder
import handler.*
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.FixedRecvByteBufAllocator
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.util.concurrent.DefaultEventExecutor
import io.netty.util.concurrent.DefaultEventExecutorGroup
import io.netty.util.concurrent.DefaultThreadFactory
import io.netty.util.concurrent.EventExecutorGroup
import io.reactivex.schedulers.Schedulers
import model.BasePacket
import model.MessagePacket
import model.VideoPacket
import service.NettyServer
import service.OnReceiveMessage
import service.ViewerSendManager
import java.net.InetAddress

class NettyServerImpl : NettyServer {

    private lateinit var group: NioEventLoopGroup
    private lateinit var serverChannel: Channel
    private lateinit var viewerSendManager: ViewerSendManager

    override fun start(port: Int) {
        try {
            group = NioEventLoopGroup()
            val bootStrap = Bootstrap().apply {
                group(group)
                channel(NioDatagramChannel::class.java)
                option(ChannelOption.SO_BROADCAST, true)
                option(ChannelOption.SO_SNDBUF, Constants.MAX_BUF)
                option(ChannelOption.SO_RCVBUF, Constants.MAX_BUF)
//                option(ChannelOption.RCVBUF_ALLOCATOR, FixedRecvByteBufAllocator(Constants.MAX_BUF))
                handler(object : ChannelInitializer<NioDatagramChannel>() {
                    override fun initChannel(ch: NioDatagramChannel?) {
                        if (ch == null) return

                        viewerSendManager = ViewerSendManagerImpl(ch)

//                        ch.config().sendBufferSize = Constants.MAX_BUF
//                        ch.config().receiveBufferSize = Constants.MAX_BUF
//                        ch.config().setRecvByteBufAllocator(FixedRecvByteBufAllocator(1000000))
//                        ch.config().sendBufferSize = 10000000
//                        ch.config().receiveBufferSize = 10000000
                        ch.pipeline().apply {
                            addFirst("log", LoggingHandler())
                            addAfter("log", "decode", ReliableReceivePacketDecoder(ch))
                            addAfter("decode", "packetDecode", DatagramToPacketDecoder(viewerSendManager))

                            addAfter("packetDecode", "auth", ReceiveAuthHandler(viewerSendManager))
                            addAfter("packetDecode", "message", ReceiveMessageHandler(onReceiveMessage))
                            addAfter("packetDecode", "video", ReceiveVideoHandler(onReceiveMessage))
                            addLast(DefaultEventExecutorGroup(4,DefaultThreadFactory("w")),ChannelWriteHandler())
                        }
                    }
                })
            }
            serverChannel = bootStrap.bind(port).sync().channel()
            println("Server IP ${InetAddress.getLocalHost()} Port ${port}")
        } finally {
            serverChannel.closeFuture().await()
        }
    }

    override fun testVideo(packet: VideoPacket) {
//        viewerSendManager.addVideo(packet)
    }

    override fun testSend(packet: BasePacket) {
        serverChannel.writeAndFlush(packet)
    }

    private val onReceiveMessage = object : OnReceiveMessage {
        override fun onVideo(packet: VideoPacket) {
            viewerSendManager.addVideo(packet)
        }

        override fun onMessage(packet: MessagePacket) {
            val msg = MessagePacket(System.currentTimeMillis().plus(100), packet.msg)
        }
    }
}