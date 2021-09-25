package impl

import constants.Constants
import converter.DatagramToPacketDecoder
import converter.PacketToDatagramPacketEncoder
import converter.ReliableReceivePacketDecoder
import handler.LoggingHandler
import handler.ReceiveAuthHandler
import handler.ReceiveMessageHandler
import handler.ReceiveVideoHandler
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.FixedRecvByteBufAllocator
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import model.MessagePacket
import model.VideoPacket
import service.NettyServer
import service.OnReceiveMessage
import service.SendManager
import service.ViewerSendManager
import java.net.InetSocketAddress

class NettyServerImpl : NettyServer {

    private val clientChannelSet: HashSet<InetSocketAddress> by lazy { HashSet() }
    private lateinit var group: NioEventLoopGroup
    private lateinit var serverChannel: Channel
    private lateinit var sendManager: SendManager
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
                option(ChannelOption.RCVBUF_ALLOCATOR, FixedRecvByteBufAllocator(Constants.MAX_BUF))
                handler(object : ChannelInitializer<NioDatagramChannel>() {
                    override fun initChannel(ch: NioDatagramChannel?) {
                        if (ch == null) return

                        sendManager = SendManagerImpl(ch)
                        viewerSendManager = ViewerSendManagerImpl(ch)

                        ch.config().sendBufferSize = Constants.MAX_BUF
                        ch.config().receiveBufferSize = Constants.MAX_BUF
//                        ch.config().setRecvByteBufAllocator(FixedRecvByteBufAllocator(1000000))
//                        ch.config().sendBufferSize = 10000000
//                        ch.config().receiveBufferSize = 10000000
                        ch.pipeline().apply {
                            addFirst("log", LoggingHandler())
                            addAfter("log", "decode", ReliableReceivePacketDecoder())
                            addAfter("decode", "packetDecode", DatagramToPacketDecoder(sendManager))

                            addAfter("packetDecode", "auth", ReceiveAuthHandler(sendManager))
                            addAfter("packetDecode", "message", ReceiveMessageHandler(onReceiveMessage))
                            addAfter("packetDecode", "video", ReceiveVideoHandler(onReceiveMessage))

                            addLast(PacketToDatagramPacketEncoder())
                        }
                    }
                })
            }
            serverChannel = bootStrap.bind(port).sync().channel()
        } finally {
            serverChannel.closeFuture().await()
        }
    }

    private val onReceiveMessage = object : OnReceiveMessage {
        override fun onVideo(packet: VideoPacket) {
            sendManager.addPacket(packet)
        }

        override fun onMessage(packet: MessagePacket) {
            val msg = MessagePacket(System.currentTimeMillis().plus(100), packet.msg)
            sendManager.addPacket(msg)
        }
    }
}