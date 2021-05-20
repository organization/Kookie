package be.zvz.kookie.network

import io.netty.buffer.ByteBuf
import java.net.InetSocketAddress

interface AdvancedNetworkInterface {

    /**
     * Prevents packets received from the IP address getting processed for the given timeout.
     */
    fun blockAddress(address: InetSocketAddress, timeout: Long)

    /**
     * Unblocks a previously-blocked address.
     */
    fun unblockAddress(address: InetSocketAddress)

    fun setNetwork(network: Network)

    /**
     * Sends a raw payload to the network interface, bypassing any sessions.
     */
    fun sendRawPacket(address: InetSocketAddress, payload: ByteBuf)

    /**
     * Adds a regex filter for raw packets to this network interface. This filter should be used to check validity of
     * raw packets before relaying them to the main thread.
     */
    fun addRawPacketFilter(regex: Regex)
}
