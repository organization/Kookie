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

open class PacketHandler : PacketHandlerInterface {

    open fun setUp() {
    }

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

    override fun handleSetActorLink(packet: SetActorLinkPacket): Boolean {
        return false
    }

    override fun handleSetHealth(packet: SetHealthPacket): Boolean {
        return false
    }

    override fun handleSetSpawnPosition(packet: SetSpawnPositionPacket): Boolean {
        return false
    }

    override fun handleAnimate(packet: AnimatePacket): Boolean {
        return false
    }

    override fun handleRespawn(packet: RespawnPacket): Boolean {
        return false
    }

    override fun handleContainerOpen(packet: ContainerOpenPacket): Boolean {
        return false
    }

    override fun handleContainerClose(packet: ContainerClosePacket): Boolean {
        return false
    }

    override fun handlePlayerHotbar(packet: PlayerHotbarPacket): Boolean {
        return false
    }

    override fun handleInventoryContent(packet: InventoryContentPacket): Boolean {
        return false
    }

    override fun handleInventorySlot(packet: InventorySlotPacket): Boolean {
        return false
    }

    override fun handleContainerSetData(packet: ContainerSetDataPacket): Boolean {
        return false
    }

    override fun handleCraftingData(packet: CraftingDataPacket): Boolean {
        return false
    }

    override fun handleCraftingEvent(packet: CraftingEventPacket): Boolean {
        return false
    }

    override fun handleGuiDataPickItem(packet: GuiDataPickItemPacket): Boolean {
        return false
    }

    override fun handleAdventureSettings(packet: AdventureSettingsPacket): Boolean {
        return false
    }

    override fun handleBlockActorData(packet: BlockActorDataPacket): Boolean {
        return false
    }

    override fun handlePlayerInput(packet: PlayerInputPacket): Boolean {
        return false
    }

    override fun handleLevelChunk(packet: LevelChunkPacket): Boolean {
        return false
    }

    override fun handleSetCommandsEnabled(packet: SetCommandsEnabledPacket): Boolean {
        return false
    }

    override fun handleSetDifficulty(packet: SetDifficultyPacket): Boolean {
        return false
    }

    override fun handleChangeDimension(packet: ChangeDimensionPacket): Boolean {
        return false
    }

    override fun handleSetPlayerGameType(packet: SetPlayerGameTypePacket): Boolean {
        return false
    }

    override fun handlePlayerList(packet: PlayerListPacket): Boolean {
        return false
    }

    override fun handleSimpleEvent(packet: SimpleEventPacket): Boolean {
        return false
    }

    override fun handleEvent(packet: EventPacket): Boolean {
        return false
    }

    override fun handleSpawnExperienceOrb(packet: SpawnExperienceOrbPacket): Boolean {
        return false
    }

    override fun handleClientboundMapItemData(packet: ClientboundMapItemDataPacket): Boolean {
        return false
    }

    override fun handleMapInfoRequest(packet: MapInfoRequestPacket): Boolean {
        return false
    }

    override fun handleRequestChunkRadius(packet: RequestChunkRadiusPacket): Boolean {
        return false
    }

    override fun handleChunkRadiusUpdated(packet: ChunkRadiusUpdatedPacket): Boolean {
        return false
    }

    override fun handleItemFrameDropItem(packet: ItemFrameDropItemPacket): Boolean {
        return false
    }

    override fun handleGameRulesChanged(packet: GameRulesChangedPacket): Boolean {
        return false
    }

    override fun handleCamera(packet: CameraPacket): Boolean {
        return false
    }

    override fun handleBossEvent(packet: BossEventPacket): Boolean {
        return false
    }

    override fun handleShowCredits(packet: ShowCreditsPacket): Boolean {
        return false
    }

    override fun handleAvailableCommands(packet: AvailableCommandsPacket): Boolean {
        return false
    }

    override fun handleCommandRequest(packet: CommandRequestPacket): Boolean {
        return false
    }

    override fun handleCommandBlockUpdate(packet: CommandBlockUpdatePacket): Boolean {
        return false
    }

    override fun handleCommandOutput(packet: CommandOutputPacket): Boolean {
        return false
    }

    override fun handleUpdateTrade(packet: UpdateTradePacket): Boolean {
        return false
    }

    override fun handleUpdateEquip(packet: UpdateEquipPacket): Boolean {
        return false
    }

    override fun handleResourcePackDataInfo(packet: ResourcePackDataInfoPacket): Boolean {
        return false
    }

    override fun handleResourcePackChunkData(packet: ResourcePackChunkDataPacket): Boolean {
        return false
    }

    override fun handleResourcePackChunkRequest(packet: ResourcePackChunkRequestPacket): Boolean {
        return false
    }

    override fun handleTransfer(packet: TransferPacket): Boolean {
        return false
    }

    override fun handlePlaySound(packet: PlaySoundPacket): Boolean {
        return false
    }

    override fun handleStopSound(packet: StopSoundPacket): Boolean {
        return false
    }

    override fun handleSetTitle(packet: SetTitlePacket): Boolean {
        return false
    }

    override fun handleAddBehaviorTree(packet: AddBehaviorTreePacket): Boolean {
        return false
    }

    override fun handleStructureBlockUpdate(packet: StructureBlockUpdatePacket): Boolean {
        return false
    }

    override fun handleShowStoreOffer(packet: ShowStoreOfferPacket): Boolean {
        return false
    }

    override fun handlePurchaseReceipt(packet: PurchaseReceiptPacket): Boolean {
        return false
    }

    override fun handlePlayerSkin(packet: PlayerSkinPacket): Boolean {
        return false
    }

    override fun handleSubClientLogin(packet: SubClientLoginPacket): Boolean {
        return false
    }

    override fun handleAutomationClientConnect(packet: AutomationClientConnectPacket): Boolean {
        return false
    }

    override fun handleSetLastHurtBy(packet: SetLastHurtByPacket): Boolean {
        return false
    }

    override fun handleBookEdit(packet: BookEditPacket): Boolean {
        return false
    }

    override fun handleNpcRequest(packet: NpcRequestPacket): Boolean {
        return false
    }

    override fun handlePhotoTransfer(packet: PhotoTransferPacket): Boolean {
        return false
    }

    override fun handleModalFormRequest(packet: ModalFormRequestPacket): Boolean {
        return false
    }

    override fun handleModalFormResponse(packet: ModalFormResponsePacket): Boolean {
        return false
    }

    override fun handleServerSettingsRequest(packet: ServerSettingsRequestPacket): Boolean {
        return false
    }

    override fun handleServerSettingsResponse(packet: ServerSettingsResponsePacket): Boolean {
        return false
    }

    override fun handleShowProfile(packet: ShowProfilePacket): Boolean {
        return false
    }

    override fun handleSetDefaultGameType(packet: SetDefaultGameTypePacket): Boolean {
        return false
    }

    override fun handleRemoveObjective(packet: RemoveObjectivePacket): Boolean {
        return false
    }

    override fun handleSetDisplayObjective(packet: SetDisplayObjectivePacket): Boolean {
        return false
    }

    override fun handleSetScore(packet: SetScorePacket): Boolean {
        return false
    }

    override fun handleLabTable(packet: LabTablePacket): Boolean {
        return false
    }

    override fun handleUpdateBlockSynced(packet: UpdateBlockSyncedPacket): Boolean {
        return false
    }

    override fun handleMoveActorDelta(packet: MoveActorDeltaPacket): Boolean {
        return false
    }

    override fun handleSetScoreboardIdentity(packet: SetScoreboardIdentityPacket): Boolean {
        return false
    }

    override fun handleSetLocalPlayerAsInitialized(packet: SetLocalPlayerAsInitializedPacket): Boolean {
        return false
    }

    override fun handleUpdateSoftEnum(packet: UpdateSoftEnumPacket): Boolean {
        return false
    }

    override fun handleNetworkStackLatency(packet: NetworkStackLatencyPacket): Boolean {
        return false
    }

    override fun handleScriptCustomEvent(packet: ScriptCustomEventPacket): Boolean {
        return false
    }

    override fun handleSpawnParticleEffect(packet: SpawnParticleEffectPacket): Boolean {
        return false
    }

    override fun handleAvailableActorIdentifiers(packet: AvailableActorIdentifiersPacket): Boolean {
        return false
    }

    override fun handleLevelSoundEventPacketV2(packet: LevelSoundEventPacketV2): Boolean {
        return false
    }

    override fun handleNetworkChunkPublisherUpdate(packet: NetworkChunkPublisherUpdatePacket): Boolean {
        return false
    }

    override fun handleBiomeDefinitionList(packet: BiomeDefinitionListPacket): Boolean {
        return false
    }

    override fun handleLevelSoundEvent(packet: LevelSoundEventPacket): Boolean {
        return false
    }

    override fun handleLevelEventGeneric(packet: LevelEventGenericPacket): Boolean {
        return false
    }

    override fun handleLecternUpdate(packet: LecternUpdatePacket): Boolean {
        return false
    }

    override fun handleAddEntity(packet: AddEntityPacket): Boolean {
        return false
    }

    override fun handleRemoveEntity(packet: RemoveEntityPacket): Boolean {
        return false
    }

    override fun handleClientCacheStatus(packet: ClientCacheStatusPacket): Boolean {
        return false
    }

    override fun handleOnScreenTextureAnimation(packet: OnScreenTextureAnimationPacket): Boolean {
        return false
    }

    override fun handleMapCreateLockedCopy(packet: MapCreateLockedCopyPacket): Boolean {
        return false
    }

    override fun handleStructureTemplateDataRequest(packet: StructureTemplateDataRequestPacket): Boolean {
        return false
    }

    override fun handleStructureTemplateDataResponse(packet: StructureTemplateDataResponsePacket): Boolean {
        return false
    }

    override fun handleClientCacheBlobStatus(packet: ClientCacheBlobStatusPacket): Boolean {
        return false
    }

    override fun handleClientCacheMissResponse(packet: ClientCacheMissResponsePacket): Boolean {
        return false
    }

    override fun handleEducationSettings(packet: EducationSettingsPacket): Boolean {
        return false
    }

    override fun handleEmote(packet: EmotePacket): Boolean {
        return false
    }

    override fun handleMultiplayerSettings(packet: MultiplayerSettingsPacket): Boolean {
        return false
    }

    override fun handleSettingsCommand(packet: SettingsCommandPacket): Boolean {
        return false
    }

    override fun handleAnvilDamage(packet: AnvilDamagePacket): Boolean {
        return false
    }

    override fun handleCompletedUsingItem(packet: CompletedUsingItemPacket): Boolean {
        return false
    }

    override fun handleNetworkSettings(packet: NetworkSettingsPacket): Boolean {
        return false
    }

    override fun handlePlayerAuthInput(packet: PlayerAuthInputPacket): Boolean {
        return false
    }

    override fun handleCreativeContent(packet: CreativeContentPacket): Boolean {
        return false
    }

    override fun handlePlayerEnchantOptions(packet: PlayerEnchantOptionsPacket): Boolean {
        return false
    }

    override fun handleItemStackRequest(packet: ItemStackRequestPacket): Boolean {
        return false
    }

    override fun handleItemStackResponse(packet: ItemStackResponsePacket): Boolean {
        return false
    }

    override fun handlePlayerArmorDamage(packet: PlayerArmorDamagePacket): Boolean {
        return false
    }

    override fun handleCodeBuilder(packet: CodeBuilderPacket): Boolean {
        return false
    }

    override fun handleUpdatePlayerGameType(packet: UpdatePlayerGameTypePacket): Boolean {
        return false
    }

    override fun handleEmoteList(packet: EmoteListPacket): Boolean {
        return false
    }

    override fun handlePositionTrackingDBServerBroadcast(packet: PositionTrackingDBServerBroadcastPacket): Boolean {
        return false
    }

    override fun handlePositionTrackingDBClientRequest(packet: PositionTrackingDBClientRequestPacket): Boolean {
        return false
    }

    override fun handleDebugInfo(packet: DebugInfoPacket): Boolean {
        return false
    }

    override fun handlePacketViolationWarning(packet: PacketViolationWarningPacket): Boolean {
        return false
    }

    override fun handleMotionPredictionHints(packet: MotionPredictionHintsPacket): Boolean {
        return false
    }

    override fun handleAnimateEntity(packet: AnimateEntityPacket): Boolean {
        return false
    }

    override fun handleCameraShake(packet: CameraShakePacket): Boolean {
        return false
    }

    override fun handlePlayerFog(packet: PlayerFogPacket): Boolean {
        return false
    }

    override fun handleCorrectPlayerMovePrediction(packet: CorrectPlayerMovePredictionPacket): Boolean {
        return false
    }

    override fun handleItemComponent(packet: ItemComponentPacket): Boolean {
        return false
    }

    override fun handleFilterText(packet: FilterTextPacket): Boolean {
        return false
    }

    override fun handleClientboundDebugRenderer(packet: ClientboundDebugRendererPacket): Boolean {
        return false
    }

    override fun handleSyncActorProperty(packet: SyncActorPropertyPacket): Boolean {
        return false
    }

    override fun handleAddVolumeEntity(packet: AddVolumeEntityPacket): Boolean {
        return false
    }

    override fun handleRemoveVolumeEntity(packet: RemoveVolumeEntityPacket): Boolean {
        return false
    }

    override fun handleSimulationType(packet: SimulationTypePacket): Boolean {
        return false
    }

    override fun handleNpcDialogue(packet: NpcDialoguePacket): Boolean {
        return false
    }
}
