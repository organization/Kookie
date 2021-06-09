/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.VersionInfo
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.LittleEndianNbtSerializer
import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.BlockPaletteEntry
import be.zvz.kookie.network.mcpe.protocol.types.EducationOffer
import be.zvz.kookie.network.mcpe.protocol.types.Experiments
import be.zvz.kookie.network.mcpe.protocol.types.GameRule
import be.zvz.kookie.network.mcpe.protocol.types.GeneratorType
import be.zvz.kookie.network.mcpe.protocol.types.ItemTypeEntry
import be.zvz.kookie.network.mcpe.protocol.types.MultiplayerGameVisibility
import be.zvz.kookie.network.mcpe.protocol.types.PlayerMovementSettings
import be.zvz.kookie.network.mcpe.protocol.types.PlayerPermissions
import be.zvz.kookie.network.mcpe.protocol.types.SpawnSettings
import com.koloboke.collect.map.hash.HashObjObjMaps

@ProtocolIdentify(ProtocolInfo.IDS.START_GAME_PACKET)
class StartGamePacket : DataPacket(), ClientboundPacket {

    var entityUniqueId: Long = -1L
    var entityRuntimeId: Long = -1L

    var playerGamemode: Int = -1

    lateinit var playerPosition: Vector3

    var pitch: Float = -1F
    var yaw: Float = -1F

    var seed: Int = -1
    lateinit var spawnSettings: SpawnSettings

    var generator: Int = GeneratorType.FINITE_OVERWORLD
    var worldGameMode: Int = -1
    var difficulty: Int = -1

    var spawnX: Int = -1
    var spawnY: Int = -1
    var spawnZ: Int = -1

    var hasAchievementsDisabled: Boolean = true
    var time: Int = -1
    var eduEditionOffer: Int = EducationOffer.NONE
    var hasEduFeaturesEnabled: Boolean = false
    var eduProductUUID: String = ""
    var rainLevel: Float = -1F
    var lightningLevel: Float = -1F
    var hasConfirmPlatformLockedContent: Boolean = false
    var isMultiplayerGame: Boolean = true
    var hasLANBroadcast: Boolean = true
    var xboxLiveBroadcastMode: Int = MultiplayerGameVisibility.PUBLIC
    var platformBroadcastMode: Int = MultiplayerGameVisibility.PUBLIC
    var commandsEnabled: Boolean = true
    var isTexturePacksRequired: Boolean = true
    var gameRules: MutableMap<String, GameRule> = HashObjObjMaps.newMutableMap()
    lateinit var experiments: Experiments
    var hasBonusChestEnabled: Boolean = false
    var hasStartWithMapEnabled: Boolean = false
    var defaultPlayerPermission: Int = PlayerPermissions.MEMBER // TODO

    var serverChunkTickRadius: Int = 4 // TODO (leave as default for now)

    var hasLockedBehaviorPack: Boolean = false
    var hasLockedResourcePack: Boolean = false
    var isFromLockedWorldTemplate: Boolean = false
    var useMsaGamerTagsOnly: Boolean = false // TODO: What is this?
    var isFromWorldTemplate: Boolean = false
    var isWorldTemplateOptionLimited: Boolean = false
    var onlySpawnV1Villagers: Boolean = false
    var vanillaVersion: String = ProtocolInfo.MINECRAFT_VERSION_NETWORK
    var limitedWorldWidth: Int = 0
    var limitedWorldLength: Int = 0
    var isNewNether: Boolean = true
    var experimentalGameplayOverride: Boolean? = null

    var levelId: String = "" // base64 string, usually the same as world folder name in vanilla
    var worldName: String = ""
    var premiumWorldTemplateId: String = ""
    var isTrial: Boolean = false
    lateinit var playerMovementSettings: PlayerMovementSettings
    var currentTick: Long = 0L // only used if isTrial is true
    var enchantmentSeed: Int = 0
    var multiplayerCorrelationId: String = "" // TODO: this should be filled with a UUID of some sort
    var enableNewInventorySystem: Boolean = false // TODO

    var blockPalette: MutableList<BlockPaletteEntry> = mutableListOf()
    var itemTable: MutableList<ItemTypeEntry> = mutableListOf()
    var serverSoftwareVersion: String = VersionInfo.NAME + " v" + VersionInfo.BASE_VERSION +
        if (VersionInfo.IS_DEVELOPMENT_BUILD) "-dev" else ""

    override fun decodePayload(input: PacketSerializer) {
        throw PacketDecodeException("Cannot decode StartGamePacket, it is TODO!")
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityUniqueId(entityUniqueId)
        output.putEntityRuntimeId(entityRuntimeId)
        output.putVarInt(playerGamemode)

        output.putVector3(playerPosition)

        output.putLFloat(pitch)
        output.putLFloat(yaw)

        output.putVarInt(seed)
        spawnSettings.write(output)
        output.putVarInt(generator)
        output.putVarInt(worldGameMode)
        output.putVarInt(difficulty)
        output.putBlockPosition(spawnX, spawnY, spawnZ)
        output.putBoolean(hasAchievementsDisabled)
        output.putVarInt(time)
        output.putVarInt(eduEditionOffer)
        output.putBoolean(hasEduFeaturesEnabled)
        output.putString(eduProductUUID)
        output.putLFloat(rainLevel)
        output.putLFloat(lightningLevel)
        output.putBoolean(hasConfirmPlatformLockedContent)
        output.putBoolean(isMultiplayerGame)
        output.putBoolean(hasLANBroadcast)
        output.putVarInt(xboxLiveBroadcastMode)
        output.putVarInt(platformBroadcastMode)
        output.putBoolean(commandsEnabled)
        output.putBoolean(isTexturePacksRequired)
        output.putGameRules(gameRules)
        experiments.write(output)
        output.putBoolean(hasBonusChestEnabled)
        output.putBoolean(hasStartWithMapEnabled)
        output.putVarInt(defaultPlayerPermission)
        output.putLInt(serverChunkTickRadius)
        output.putBoolean(hasLockedBehaviorPack)
        output.putBoolean(hasLockedResourcePack)
        output.putBoolean(isFromLockedWorldTemplate)
        output.putBoolean(useMsaGamerTagsOnly)
        output.putBoolean(isFromWorldTemplate)
        output.putBoolean(isWorldTemplateOptionLimited)
        output.putBoolean(onlySpawnV1Villagers)
        output.putString(vanillaVersion)
        output.putLInt(limitedWorldWidth)
        output.putLInt(limitedWorldLength)
        output.putBoolean(isNewNether)
        output.putBoolean(experimentalGameplayOverride != null)
        if (experimentalGameplayOverride != null) {
            output.putBoolean(experimentalGameplayOverride!!)
        }
        output.putString(levelId)
        output.putString(worldName)
        output.putString(premiumWorldTemplateId)
        output.putBoolean(isTrial)
        playerMovementSettings.write(output)
        output.putLLong(currentTick)
        output.putVarInt(enchantmentSeed)

        output.putUnsignedVarInt(blockPalette.size)
        val nbtWriter = LittleEndianNbtSerializer()
        blockPalette.forEach {
            output.putString(it.getName())
            output.put(nbtWriter.write(TreeRoot(it.getStates())))
        }
        output.putUnsignedVarInt(itemTable.size)
        itemTable.forEach {
            output.putString(it.stringId)
            output.putLShort(it.numericId)
            output.putBoolean(it.componentBased)
        }
        output.putString(multiplayerCorrelationId)
        output.putBoolean(enableNewInventorySystem)
        output.putString(serverSoftwareVersion)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        TODO("Not yet implemented")
    }
}
