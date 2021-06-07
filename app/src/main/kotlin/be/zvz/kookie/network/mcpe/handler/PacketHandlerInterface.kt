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
import be.zvz.kookie.network.mcpe.protocol.SpawnExperienceOrbPacket
import be.zvz.kookie.network.mcpe.protocol.SpawnParticleEffectPacket
import be.zvz.kookie.network.mcpe.protocol.StartGamePacket
import be.zvz.kookie.network.mcpe.protocol.StopSoundPacket
import be.zvz.kookie.network.mcpe.protocol.StructureBlockUpdatePacket
import be.zvz.kookie.network.mcpe.protocol.StructureTemplateDataRequestPacket
import be.zvz.kookie.network.mcpe.protocol.StructureTemplateDataResponsePacket
import be.zvz.kookie.network.mcpe.protocol.SubClientLoginPacket
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

    fun handleSetActorLink(packet: SetActorLinkPacket): Boolean

    fun handleSetHealth(packet: SetHealthPacket): Boolean

    fun handleSetSpawnPosition(packet: SetSpawnPositionPacket): Boolean

    fun handleAnimate(packet: AnimatePacket): Boolean

    fun handleRespawn(packet: RespawnPacket): Boolean

    fun handleContainerOpen(packet: ContainerOpenPacket): Boolean

    fun handleContainerClose(packet: ContainerClosePacket): Boolean

    fun handlePlayerHotbar(packet: PlayerHotbarPacket): Boolean

    fun handleInventoryContent(packet: InventoryContentPacket): Boolean

    fun handleInventorySlot(packet: InventorySlotPacket): Boolean

    fun handleContainerSetData(packet: ContainerSetDataPacket): Boolean

    fun handleCraftingData(packet: CraftingDataPacket): Boolean

    fun handleCraftingEvent(packet: CraftingEventPacket): Boolean

    fun handleGuiDataPickItem(packet: GuiDataPickItemPacket): Boolean

    fun handleAdventureSettings(packet: AdventureSettingsPacket): Boolean

    fun handleBlockActorData(packet: BlockActorDataPacket): Boolean

    fun handlePlayerInput(packet: PlayerInputPacket): Boolean

    fun handleLevelChunk(packet: LevelChunkPacket): Boolean

    fun handleSetCommandsEnabled(packet: SetCommandsEnabledPacket): Boolean

    fun handleSetDifficulty(packet: SetDifficultyPacket): Boolean

    fun handleChangeDimension(packet: ChangeDimensionPacket): Boolean

    fun handleSetPlayerGameType(packet: SetPlayerGameTypePacket): Boolean

    fun handlePlayerList(packet: PlayerListPacket): Boolean

    fun handleSimpleEvent(packet: SimpleEventPacket): Boolean

    fun handleEvent(packet: EventPacket): Boolean

    fun handleSpawnExperienceOrb(packet: SpawnExperienceOrbPacket): Boolean

    fun handleClientboundMapItemData(packet: ClientboundMapItemDataPacket): Boolean

    fun handleMapInfoRequest(packet: MapInfoRequestPacket): Boolean

    fun handleRequestChunkRadius(packet: RequestChunkRadiusPacket): Boolean

    fun handleChunkRadiusUpdated(packet: ChunkRadiusUpdatedPacket): Boolean

    fun handleItemFrameDropItem(packet: ItemFrameDropItemPacket): Boolean

    fun handleGameRulesChanged(packet: GameRulesChangedPacket): Boolean

    fun handleCamera(packet: CameraPacket): Boolean

    fun handleBossEvent(packet: BossEventPacket): Boolean

    fun handleShowCredits(packet: ShowCreditsPacket): Boolean

    fun handleAvailableCommands(packet: AvailableCommandsPacket): Boolean

    fun handleCommandRequest(packet: CommandRequestPacket): Boolean

    fun handleCommandBlockUpdate(packet: CommandBlockUpdatePacket): Boolean

    fun handleCommandOutput(packet: CommandOutputPacket): Boolean

    fun handleUpdateTrade(packet: UpdateTradePacket): Boolean

    fun handleUpdateEquip(packet: UpdateEquipPacket): Boolean

    fun handleResourcePackDataInfo(packet: ResourcePackDataInfoPacket): Boolean

    fun handleResourcePackChunkData(packet: ResourcePackChunkDataPacket): Boolean

    fun handleResourcePackChunkRequest(packet: ResourcePackChunkRequestPacket): Boolean

    fun handleTransfer(packet: TransferPacket): Boolean

    fun handlePlaySound(packet: PlaySoundPacket): Boolean

    fun handleStopSound(packet: StopSoundPacket): Boolean

    fun handleSetTitle(packet: SetTitlePacket): Boolean

    fun handleAddBehaviorTree(packet: AddBehaviorTreePacket): Boolean

    fun handleStructureBlockUpdate(packet: StructureBlockUpdatePacket): Boolean

    fun handleShowStoreOffer(packet: ShowStoreOfferPacket): Boolean

    fun handlePurchaseReceipt(packet: PurchaseReceiptPacket): Boolean

    fun handlePlayerSkin(packet: PlayerSkinPacket): Boolean

    fun handleSubClientLogin(packet: SubClientLoginPacket): Boolean

    fun handleAutomationClientConnect(packet: AutomationClientConnectPacket): Boolean

    fun handleSetLastHurtBy(packet: SetLastHurtByPacket): Boolean

    fun handleBookEdit(packet: BookEditPacket): Boolean

    fun handleNpcRequest(packet: NpcRequestPacket): Boolean

    fun handlePhotoTransfer(packet: PhotoTransferPacket): Boolean

    fun handleModalFormRequest(packet: ModalFormRequestPacket): Boolean

    fun handleModalFormResponse(packet: ModalFormResponsePacket): Boolean

    fun handleServerSettingsRequest(packet: ServerSettingsRequestPacket): Boolean

    fun handleServerSettingsResponse(packet: ServerSettingsResponsePacket): Boolean

    fun handleShowProfile(packet: ShowProfilePacket): Boolean

    fun handleSetDefaultGameType(packet: SetDefaultGameTypePacket): Boolean

    fun handleRemoveObjective(packet: RemoveObjectivePacket): Boolean

    fun handleSetDisplayObjective(packet: SetDisplayObjectivePacket): Boolean

    fun handleSetScore(packet: SetScorePacket): Boolean

    fun handleLabTable(packet: LabTablePacket): Boolean

    fun handleUpdateBlockSynced(packet: UpdateBlockSyncedPacket): Boolean

    fun handleMoveActorDelta(packet: MoveActorDeltaPacket): Boolean

    fun handleSetScoreboardIdentity(packet: SetScoreboardIdentityPacket): Boolean

    fun handleSetLocalPlayerAsInitialized(packet: SetLocalPlayerAsInitializedPacket): Boolean

    fun handleUpdateSoftEnum(packet: UpdateSoftEnumPacket): Boolean

    fun handleNetworkStackLatency(packet: NetworkStackLatencyPacket): Boolean

    fun handleScriptCustomEvent(packet: ScriptCustomEventPacket): Boolean

    fun handleSpawnParticleEffect(packet: SpawnParticleEffectPacket): Boolean

    fun handleAvailableActorIdentifiers(packet: AvailableActorIdentifiersPacket): Boolean

    fun handleLevelSoundEventPacketV2(packet: LevelSoundEventPacketV2): Boolean

    fun handleNetworkChunkPublisherUpdate(packet: NetworkChunkPublisherUpdatePacket): Boolean

    fun handleBiomeDefinitionList(packet: BiomeDefinitionListPacket): Boolean

    fun handleLevelSoundEvent(packet: LevelSoundEventPacket): Boolean

    fun handleLevelEventGeneric(packet: LevelEventGenericPacket): Boolean

    fun handleLecternUpdate(packet: LecternUpdatePacket): Boolean

    fun handleAddEntity(packet: AddEntityPacket): Boolean

    fun handleRemoveEntity(packet: RemoveEntityPacket): Boolean

    fun handleClientCacheStatus(packet: ClientCacheStatusPacket): Boolean

    fun handleOnScreenTextureAnimation(packet: OnScreenTextureAnimationPacket): Boolean

    fun handleMapCreateLockedCopy(packet: MapCreateLockedCopyPacket): Boolean

    fun handleStructureTemplateDataRequest(packet: StructureTemplateDataRequestPacket): Boolean

    fun handleStructureTemplateDataResponse(packet: StructureTemplateDataResponsePacket): Boolean

    fun handleClientCacheBlobStatus(packet: ClientCacheBlobStatusPacket): Boolean

    fun handleClientCacheMissResponse(packet: ClientCacheMissResponsePacket): Boolean

    fun handleEducationSettings(packet: EducationSettingsPacket): Boolean

    fun handleEmote(packet: EmotePacket): Boolean

    fun handleMultiplayerSettings(packet: MultiplayerSettingsPacket): Boolean

    fun handleSettingsCommand(packet: SettingsCommandPacket): Boolean

    fun handleAnvilDamage(packet: AnvilDamagePacket): Boolean

    fun handleCompletedUsingItem(packet: CompletedUsingItemPacket): Boolean

    fun handleNetworkSettings(packet: NetworkSettingsPacket): Boolean

    fun handlePlayerAuthInput(packet: PlayerAuthInputPacket): Boolean

    fun handleCreativeContent(packet: CreativeContentPacket): Boolean

    fun handlePlayerEnchantOptions(packet: PlayerEnchantOptionsPacket): Boolean

    fun handleItemStackRequest(packet: ItemStackRequestPacket): Boolean

    fun handleItemStackResponse(packet: ItemStackResponsePacket): Boolean

    fun handlePlayerArmorDamage(packet: PlayerArmorDamagePacket): Boolean

    fun handleCodeBuilder(packet: CodeBuilderPacket): Boolean

    fun handleUpdatePlayerGameType(packet: UpdatePlayerGameTypePacket): Boolean

    fun handleEmoteList(packet: EmoteListPacket): Boolean

    fun handlePositionTrackingDBServerBroadcast(packet: PositionTrackingDBServerBroadcastPacket): Boolean

    fun handlePositionTrackingDBClientRequest(packet: PositionTrackingDBClientRequestPacket): Boolean

    fun handleDebugInfo(packet: DebugInfoPacket): Boolean

    fun handlePacketViolationWarning(packet: PacketViolationWarningPacket): Boolean

    fun handleMotionPredictionHints(packet: MotionPredictionHintsPacket): Boolean

    fun handleAnimateEntity(packet: AnimateEntityPacket): Boolean

    fun handleCameraShake(packet: CameraShakePacket): Boolean

    fun handlePlayerFog(packet: PlayerFogPacket): Boolean

    fun handleCorrectPlayerMovePrediction(packet: CorrectPlayerMovePredictionPacket): Boolean

    fun handleItemComponent(packet: ItemComponentPacket): Boolean

    fun handleFilterText(packet: FilterTextPacket): Boolean

    fun handleClientboundDebugRenderer(packet: ClientboundDebugRendererPacket): Boolean
}
