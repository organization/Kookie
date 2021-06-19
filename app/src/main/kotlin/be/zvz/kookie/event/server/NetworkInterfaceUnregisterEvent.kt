package be.zvz.kookie.event.server

import be.zvz.kookie.network.NetworkInterface

class NetworkInterfaceUnregisterEvent(networkInterface: NetworkInterface) : NetworkInterfaceEvent(networkInterface)
