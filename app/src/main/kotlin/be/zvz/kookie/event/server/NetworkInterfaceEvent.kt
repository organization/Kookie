package be.zvz.kookie.event.server

import be.zvz.kookie.network.NetworkInterface

open class NetworkInterfaceEvent(val networkInterface: NetworkInterface) : ServerEvent()
