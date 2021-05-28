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
package be.zvz.kookie.network.mcpe.handler

import be.zvz.kookie.network.mcpe.protocol.*

interface PacketHandlerInterface {

    fun handleUnknown(packet: UnknownPacket): Boolean

    fun handleLogin(packet: LoginPacket): Boolean

    fun handlePlayStatus(packet: PlayStatusPacket): Boolean

    fun handleServerToClientHandshake(packet: ServerToClientHandshakePacket): Boolean

    fun handleClientToServerHandshake(packet: ClientToServerHandshakePacket): Boolean

    fun handleDisconnect(packet: DisconnectPacket): Boolean

    fun handleResourcePacksInfo(packet: ResourcePacksInfoPacket): Boolean

    fun handleResourcePackStack(packet: ResourcePackStackPacket): Boolean

    fun handleResourcePackClientResponse(packet: ResourcePackClientResponsePacket): Boolean

    fun handleText(packet: TextPacket): Boolean

    fun handleSetTime(packet: SetTimePacket): Boolean

    fun handleStartGame(packet: StartGamePacket): Boolean

    fun handleAddPlayer(packet: AddPlayerPacket): Boolean

    fun handleAddActor(packet: AddActorPacket): Boolean

    fun handleRemoveActor(packet: RemoveActorPacket): Boolean

    fun handleAddItemActor(packet: AddItemActorPacket): Boolean

    fun handleTakeItemActor(packet: TakeItemActorPacket): Boolean

    fun handleMoveActorAbsolute(packet: MoveActorAbsolutePacket): Boolean

    fun handleMovePlayer(packet: MovePlayerPacket): Boolean

    fun handleRiderJump(packet: RiderJumpPacket): Boolean

    fun handleUpdateBlock(packet: UpdateBlockPacket): Boolean

    fun handleAddPainting(packet: AddPaintingPacket): Boolean

    fun handleTickSync(packet: TickSyncPacket): Boolean

    fun handleLevelSoundEventPacketV1(packet: LevelSoundEventPacketV1): Boolean

    fun handleLevelEvent(packet: LevelEventPacket): Boolean

    fun handleBlockEvent(packet: BlockEventPacket): Boolean

    fun handleActorEvent(packet: ActorEventPacket): Boolean

    fun handleMobEffect(packet: MobEffectPacket): Boolean

    fun handleUpdateAttributes(packet: UpdateAttributesPacket): Boolean

    fun handleInventoryTransaction(packet: InventoryTransactionPacket): Boolean

    fun handleMobEquipment(packet: MobEquipmentPacket): Boolean

    fun handleMobArmorEquipment(packet: MobArmorEquipmentPacket): Boolean

    fun handleInteract(packet: InteractPacket): Boolean

    fun handleBlockPickRequest(packet: BlockPickRequestPacket): Boolean

    fun handleActorPickRequest(packet: ActorPickRequestPacket): Boolean

    fun handlePlayerAction(packet: PlayerActionPacket): Boolean

    fun handleHurtArmor(packet: HurtArmorPacket): Boolean

    fun handleSetActorData(packet: SetActorDataPacket): Boolean

    fun handleSetActorMotion(packet: SetActorMotionPacket): Boolean

    fun handleAddBehaviorTree(packet: AddBehaviorTreePacket): Boolean

    fun handleAddEntity(packet: AddEntityPacket): Boolean

    fun handleAdventureSettings(packet: AdventureSettingsPacket): Boolean

    fun handleAnimateEntity(packet: AnimateEntityPacket): Boolean

    fun handleAnimate(packet: AnimatePacket): Boolean

    fun handleAnvilDamage(packet: AnvilDamagePacket): Boolean

    fun handleAutomationClientConnect(packet: AutomationClientConnectPacket): Boolean

    fun handleBookEdit(packet: BookEditPacket): Boolean

    fun handleBossEvent(packet: BossEventPacket): Boolean

    fun handleCamera(packet: CameraPacket): Boolean

    fun handleCameraShake(packet: CameraShakePacket): Boolean

    fun handleChangeDimension(packet: ChangeDimensionPacket): Boolean

    fun handleChunkRadiusUpdated(packet: ChunkRadiusUpdatedPacket): Boolean

    fun handleClientboundDebugRenderer(packet: ClientboundDebugRendererPacket): Boolean
}
