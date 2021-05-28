package be.zvz.kookie.network.mcpe.protocol

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





/**
 * This class is an automatically generated stub. Do not edit it manually.
 */
Interface PacketHandlerInterface{
	 override fun handleLogin(packet: LoginPacket) : Boolean

	 override fun handlePlayStatus(packet: PlayStatusPacket) : Boolean

	 override fun handleServerToClientHandshake(packet: ServerToClientHandshakePacket) : Boolean

	 override fun handleClientToServerHandshake(packet: ClientToServerHandshakePacket) : Boolean

	 override fun handleDisconnect(packet: DisconnectPacket) : Boolean

	 override fun handleResourcePacksInfo(packet: ResourcePacksInfoPacket) : Boolean

	 override fun handleResourcePackStack(packet: ResourcePackStackPacket) : Boolean

	 override fun handleResourcePackClientResponse(packet: ResourcePackClientResponsePacket) : Boolean

	 override fun handleText(packet: TextPacket) : Boolean

	 override fun handleSetTime(packet: SetTimePacket) : Boolean

	 override fun handleStartGame(packet: StartGamePacket) : Boolean

	 override fun handleAddPlayer(packet: AddPlayerPacket) : Boolean

	 override fun handleAddActor(packet: AddActorPacket) : Boolean

	 override fun handleRemoveActor(packet: RemoveActorPacket) : Boolean

	 override fun handleAddItemActor(packet: AddItemActorPacket) : Boolean

	 override fun handleTakeItemActor(packet: TakeItemActorPacket) : Boolean

	 override fun handleMoveActorAbsolute(packet: MoveActorAbsolutePacket) : Boolean

	 override fun handleMovePlayer(packet: MovePlayerPacket) : Boolean

	 override fun handleRiderJump(packet: RiderJumpPacket) : Boolean

	 override fun handleUpdateBlock(packet: UpdateBlockPacket) : Boolean

	 override fun handleAddPaInting(packet: AddPaIntingPacket) : Boolean

	 override fun handleTickSync(packet: TickSyncPacket) : Boolean

	 override fun handleLevelSoundEventPacketV1(packet: LevelSoundEventPacketV1) : Boolean

	 override fun handleLevelEvent(packet: LevelEventPacket) : Boolean

	 override fun handleBlockEvent(packet: BlockEventPacket) : Boolean

	 override fun handleActorEvent(packet: ActorEventPacket) : Boolean

	 override fun handleMobEffect(packet: MobEffectPacket) : Boolean

	 override fun handleUpdateAttributes(packet: UpdateAttributesPacket) : Boolean

	 override fun handleInventoryTransaction(packet: InventoryTransactionPacket) : Boolean

	 override fun handleMobEquipment(packet: MobEquipmentPacket) : Boolean

	 override fun handleMobArmorEquipment(packet: MobArmorEquipmentPacket) : Boolean

	 override fun handleInteract(packet: InteractPacket) : Boolean

	 override fun handleBlockPickRequest(packet: BlockPickRequestPacket) : Boolean

	 override fun handleActorPickRequest(packet: ActorPickRequestPacket) : Boolean

	 override fun handlePlayerAction(packet: PlayerActionPacket) : Boolean

	 override fun handleHurtArmor(packet: HurtArmorPacket) : Boolean

	 override fun handleSetActorData(packet: SetActorDataPacket) : Boolean

	 override fun handleSetActorMotion(packet: SetActorMotionPacket) : Boolean

	 override fun handleSetActorLink(packet: SetActorLinkPacket) : Boolean

	 override fun handleSetHealth(packet: SetHealthPacket) : Boolean

	 override fun handleSetSpawnPosition(packet: SetSpawnPositionPacket) : Boolean

	 override fun handleAnimate(packet: AnimatePacket) : Boolean

	 override fun handleRespawn(packet: RespawnPacket) : Boolean

	 override fun handleContainerOpen(packet: ContainerOpenPacket) : Boolean

	 override fun handleContainerClose(packet: ContainerClosePacket) : Boolean

	 override fun handlePlayerHotbar(packet: PlayerHotbarPacket) : Boolean

	 override fun handleInventoryContent(packet: InventoryContentPacket) : Boolean

	 override fun handleInventorySlot(packet: InventorySlotPacket) : Boolean

	 override fun handleContainerSetData(packet: ContainerSetDataPacket) : Boolean

	 override fun handleCraftingData(packet: CraftingDataPacket) : Boolean

	 override fun handleCraftingEvent(packet: CraftingEventPacket) : Boolean

	 override fun handleGuiDataPickItem(packet: GuiDataPickItemPacket) : Boolean

	 override fun handleAdventureSettings(packet: AdventureSettingsPacket) : Boolean

	 override fun handleBlockActorData(packet: BlockActorDataPacket) : Boolean

	 override fun handlePlayerInput(packet: PlayerInputPacket) : Boolean

	 override fun handleLevelChunk(packet: LevelChunkPacket) : Boolean

	 override fun handleSetCommandsEnabled(packet: SetCommandsEnabledPacket) : Boolean

	 override fun handleSetDifficulty(packet: SetDifficultyPacket) : Boolean

	 override fun handleChangeDimension(packet: ChangeDimensionPacket) : Boolean

	 override fun handleSetPlayerGameType(packet: SetPlayerGameTypePacket) : Boolean

	 override fun handlePlayerList(packet: PlayerListPacket) : Boolean

	 override fun handleSimpleEvent(packet: SimpleEventPacket) : Boolean

	 override fun handleEvent(packet: EventPacket) : Boolean

	 override fun handleSpawnExperienceOrb(packet: SpawnExperienceOrbPacket) : Boolean

	 override fun handleClientboundMapItemData(packet: ClientboundMapItemDataPacket) : Boolean

	 override fun handleMapInfoRequest(packet: MapInfoRequestPacket) : Boolean

	 override fun handleRequestChunkRadius(packet: RequestChunkRadiusPacket) : Boolean

	 override fun handleChunkRadiusUpdated(packet: ChunkRadiusUpdatedPacket) : Boolean

	 override fun handleItemFrameDropItem(packet: ItemFrameDropItemPacket) : Boolean

	 override fun handleGameRulesChanged(packet: GameRulesChangedPacket) : Boolean

	 override fun handleCamera(packet: CameraPacket) : Boolean

	 override fun handleBossEvent(packet: BossEventPacket) : Boolean

	 override fun handleShowCredits(packet: ShowCreditsPacket) : Boolean

	 override fun handleAvailableCommands(packet: AvailableCommandsPacket) : Boolean

	 override fun handleCommandRequest(packet: CommandRequestPacket) : Boolean

	 override fun handleCommandBlockUpdate(packet: CommandBlockUpdatePacket) : Boolean

	 override fun handleCommandOutput(packet: CommandOutputPacket) : Boolean

	 override fun handleUpdateTrade(packet: UpdateTradePacket) : Boolean

	 override fun handleUpdateEquip(packet: UpdateEquipPacket) : Boolean

	 override fun handleResourcePackDataInfo(packet: ResourcePackDataInfoPacket) : Boolean

	 override fun handleResourcePackChunkData(packet: ResourcePackChunkDataPacket) : Boolean

	 override fun handleResourcePackChunkRequest(packet: ResourcePackChunkRequestPacket) : Boolean

	 override fun handleTransfer(packet: TransferPacket) : Boolean

	 override fun handlePlaySound(packet: PlaySoundPacket) : Boolean

	 override fun handleStopSound(packet: StopSoundPacket) : Boolean

	 override fun handleSetTitle(packet: SetTitlePacket) : Boolean

	 override fun handleAddBehaviorTree(packet: AddBehaviorTreePacket) : Boolean

	 override fun handleStructureBlockUpdate(packet: StructureBlockUpdatePacket) : Boolean

	 override fun handleShowStoreOffer(packet: ShowStoreOfferPacket) : Boolean

	 override fun handlePurchaseReceipt(packet: PurchaseReceiptPacket) : Boolean

	 override fun handlePlayerSkin(packet: PlayerSkinPacket) : Boolean

	 override fun handleSubClientLogin(packet: SubClientLoginPacket) : Boolean

	 override fun handleAutomationClientConnect(packet: AutomationClientConnectPacket) : Boolean

	 override fun handleSetLastHurtBy(packet: SetLastHurtByPacket) : Boolean

	 override fun handleBookEdit(packet: BookEditPacket) : Boolean

	 override fun handleNpcRequest(packet: NpcRequestPacket) : Boolean

	 override fun handlePhotoTransfer(packet: PhotoTransferPacket) : Boolean

	 override fun handleModalFormRequest(packet: ModalFormRequestPacket) : Boolean

	 override fun handleModalFormResponse(packet: ModalFormResponsePacket) : Boolean

	 override fun handleServerSettingsRequest(packet: ServerSettingsRequestPacket) : Boolean

	 override fun handleServerSettingsResponse(packet: ServerSettingsResponsePacket) : Boolean

	 override fun handleShowProfile(packet: ShowProfilePacket) : Boolean

	 override fun handleSetDefaultGameType(packet: SetDefaultGameTypePacket) : Boolean

	 override fun handleRemoveObjective(packet: RemoveObjectivePacket) : Boolean

	 override fun handleSetDisplayObjective(packet: SetDisplayObjectivePacket) : Boolean

	 override fun handleSetScore(packet: SetScorePacket) : Boolean

	 override fun handleLabTable(packet: LabTablePacket) : Boolean

	 override fun handleUpdateBlockSynced(packet: UpdateBlockSyncedPacket) : Boolean

	 override fun handleMoveActorDelta(packet: MoveActorDeltaPacket) : Boolean

	 override fun handleSetScoreboardIdentity(packet: SetScoreboardIdentityPacket) : Boolean

	 override fun handleSetLocalPlayerAsInitialized(packet: SetLocalPlayerAsInitializedPacket) : Boolean

	 override fun handleUpdateSoftEnum(packet: UpdateSoftEnumPacket) : Boolean

	 override fun handleNetworkStackLatency(packet: NetworkStackLatencyPacket) : Boolean

	 override fun handleScriptCustomEvent(packet: ScriptCustomEventPacket) : Boolean

	 override fun handleSpawnParticleEffect(packet: SpawnParticleEffectPacket) : Boolean

	 override fun handleAvailableActorIdentifiers(packet: AvailableActorIdentifiersPacket) : Boolean

	 override fun handleLevelSoundEventPacketV2(packet: LevelSoundEventPacketV2) : Boolean

	 override fun handleNetworkChunkPublisherUpdate(packet: NetworkChunkPublisherUpdatePacket) : Boolean

	 override fun handleBiomeDefinitionList(packet: BiomeDefinitionListPacket) : Boolean

	 override fun handleLevelSoundEvent(packet: LevelSoundEventPacket) : Boolean

	 override fun handleLevelEventGeneric(packet: LevelEventGenericPacket) : Boolean

	 override fun handleLecternUpdate(packet: LecternUpdatePacket) : Boolean

	 override fun handleAddEntity(packet: AddEntityPacket) : Boolean

	 override fun handleRemoveEntity(packet: RemoveEntityPacket) : Boolean

	 override fun handleClientCacheStatus(packet: ClientCacheStatusPacket) : Boolean

	 override fun handleOnScreenTextureAnimation(packet: OnScreenTextureAnimationPacket) : Boolean

	 override fun handleMapCreateLockedCopy(packet: MapCreateLockedCopyPacket) : Boolean

	 override fun handleStructureTemplateDataRequest(packet: StructureTemplateDataRequestPacket) : Boolean

	 override fun handleStructureTemplateDataResponse(packet: StructureTemplateDataResponsePacket) : Boolean

	 override fun handleClientCacheBlobStatus(packet: ClientCacheBlobStatusPacket) : Boolean

	 override fun handleClientCacheMissResponse(packet: ClientCacheMissResponsePacket) : Boolean

	 override fun handleEducationSettings(packet: EducationSettingsPacket) : Boolean

	 override fun handleEmote(packet: EmotePacket) : Boolean

	 override fun handleMultiplayerSettings(packet: MultiplayerSettingsPacket) : Boolean

	 override fun handleSettingsCommand(packet: SettingsCommandPacket) : Boolean

	 override fun handleAnvilDamage(packet: AnvilDamagePacket) : Boolean

	 override fun handleCompletedUsingItem(packet: CompletedUsingItemPacket) : Boolean

	 override fun handleNetworkSettings(packet: NetworkSettingsPacket) : Boolean

	 override fun handlePlayerAuthInput(packet: PlayerAuthInputPacket) : Boolean

	 override fun handleCreativeContent(packet: CreativeContentPacket) : Boolean

	 override fun handlePlayerEnchantOptions(packet: PlayerEnchantOptionsPacket) : Boolean

	 override fun handleItemStackRequest(packet: ItemStackRequestPacket) : Boolean

	 override fun handleItemStackResponse(packet: ItemStackResponsePacket) : Boolean

	 override fun handlePlayerArmorDamage(packet: PlayerArmorDamagePacket) : Boolean

	 override fun handleCodeBuilder(packet: CodeBuilderPacket) : Boolean

	 override fun handleUpdatePlayerGameType(packet: UpdatePlayerGameTypePacket) : Boolean

	 override fun handleEmoteList(packet: EmoteListPacket) : Boolean

	 override fun handlePositionTrackingDBServerBroadcast(packet: PositionTrackingDBServerBroadcastPacket) : Boolean

	 override fun handlePositionTrackingDBClientRequest(packet: PositionTrackingDBClientRequestPacket) : Boolean

	 override fun handleDebugInfo(packet: DebugInfoPacket) : Boolean

	 override fun handlePacketViolationWarning(packet: PacketViolationWarningPacket) : Boolean

	 override fun handleMotionPredictionHInts(packet: MotionPredictionHIntsPacket) : Boolean

	 override fun handleAnimateEntity(packet: AnimateEntityPacket) : Boolean

	 override fun handleCameraShake(packet: CameraShakePacket) : Boolean

	 override fun handlePlayerFog(packet: PlayerFogPacket) : Boolean

	 override fun handleCorrectPlayerMovePrediction(packet: CorrectPlayerMovePredictionPacket) : Boolean

	 override fun handleItemComponent(packet: ItemComponentPacket) : Boolean

	 override fun handleFilterText(packet: FilterTextPacket) : Boolean

	 override fun handleClientboundDebugRenderer(packet: ClientboundDebugRendererPacket) : Boolean
}
