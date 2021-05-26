package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.LEVEL_EVENT_PACKET)
class LevelEventPacket : DataPacket(), ClientboundPacket {
    var evid: Int = 0
    var position: Vector3? = null
    var data: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        evid = input.getVarInt()
        position = input.getVector3()
        data = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(evid)
        output.putVector3Nullable(position)
        output.putVarInt(data)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleLevelEvent(this)
    }

    companion object {
        const val EVENT_SOUND_CLICK = 1000
        const val EVENT_SOUND_CLICK_FAIL = 1001
        const val EVENT_SOUND_SHOOT = 1002
        const val EVENT_SOUND_DOOR = 1003
        const val EVENT_SOUND_FIZZ = 1004
        const val EVENT_SOUND_IGNITE = 1005
        const val EVENT_SOUND_GHAST = 1007
        const val EVENT_SOUND_GHAST_SHOOT = 1008
        const val EVENT_SOUND_BLAZE_SHOOT = 1009
        const val EVENT_SOUND_DOOR_BUMP = 1010
        const val EVENT_SOUND_DOOR_CRASH = 1012
        const val EVENT_SOUND_ENDERMAN_TELEPORT = 1018
        const val EVENT_SOUND_ANVIL_BREAK = 1020
        const val EVENT_SOUND_ANVIL_USE = 1021
        const val EVENT_SOUND_ANVIL_FALL = 1022
        const val EVENT_SOUND_POP = 1030
        const val EVENT_SOUND_PORTAL = 1032
        const val EVENT_SOUND_ITEMFRAME_ADD_ITEM = 1040
        const val EVENT_SOUND_ITEMFRAME_REMOVE = 1041
        const val EVENT_SOUND_ITEMFRAME_PLACE = 1042
        const val EVENT_SOUND_ITEMFRAME_REMOVE_ITEM = 1043
        const val EVENT_SOUND_ITEMFRAME_ROTATE_ITEM = 1044
        const val EVENT_SOUND_CAMERA = 1050
        const val EVENT_SOUND_ORB = 1051
        const val EVENT_SOUND_TOTEM = 1052
        const val EVENT_SOUND_ARMOR_STAND_BREAK = 1060
        const val EVENT_SOUND_ARMOR_STAND_HIT = 1061
        const val EVENT_SOUND_ARMOR_STAND_FALL = 1062
        const val EVENT_SOUND_ARMOR_STAND_PLACE = 1063
        const val EVENT_PARTICLE_SHOOT = 2000
        const val EVENT_PARTICLE_DESTROY = 2001
        const val EVENT_PARTICLE_SPLASH = 2002
        const val EVENT_PARTICLE_EYE_DESPAWN = 2003
        const val EVENT_PARTICLE_SPAWN = 2004
        const val EVENT_GUARDIAN_CURSE = 2006
        const val EVENT_PARTICLE_BLOCK_FORCE_FIELD = 2008
        const val EVENT_PARTICLE_PROJECTILE_HIT = 2009
        const val EVENT_PARTICLE_DRAGON_EGG_TELEPORT = 2010
        const val EVENT_PARTICLE_ENDERMAN_TELEPORT = 2013
        const val EVENT_PARTICLE_PUNCH_BLOCK = 2014
        const val EVENT_START_RAIN = 3001
        const val EVENT_START_THUNDER = 3002
        const val EVENT_STOP_RAIN = 3003
        const val EVENT_STOP_THUNDER = 3004
        const val EVENT_PAUSE_GAME = 3005
        const val EVENT_PAUSE_GAME_NO_SCREEN = 3006
        const val EVENT_SET_GAME_SPEED = 3007
        const val EVENT_REDSTONE_TRIGGER = 3500
        const val EVENT_CAULDRON_EXPLODE = 3501
        const val EVENT_CAULDRON_DYE_ARMOR = 3502
        const val EVENT_CAULDRON_CLEAN_ARMOR = 3503
        const val EVENT_CAULDRON_FILL_POTION = 3504
        const val EVENT_CAULDRON_TAKE_POTION = 3505
        const val EVENT_CAULDRON_FILL_WATER = 3506
        const val EVENT_CAULDRON_TAKE_WATER = 3507
        const val EVENT_CAULDRON_ADD_DYE = 3508
        const val EVENT_CAULDRON_CLEAN_BANNER = 3509
        const val EVENT_BLOCK_START_BREAK = 3600
        const val EVENT_BLOCK_STOP_BREAK = 3601
        const val EVENT_SET_DATA = 4000
        const val EVENT_PLAYERS_SLEEPING = 9800
        const val EVENT_ADD_PARTICLE_MASK = 16384
        const val PID_MASK = 1023

        fun create(evid: Int, data: Int, pos: Vector3? = null): LevelEventPacket {
            return LevelEventPacket().apply {
                this.evid = evid
                this.data = data
                this.position = pos
            }
        }

        fun standardParticle(particleId: Int, data: Int, pos: Vector3): LevelEventPacket {
            return create(EVENT_ADD_PARTICLE_MASK or particleId, data, pos)
        }
    }
}
