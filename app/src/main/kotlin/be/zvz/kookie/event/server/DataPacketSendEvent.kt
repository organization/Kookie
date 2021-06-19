package be.zvz.kookie.event.server

import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.network.mcpe.protocol.ClientboundPacket

class DataPacketSendEvent(val targets: List<NetworkSession>, val packets: List<ClientboundPacket>)
