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

import be.zvz.kookie.network.mcpe.protocol.ActorEventPacket
import be.zvz.kookie.network.mcpe.protocol.ActorPickRequestPacket
import be.zvz.kookie.network.mcpe.protocol.AddActorPacket
import be.zvz.kookie.network.mcpe.protocol.AddBehaviorTreePacket
import be.zvz.kookie.network.mcpe.protocol.AddEntityPacket
import be.zvz.kookie.network.mcpe.protocol.AddItemActorPacket
import be.zvz.kookie.network.mcpe.protocol.AddPaintingPacket
import be.zvz.kookie.network.mcpe.protocol.AddPlayerPacket
import be.zvz.kookie.network.mcpe.protocol.AddVolumeEntityPacket
import be.zvz.kookie.network.mcpe.protocol.AdventureSettingsPacket
import be.zvz.kookie.network.mcpe.protocol.AnimateEntityPacket
import be.zvz.kookie.network.mcpe.protocol.AnimatePacket
import be.zvz.kookie.network.mcpe.protocol.AnvilDamagePacket
import be.zvz.kookie.network.mcpe.protocol.AutomationClientConnectPacket
import be.zvz.kookie.network.mcpe.protocol.AvailableActorIdentifiersPacket
import be.zvz.kookie.network.mcpe.protocol.AvailableCommandsPacket
import be.zvz.kookie.network.mcpe.protocol.BiomeDefinitionListPacket
import be.zvz.kookie.network.mcpe.protocol.BlockActorDataPacket
import be.zvz.kookie.network.mcpe.protocol.BlockEventPacket
import be.zvz.kookie.network.mcpe.protocol.BlockPickRequestPacket
import be.zvz.kookie.network.mcpe.protocol.BookEditPacket
import be.zvz.kookie.network.mcpe.protocol.BossEventPacket
import be.zvz.kookie.network.mcpe.protocol.CameraPacket
import be.zvz.kookie.network.mcpe.protocol.CameraShakePacket
import be.zvz.kookie.network.mcpe.protocol.ChangeDimensionPacket
import be.zvz.kookie.network.mcpe.protocol.ChunkRadiusUpdatedPacket
import be.zvz.kookie.network.mcpe.protocol.ClientCacheBlobStatusPacket
import be.zvz.kookie.network.mcpe.protocol.ClientCacheMissResponsePacket
import be.zvz.kookie.network.mcpe.protocol.ClientCacheStatusPacket
import be.zvz.kookie.network.mcpe.protocol.ClientToServerHandshakePacket
import be.zvz.kookie.network.mcpe.protocol.ClientboundDebugRendererPacket
import be.zvz.kookie.network.mcpe.protocol.ClientboundMapItemDataPacket
import be.zvz.kookie.network.mcpe.protocol.CodeBuilderPacket
import be.zvz.kookie.network.mcpe.protocol.CommandBlockUpdatePacket
import be.zvz.kookie.network.mcpe.protocol.CommandOutputPacket
import be.zvz.kookie.network.mcpe.protocol.CommandRequestPacket
import be.zvz.kookie.network.mcpe.protocol.CompletedUsingItemPacket
import be.zvz.kookie.network.mcpe.protocol.ContainerClosePacket
import be.zvz.kookie.network.mcpe.protocol.ContainerOpenPacket
import be.zvz.kookie.network.mcpe.protocol.ContainerSetDataPacket
import be.zvz.kookie.network.mcpe.protocol.CorrectPlayerMovePredictionPacket
import be.zvz.kookie.network.mcpe.protocol.CraftingDataPacket
import be.zvz.kookie.network.mcpe.protocol.CraftingEventPacket
import be.zvz.kookie.network.mcpe.protocol.CreativeContentPacket
import be.zvz.kookie.network.mcpe.protocol.DebugInfoPacket
import be.zvz.kookie.network.mcpe.protocol.DisconnectPacket
import be.zvz.kookie.network.mcpe.protocol.EducationSettingsPacket
import be.zvz.kookie.network.mcpe.protocol.EmoteListPacket
import be.zvz.kookie.network.mcpe.protocol.EmotePacket
import be.zvz.kookie.network.mcpe.protocol.EventPacket
import be.zvz.kookie.network.mcpe.protocol.FilterTextPacket
import be.zvz.kookie.network.mcpe.protocol.GameRulesChangedPacket
import be.zvz.kookie.network.mcpe.protocol.GuiDataPickItemPacket
import be.zvz.kookie.network.mcpe.protocol.HurtArmorPacket
import be.zvz.kookie.network.mcpe.protocol.InteractPacket
import be.zvz.kookie.network.mcpe.protocol.InventoryContentPacket
import be.zvz.kookie.network.mcpe.protocol.InventorySlotPacket
import be.zvz.kookie.network.mcpe.protocol.InventoryTransactionPacket
import be.zvz.kookie.network.mcpe.protocol.ItemComponentPacket
import be.zvz.kookie.network.mcpe.protocol.ItemFrameDropItemPacket
import be.zvz.kookie.network.mcpe.protocol.ItemStackRequestPacket
import be.zvz.kookie.network.mcpe.protocol.ItemStackResponsePacket
import be.zvz.kookie.network.mcpe.protocol.LabTablePacket
import be.zvz.kookie.network.mcpe.protocol.LecternUpdatePacket
import be.zvz.kookie.network.mcpe.protocol.LevelChunkPacket
import be.zvz.kookie.network.mcpe.protocol.LevelEventGenericPacket
import be.zvz.kookie.network.mcpe.protocol.LevelEventPacket
import be.zvz.kookie.network.mcpe.protocol.LevelSoundEventPacket
import be.zvz.kookie.network.mcpe.protocol.LevelSoundEventPacketV1
import be.zvz.kookie.network.mcpe.protocol.LevelSoundEventPacketV2
import be.zvz.kookie.network.mcpe.protocol.LoginPacket
import be.zvz.kookie.network.mcpe.protocol.MapCreateLockedCopyPacket
import be.zvz.kookie.network.mcpe.protocol.MapInfoRequestPacket
import be.zvz.kookie.network.mcpe.protocol.MobArmorEquipmentPacket
import be.zvz.kookie.network.mcpe.protocol.MobEffectPacket
import be.zvz.kookie.network.mcpe.protocol.MobEquipmentPacket
import be.zvz.kookie.network.mcpe.protocol.ModalFormRequestPacket
import be.zvz.kookie.network.mcpe.protocol.ModalFormResponsePacket
import be.zvz.kookie.network.mcpe.protocol.MotionPredictionHintsPacket
import be.zvz.kookie.network.mcpe.protocol.MoveActorAbsolutePacket
import be.zvz.kookie.network.mcpe.protocol.MoveActorDeltaPacket
import be.zvz.kookie.network.mcpe.protocol.MovePlayerPacket
import be.zvz.kookie.network.mcpe.protocol.MultiplayerSettingsPacket
import be.zvz.kookie.network.mcpe.protocol.NetworkChunkPublisherUpdatePacket
import be.zvz.kookie.network.mcpe.protocol.NetworkSettingsPacket
import be.zvz.kookie.network.mcpe.protocol.NetworkStackLatencyPacket
import be.zvz.kookie.network.mcpe.protocol.NpcDialoguePacket
import be.zvz.kookie.network.mcpe.protocol.NpcRequestPacket
import be.zvz.kookie.network.mcpe.protocol.OnScreenTextureAnimationPacket
import be.zvz.kookie.network.mcpe.protocol.PacketViolationWarningPacket
import be.zvz.kookie.network.mcpe.protocol.PhotoTransferPacket
import be.zvz.kookie.network.mcpe.protocol.PlaySoundPacket
import be.zvz.kookie.network.mcpe.protocol.PlayStatusPacket
import be.zvz.kookie.network.mcpe.protocol.PlayerActionPacket
import be.zvz.kookie.network.mcpe.protocol.PlayerArmorDamagePacket
import be.zvz.kookie.network.mcpe.protocol.PlayerAuthInputPacket
import be.zvz.kookie.network.mcpe.protocol.PlayerEnchantOptionsPacket
import be.zvz.kookie.network.mcpe.protocol.PlayerFogPacket
import be.zvz.kookie.network.mcpe.protocol.PlayerHotbarPacket
import be.zvz.kookie.network.mcpe.protocol.PlayerInputPacket
import be.zvz.kookie.network.mcpe.protocol.PlayerListPacket
import be.zvz.kookie.network.mcpe.protocol.PlayerSkinPacket
import be.zvz.kookie.network.mcpe.protocol.PositionTrackingDBClientRequestPacket
import be.zvz.kookie.network.mcpe.protocol.PositionTrackingDBServerBroadcastPacket
import be.zvz.kookie.network.mcpe.protocol.PurchaseReceiptPacket
import be.zvz.kookie.network.mcpe.protocol.RemoveActorPacket
import be.zvz.kookie.network.mcpe.protocol.RemoveEntityPacket
import be.zvz.kookie.network.mcpe.protocol.RemoveObjectivePacket
import be.zvz.kookie.network.mcpe.protocol.RemoveVolumeEntityPacket
import be.zvz.kookie.network.mcpe.protocol.RequestChunkRadiusPacket
import be.zvz.kookie.network.mcpe.protocol.ResourcePackChunkDataPacket
import be.zvz.kookie.network.mcpe.protocol.ResourcePackChunkRequestPacket
import be.zvz.kookie.network.mcpe.protocol.ResourcePackClientResponsePacket
import be.zvz.kookie.network.mcpe.protocol.ResourcePackDataInfoPacket
import be.zvz.kookie.network.mcpe.protocol.ResourcePackStackPacket
import be.zvz.kookie.network.mcpe.protocol.ResourcePacksInfoPacket
import be.zvz.kookie.network.mcpe.protocol.RespawnPacket
import be.zvz.kookie.network.mcpe.protocol.RiderJumpPacket
import be.zvz.kookie.network.mcpe.protocol.ScriptCustomEventPacket
import be.zvz.kookie.network.mcpe.protocol.ServerSettingsRequestPacket
import be.zvz.kookie.network.mcpe.protocol.ServerSettingsResponsePacket
import be.zvz.kookie.network.mcpe.protocol.ServerToClientHandshakePacket
import be.zvz.kookie.network.mcpe.protocol.SetActorDataPacket
import be.zvz.kookie.network.mcpe.protocol.SetActorLinkPacket
import be.zvz.kookie.network.mcpe.protocol.SetActorMotionPacket
import be.zvz.kookie.network.mcpe.protocol.SetCommandsEnabledPacket
import be.zvz.kookie.network.mcpe.protocol.SetDefaultGameTypePacket
import be.zvz.kookie.network.mcpe.protocol.SetDifficultyPacket
import be.zvz.kookie.network.mcpe.protocol.SetDisplayObjectivePacket
import be.zvz.kookie.network.mcpe.protocol.SetHealthPacket
import be.zvz.kookie.network.mcpe.protocol.SetLastHurtByPacket
import be.zvz.kookie.network.mcpe.protocol.SetLocalPlayerAsInitializedPacket
import be.zvz.kookie.network.mcpe.protocol.SetPlayerGameTypePacket
import be.zvz.kookie.network.mcpe.protocol.SetScorePacket
import be.zvz.kookie.network.mcpe.protocol.SetScoreboardIdentityPacket
import be.zvz.kookie.network.mcpe.protocol.SetSpawnPositionPacket
import be.zvz.kookie.network.mcpe.protocol.SetTimePacket
import be.zvz.kookie.network.mcpe.protocol.SetTitlePacket
import be.zvz.kookie.network.mcpe.protocol.SettingsCommandPacket
import be.zvz.kookie.network.mcpe.protocol.ShowCreditsPacket
import be.zvz.kookie.network.mcpe.protocol.ShowProfilePacket
import be.zvz.kookie.network.mcpe.protocol.ShowStoreOfferPacket
import be.zvz.kookie.network.mcpe.protocol.SimpleEventPacket
import be.zvz.kookie.network.mcpe.protocol.SimulationTypePacket
import be.zvz.kookie.network.mcpe.protocol.SpawnExperienceOrbPacket
import be.zvz.kookie.network.mcpe.protocol.SpawnParticleEffectPacket
import be.zvz.kookie.network.mcpe.protocol.StartGamePacket
import be.zvz.kookie.network.mcpe.protocol.StopSoundPacket
import be.zvz.kookie.network.mcpe.protocol.StructureBlockUpdatePacket
import be.zvz.kookie.network.mcpe.protocol.StructureTemplateDataRequestPacket
import be.zvz.kookie.network.mcpe.protocol.StructureTemplateDataResponsePacket
import be.zvz.kookie.network.mcpe.protocol.SubClientLoginPacket
import be.zvz.kookie.network.mcpe.protocol.SyncActorPropertyPacket
import be.zvz.kookie.network.mcpe.protocol.TakeItemActorPacket
import be.zvz.kookie.network.mcpe.protocol.TextPacket
import be.zvz.kookie.network.mcpe.protocol.TickSyncPacket
import be.zvz.kookie.network.mcpe.protocol.TransferPacket
import be.zvz.kookie.network.mcpe.protocol.UnknownPacket
import be.zvz.kookie.network.mcpe.protocol.UpdateAttributesPacket
import be.zvz.kookie.network.mcpe.protocol.UpdateBlockPacket
import be.zvz.kookie.network.mcpe.protocol.UpdateBlockSyncedPacket
import be.zvz.kookie.network.mcpe.protocol.UpdateEquipPacket
import be.zvz.kookie.network.mcpe.protocol.UpdatePlayerGameTypePacket
import be.zvz.kookie.network.mcpe.protocol.UpdateSoftEnumPacket
import be.zvz.kookie.network.mcpe.protocol.UpdateTradePacket

open class DefaultPacketHandler : PacketHandlerInterface {
    override fun handleUnknown(packet: UnknownPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleLogin(packet: LoginPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlayStatus(packet: PlayStatusPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleServerToClientHandshake(packet: ServerToClientHandshakePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleClientToServerHandshake(packet: ClientToServerHandshakePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleDisconnect(packet: DisconnectPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleResourcePacksInfo(packet: ResourcePacksInfoPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleResourcePackStack(packet: ResourcePackStackPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleResourcePackClientResponse(packet: ResourcePackClientResponsePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleText(packet: TextPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetTime(packet: SetTimePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleStartGame(packet: StartGamePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAddPlayer(packet: AddPlayerPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAddActor(packet: AddActorPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleRemoveActor(packet: RemoveActorPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAddItemActor(packet: AddItemActorPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleTakeItemActor(packet: TakeItemActorPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleMoveActorAbsolute(packet: MoveActorAbsolutePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleMovePlayer(packet: MovePlayerPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleRiderJump(packet: RiderJumpPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleUpdateBlock(packet: UpdateBlockPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAddPainting(packet: AddPaintingPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleTickSync(packet: TickSyncPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleLevelSoundEventPacketV1(packet: LevelSoundEventPacketV1): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleLevelEvent(packet: LevelEventPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleBlockEvent(packet: BlockEventPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleActorEvent(packet: ActorEventPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleMobEffect(packet: MobEffectPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleUpdateAttributes(packet: UpdateAttributesPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleInventoryTransaction(packet: InventoryTransactionPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleMobEquipment(packet: MobEquipmentPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleMobArmorEquipment(packet: MobArmorEquipmentPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleInteract(packet: InteractPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleBlockPickRequest(packet: BlockPickRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleActorPickRequest(packet: ActorPickRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlayerAction(packet: PlayerActionPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleHurtArmor(packet: HurtArmorPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetActorData(packet: SetActorDataPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetActorMotion(packet: SetActorMotionPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetActorLink(packet: SetActorLinkPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetHealth(packet: SetHealthPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetSpawnPosition(packet: SetSpawnPositionPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAnimate(packet: AnimatePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleRespawn(packet: RespawnPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleContainerOpen(packet: ContainerOpenPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleContainerClose(packet: ContainerClosePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlayerHotbar(packet: PlayerHotbarPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleInventoryContent(packet: InventoryContentPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleInventorySlot(packet: InventorySlotPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleContainerSetData(packet: ContainerSetDataPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCraftingData(packet: CraftingDataPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCraftingEvent(packet: CraftingEventPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleGuiDataPickItem(packet: GuiDataPickItemPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAdventureSettings(packet: AdventureSettingsPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleBlockActorData(packet: BlockActorDataPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlayerInput(packet: PlayerInputPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleLevelChunk(packet: LevelChunkPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetCommandsEnabled(packet: SetCommandsEnabledPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetDifficulty(packet: SetDifficultyPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleChangeDimension(packet: ChangeDimensionPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetPlayerGameType(packet: SetPlayerGameTypePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlayerList(packet: PlayerListPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSimpleEvent(packet: SimpleEventPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleEvent(packet: EventPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSpawnExperienceOrb(packet: SpawnExperienceOrbPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleClientboundMapItemData(packet: ClientboundMapItemDataPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleMapInfoRequest(packet: MapInfoRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleRequestChunkRadius(packet: RequestChunkRadiusPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleChunkRadiusUpdated(packet: ChunkRadiusUpdatedPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleItemFrameDropItem(packet: ItemFrameDropItemPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleGameRulesChanged(packet: GameRulesChangedPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCamera(packet: CameraPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleBossEvent(packet: BossEventPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleShowCredits(packet: ShowCreditsPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAvailableCommands(packet: AvailableCommandsPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCommandRequest(packet: CommandRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCommandBlockUpdate(packet: CommandBlockUpdatePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCommandOutput(packet: CommandOutputPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleUpdateTrade(packet: UpdateTradePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleUpdateEquip(packet: UpdateEquipPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleResourcePackDataInfo(packet: ResourcePackDataInfoPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleResourcePackChunkData(packet: ResourcePackChunkDataPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleResourcePackChunkRequest(packet: ResourcePackChunkRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleTransfer(packet: TransferPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlaySound(packet: PlaySoundPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleStopSound(packet: StopSoundPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetTitle(packet: SetTitlePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAddBehaviorTree(packet: AddBehaviorTreePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleStructureBlockUpdate(packet: StructureBlockUpdatePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleShowStoreOffer(packet: ShowStoreOfferPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePurchaseReceipt(packet: PurchaseReceiptPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlayerSkin(packet: PlayerSkinPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSubClientLogin(packet: SubClientLoginPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAutomationClientConnect(packet: AutomationClientConnectPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetLastHurtBy(packet: SetLastHurtByPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleBookEdit(packet: BookEditPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleNpcRequest(packet: NpcRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePhotoTransfer(packet: PhotoTransferPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleModalFormRequest(packet: ModalFormRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleModalFormResponse(packet: ModalFormResponsePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleServerSettingsRequest(packet: ServerSettingsRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleServerSettingsResponse(packet: ServerSettingsResponsePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleShowProfile(packet: ShowProfilePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetDefaultGameType(packet: SetDefaultGameTypePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleRemoveObjective(packet: RemoveObjectivePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetDisplayObjective(packet: SetDisplayObjectivePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetScore(packet: SetScorePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleLabTable(packet: LabTablePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleUpdateBlockSynced(packet: UpdateBlockSyncedPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleMoveActorDelta(packet: MoveActorDeltaPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetScoreboardIdentity(packet: SetScoreboardIdentityPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSetLocalPlayerAsInitialized(packet: SetLocalPlayerAsInitializedPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleUpdateSoftEnum(packet: UpdateSoftEnumPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleNetworkStackLatency(packet: NetworkStackLatencyPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleScriptCustomEvent(packet: ScriptCustomEventPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSpawnParticleEffect(packet: SpawnParticleEffectPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAvailableActorIdentifiers(packet: AvailableActorIdentifiersPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleLevelSoundEventPacketV2(packet: LevelSoundEventPacketV2): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleNetworkChunkPublisherUpdate(packet: NetworkChunkPublisherUpdatePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleBiomeDefinitionList(packet: BiomeDefinitionListPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleLevelSoundEvent(packet: LevelSoundEventPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleLevelEventGeneric(packet: LevelEventGenericPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleLecternUpdate(packet: LecternUpdatePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAddEntity(packet: AddEntityPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleRemoveEntity(packet: RemoveEntityPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleClientCacheStatus(packet: ClientCacheStatusPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleOnScreenTextureAnimation(packet: OnScreenTextureAnimationPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleMapCreateLockedCopy(packet: MapCreateLockedCopyPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleStructureTemplateDataRequest(packet: StructureTemplateDataRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleStructureTemplateDataResponse(packet: StructureTemplateDataResponsePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleClientCacheBlobStatus(packet: ClientCacheBlobStatusPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleClientCacheMissResponse(packet: ClientCacheMissResponsePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleEducationSettings(packet: EducationSettingsPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleEmote(packet: EmotePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleMultiplayerSettings(packet: MultiplayerSettingsPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSettingsCommand(packet: SettingsCommandPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAnvilDamage(packet: AnvilDamagePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCompletedUsingItem(packet: CompletedUsingItemPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleNetworkSettings(packet: NetworkSettingsPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlayerAuthInput(packet: PlayerAuthInputPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCreativeContent(packet: CreativeContentPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlayerEnchantOptions(packet: PlayerEnchantOptionsPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleItemStackRequest(packet: ItemStackRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleItemStackResponse(packet: ItemStackResponsePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlayerArmorDamage(packet: PlayerArmorDamagePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCodeBuilder(packet: CodeBuilderPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleUpdatePlayerGameType(packet: UpdatePlayerGameTypePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleEmoteList(packet: EmoteListPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePositionTrackingDBServerBroadcast(packet: PositionTrackingDBServerBroadcastPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePositionTrackingDBClientRequest(packet: PositionTrackingDBClientRequestPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleDebugInfo(packet: DebugInfoPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePacketViolationWarning(packet: PacketViolationWarningPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleMotionPredictionHints(packet: MotionPredictionHintsPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAnimateEntity(packet: AnimateEntityPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCameraShake(packet: CameraShakePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handlePlayerFog(packet: PlayerFogPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleCorrectPlayerMovePrediction(packet: CorrectPlayerMovePredictionPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleItemComponent(packet: ItemComponentPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleFilterText(packet: FilterTextPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleClientboundDebugRenderer(packet: ClientboundDebugRendererPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSyncActorProperty(packet: SyncActorPropertyPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAddVolumeEntity(packet: AddVolumeEntityPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleRemoveVolumeEntity(packet: RemoveVolumeEntityPacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleSimulationType(packet: SimulationTypePacket): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleNpcDialogue(packet: NpcDialoguePacket): Boolean {
        TODO("Not yet implemented")
    }
}
