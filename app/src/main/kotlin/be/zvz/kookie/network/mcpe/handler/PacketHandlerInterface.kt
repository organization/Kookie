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
