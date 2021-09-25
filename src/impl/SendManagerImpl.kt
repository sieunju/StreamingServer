package impl

import io.netty.channel.Channel
import model.BasePacket
import model.ReliablePacket
import service.SendManager
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SendManagerImpl(private val serverChannel: Channel) : SendManager, Runnable {

    private var isRun = false
    private val viewerClientMap : ConcurrentHashMap<InetSocketAddress,Boolean> by lazy { ConcurrentHashMap() }
    private val clientUser: HashSet<InetSocketAddress> by lazy { HashSet() }
    private val sendPacketMap: HashMap<Int, BasePacket> by lazy { HashMap() }
    private val executors: ExecutorService by lazy { Executors.newCachedThreadPool() }
    private var reliableCnt = 0

    override fun run() {
        while (isRun) {
            runCatching {
                if (sendPacketMap.size > 0 && clientUser.size > 0) {
                    println("SendPacket Size ${sendPacketMap.size}")
                    val keys = sendPacketMap.keys
                    for (key in keys) {
                        val packet = sendPacketMap[key]
                        if (packet != null) {
                            clientUser.forEach {
                                packet.recipient = it
                                packet.reliableCnt = key
                                serverChannel.writeAndFlush(packet).sync()
                            }
                        }
                    }
                }
                Thread.sleep(30)
            }
        }
    }

    override fun addPacket(packet: BasePacket) {
        sendPacketMap.put(reliableCnt,packet)
        reliableCnt++
        if(reliableCnt == Int.MAX_VALUE - 1) {
            reliableCnt = 0
        }
        if(!isRun) {
            isRun = true
            executors.submit(this)
        }
    }

    override fun addUser(userInfo: InetSocketAddress) {
        viewerClientMap.put(userInfo,true)
//        clientUser.add(userInfo)
    }

    override fun onReliablePacket(packet: ReliablePacket) {
        // TODO 각 클라이언트 별로 패킷 제거를 해야함.
//        sendPacketMap.remove(packet.uid)
    }
}