package be.zvz.kookie.event.server

import be.zvz.kookie.network.NetworkInterface

abstract class NetworkInterfaceEvent(val networkInterface: NetworkInterface) : ServerEvent()
