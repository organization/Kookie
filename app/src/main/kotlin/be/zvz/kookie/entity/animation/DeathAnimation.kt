package be.zvz.kookie.entity.animation

import be.zvz.kookie.entity.Living
import be.zvz.kookie.network.mcpe.protocol.ActorEventPacket
import be.zvz.kookie.network.mcpe.protocol.ClientboundPacket

class DeathAnimation(private val entity: Living) : Animation {

    override fun encode(): List<ClientboundPacket> {
        return listOf(
            ActorEventPacket().apply {
                entityRuntimeId = entity.getId()
                eventId = ActorEventPacket.DEATH_ANIMATION
                data = 0
            }
        )
    }
}
