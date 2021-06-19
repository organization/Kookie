package be.zvz.kookie.event.server

import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.network.mcpe.protocol.ServerboundPacket

class DataPacketReceiveEvent(val origin: NetworkSession, val packet: ServerboundPacket) : ServerEvent()
