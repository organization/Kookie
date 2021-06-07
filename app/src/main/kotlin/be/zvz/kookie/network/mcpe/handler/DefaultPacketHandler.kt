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
}
