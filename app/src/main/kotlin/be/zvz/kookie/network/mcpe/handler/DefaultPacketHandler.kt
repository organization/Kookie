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

open class DefaultPacketHandler : PacketHandlerInterface {

    override fun handleUnknown(packet: UnknownPacket): Boolean {
        return false
    }

    override fun handleLogin(packet: LoginPacket): Boolean {
        return false
    }

    override fun handlePlayStatus(packet: PlayStatusPacket): Boolean {
        return false
    }

    override fun handleServerToClientHandshake(packet: ServerToClientHandshakePacket): Boolean {
        return false
    }

    override fun handleClientToServerHandshake(packet: ClientToServerHandshakePacket): Boolean {
        return false
    }

    override fun handleDisconnect(packet: DisconnectPacket): Boolean {
        return false
    }

    override fun handleResourcePacksInfo(packet: ResourcePacksInfoPacket): Boolean {
        return false
    }

    override fun handleResourcePackStack(packet: ResourcePackStackPacket): Boolean {
        return false
    }

    override fun handleResourcePackClientResponse(packet: ResourcePackClientResponsePacket): Boolean {
        return false
    }

    override fun handleText(packet: TextPacket): Boolean {
        return false
    }

    override fun handleSetTime(packet: SetTimePacket): Boolean {
        return false
    }

    override fun handleStartGame(packet: StartGamePacket): Boolean {
        return false
    }

    override fun handleAddPlayer(packet: AddPlayerPacket): Boolean {
        return false
    }

    override fun handleAddActor(packet: AddActorPacket): Boolean {
        return false
    }

    override fun handleRemoveActor(packet: RemoveActorPacket): Boolean {
        return false
    }

    override fun handleAddItemActor(packet: AddItemActorPacket): Boolean {
        return false
    }

    override fun handleTakeItemActor(packet: TakeItemActorPacket): Boolean {
        return false
    }

    override fun handleMoveActorAbsolute(packet: MoveActorAbsolutePacket): Boolean {
        return false
    }

    override fun handleMovePlayer(packet: MovePlayerPacket): Boolean {
        return false
    }

    override fun handleRiderJump(packet: RiderJumpPacket): Boolean {
        return false
    }

    override fun handleUpdateBlock(packet: UpdateBlockPacket): Boolean {
        return false
    }

    override fun handleAddPainting(packet: AddPaintingPacket): Boolean {
        return false
    }

    override fun handleTickSync(packet: TickSyncPacket): Boolean {
        return false
    }

    override fun handleLevelSoundEventPacketV1(packet: LevelSoundEventPacketV1): Boolean {
        return false
    }

    override fun handleLevelEvent(packet: LevelEventPacket): Boolean {
        return false
    }

    override fun handleBlockEvent(packet: BlockEventPacket): Boolean {
        return false
    }

    override fun handleActorEvent(packet: ActorEventPacket): Boolean {
        return false
    }

    override fun handleMobEffect(packet: MobEffectPacket): Boolean {
        return false
    }

    override fun handleUpdateAttributes(packet: UpdateAttributesPacket): Boolean {
        return false
    }

    override fun handleInventoryTransaction(packet: InventoryTransactionPacket): Boolean {
        return false
    }

    override fun handleMobEquipment(packet: MobEquipmentPacket): Boolean {
        return false
    }

    override fun handleMobArmorEquipment(packet: MobArmorEquipmentPacket): Boolean {
        return false
    }

    override fun handleInteract(packet: InteractPacket): Boolean {
        return false
    }

    override fun handleBlockPickRequest(packet: BlockPickRequestPacket): Boolean {
        return false
    }

    override fun handleActorPickRequest(packet: ActorPickRequestPacket): Boolean {
        return false
    }

    override fun handlePlayerAction(packet: PlayerActionPacket): Boolean {
        return false
    }

    override fun handleHurtArmor(packet: HurtArmorPacket): Boolean {
        return false
    }

    override fun handleSetActorData(packet: SetActorDataPacket): Boolean {
        return false
    }

    override fun handleSetActorMotion(packet: SetActorMotionPacket): Boolean {
        return false
    }
}
