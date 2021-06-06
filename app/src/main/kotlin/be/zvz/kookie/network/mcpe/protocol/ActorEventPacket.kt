package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.ACTOR_EVENT_PACKET)
class ActorEventPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    var entityRuntimeId: Long = 0
    var eventId: Int = 0
    var data: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        eventId = input.getByte()
        data = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putByte(eventId)
        output.putVarInt(data)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleActorEvent(this)
    }

    companion object {
        const val JUMP = 1
        const val HURT_ANIMATION = 2
        const val DEATH_ANIMATION = 3
        const val ARM_SWING = 4
        const val STOP_ATTACK = 5
        const val TAME_FAIL = 6
        const val TAME_SUCCESS = 7
        const val SHAKE_WET = 8
        const val USE_ITEM = 9
        const val EAT_GRASS_ANIMATION = 10
        const val FISH_HOOK_BUBBLE = 11
        const val FISH_HOOK_POSITION = 12
        const val FISH_HOOK_HOOK = 13
        const val FISH_HOOK_TEASE = 14
        const val SQUID_INK_CLOUD = 15
        const val ZOMBIE_VILLAGER_CURE = 16
        const val RESPAWN = 18
        const val IRON_GOLEM_OFFER_FLOWER = 19
        const val IRON_GOLEM_WITHDRAW_FLOWER = 20
        const val LOVE_PARTICLES = 21
        const val VILLAGER_ANGRY = 22
        const val VILLAGER_HAPPY = 23
        const val WITCH_SPELL_PARTICLES = 24
        const val FIREWORK_PARTICLES = 25
        const val IN_LOVE_PARTICLES = 26
        const val SILVERFISH_SPAWN_ANIMATION = 27
        const val GUARDIAN_ATTACK = 28
        const val WITCH_DRINK_POTION = 29
        const val WITCH_THROW_POTION = 30
        const val MINECART_TNT_PRIME_FUSE = 31
        const val CREEPER_PRIME_FUSE = 32
        const val AIR_SUPPLY_EXPIRED = 33
        const val PLAYER_ADD_XP_LEVELS = 34
        const val ELDER_GUARDIAN_CURSE = 35
        const val AGENT_ARM_SWING = 36
        const val ENDER_DRAGON_DEATH = 37
        const val DUST_PARTICLES = 38
        const val ARROW_SHAKE = 39
        const val EATING_ITEM = 57
        const val BABY_ANIMAL_FEED = 60
        const val DEATH_SMOKE_CLOUD = 61
        const val COMPLETE_TRADE = 62
        const val REMOVE_LEASH = 63
        const val CONSUME_TOTEM = 65
        const val PLAYER_CHECK_TREASURE_HUNTER_ACHIEVEMENT = 66
        const val ENTITY_SPAWN = 67
        const val DRAGON_PUKE = 68
        const val ITEM_ENTITY_MERGE = 69
        const val START_SWIM = 70
        const val BALLOON_POP = 71
        const val TREASURE_HUNT = 72
        const val AGENT_SUMMON = 73
        const val CHARGED_CROSSBOW = 74
        const val FALL = 75
        const val PID_MASK = 1023
    }
}
