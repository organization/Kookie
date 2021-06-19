package be.zvz.kookie.event.server

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.network.NetworkInterface

class NetworkInterfaceRegisterEvent(networkInterface: NetworkInterface) : NetworkInterfaceEvent(networkInterface), Cancellable {
    override var isCancelled: Boolean = false
}
