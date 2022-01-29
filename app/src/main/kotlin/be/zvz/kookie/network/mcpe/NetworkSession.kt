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
package be.zvz.kookie.network.mcpe

import be.zvz.kookie.Server
import be.zvz.kookie.entity.Attribute
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living
import be.zvz.kookie.entity.effect.EffectInstance
import be.zvz.kookie.event.player.PlayerDuplicateLoginEvent
import be.zvz.kookie.form.Form
import be.zvz.kookie.lang.KnownTranslationKeys
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.network.mcpe.convert.KookieToNukkitProtocolConverter
import be.zvz.kookie.network.mcpe.handler.LoginPacketHandler
import be.zvz.kookie.network.mcpe.handler.PreSpawnPacketHandler
import be.zvz.kookie.network.mcpe.handler.SpawnResponsePacketHandler
import be.zvz.kookie.network.mcpe.protocol.types.DimensionIds
import be.zvz.kookie.network.mcpe.protocol.types.entity.ByteMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.CompoundMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataTypes
import be.zvz.kookie.network.mcpe.protocol.types.entity.FloatMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.IntMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.LongMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.MetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.ShortMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.StringMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.Vec3MetadataProperty
import be.zvz.kookie.permission.DefaultPermissions
import be.zvz.kookie.player.GameMode
import be.zvz.kookie.player.Player
import be.zvz.kookie.player.PlayerInfo
import be.zvz.kookie.player.XboxLivePlayerInfo
import be.zvz.kookie.utils.TextFormat
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.nukkitx.math.vector.Vector3f
import com.nukkitx.math.vector.Vector3i
import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.BedrockServerSession
import com.nukkitx.protocol.bedrock.data.AttributeData
import com.nukkitx.protocol.bedrock.data.PlayerPermission
import com.nukkitx.protocol.bedrock.data.command.CommandPermission
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler
import com.nukkitx.protocol.bedrock.packet.AdventureSettingsPacket
import com.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket
import com.nukkitx.protocol.bedrock.packet.ChunkRadiusUpdatedPacket
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket
import com.nukkitx.protocol.bedrock.packet.MobEffectPacket
import com.nukkitx.protocol.bedrock.packet.ModalFormRequestPacket
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket
import com.nukkitx.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket
import com.nukkitx.protocol.bedrock.packet.RemoveEntityPacket
import com.nukkitx.protocol.bedrock.packet.SetDifficultyPacket
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket
import com.nukkitx.protocol.bedrock.packet.SetPlayerGameTypePacket
import com.nukkitx.protocol.bedrock.packet.SetSpawnPositionPacket
import com.nukkitx.protocol.bedrock.packet.SetTimePacket
import com.nukkitx.protocol.bedrock.packet.TextPacket
import com.nukkitx.protocol.bedrock.packet.TransferPacket
import com.nukkitx.protocol.bedrock.packet.UpdateAttributesPacket
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.Date
import kotlin.math.pow

class NetworkSession(
    private val server: Server,
    private val sessionManager: NetworkSessionManager,
    private val session: BedrockServerSession,
) {

    val logger: Logger = LoggerFactory.getLogger(NetworkSession::class.java)
    var ping: Int? = null
        private set
    var player: Player? = null
        private set
    var info: PlayerInfo? = null
        private set

    private var handler: BedrockPacketHandler? = null

    var connected = true
        private set
    var disconnectGuard = false
        private set
    var loggedIn = false
        private set
    var authenticated = false
        private set
    var connectedTime = Date()
        private set
    var cachedOfflinePlayerData: CompoundTag? = null
        private set

    var invManager: InventoryManager? = null

    val disposeHooks: MutableSet<() -> Unit> = mutableSetOf()

    val ip: String
        get() = session.address.hostString
    val port: Int
        get() = session.address.port

    init {
        setHandler(
            LoginPacketHandler(this, { info ->
                // TODO
                logger.info("Player: ${TextFormat.AQUA}${info.username}${TextFormat.RESET}")
            }, { authenticated, authRequired, error, clientPubKey ->

                // TODO
            })
        )
        logger.info("Session opened")
    }

    protected fun createPlayer() {
        // TODO
        server.createPlayer(this, info!!, authenticated, cachedOfflinePlayerData).onCompletion({
        }) {
            disconnect("Player creation failed")
        }
    }

    private fun onPlayerCreated(player: Player) {
        if (!isConnected()) {
            return
        }
        this.player = player
        if (!server.addOnlinePlayer(player)) {
            return
        }

        invManager = InventoryManager(player, this)

        val effectManager = player.effectManager
        val effectAddHook = { effect: EffectInstance, replaceOldEffect: Boolean ->
            // TODO: onEntityEffectAdded(player, effect, replaceOldEffect)
        }
        effectManager.effectAddHooks.add(effectAddHook)
        val effectRemoveHook = { effect: EffectInstance ->
            // TODO: onEntityEffectRemoved(player, effect)
        }
        effectManager.effectRemoveHooks.add(effectRemoveHook)
        disposeHooks.add {
            effectManager.effectAddHooks.remove(effectAddHook)
            effectManager.effectRemoveHooks.remove(effectRemoveHook)
        }

        val permissionHooks = player.permissionRecalculationCallbacks
        val permHook = { _: Map<String, Boolean> ->
            logger.debug("Syncing available commands and adventure settings due to permission recalculation")
            // TODO: syncAdventureSettings(player)
            // TODO: syncAvailableCommands()
        }
        permissionHooks.add(permHook)
        disposeHooks.add {
            permissionHooks.remove(permHook)
        }
        beginSpawnSequence()
    }

    fun isConnected(): Boolean {
        return connected && !disconnectGuard
    }

    fun getDisplayName(): String {
        return info?.username ?: session.address.toString()
    }

    fun getPing(): Long {
        return session.connection.ping
    }

    @JvmOverloads
    fun setHandler(handler: BedrockPacketHandler? = null) {
        this.handler = handler
    }

    @JvmOverloads
    fun sendDataPacket(packet: BedrockPacket, immediate: Boolean = false) {
        if (immediate) session.sendPacketImmediately(packet) else session.sendPacket(packet)
    }

    fun tryDisconnect(func: () -> Unit, reason: String) {
        if (connected && !disconnectGuard) {
            disconnectGuard = true
            func()
            disconnectGuard = false
            disposeHooks.forEach {
                it()
            }
            disposeHooks.clear()
            setHandler(null)
            connected = false
            sessionManager.remove(this)
            logger.info("Session closed due to $reason")

            invManager = null
        }
    }

    @JvmOverloads
    fun disconnect(reason: String, notify: Boolean = true) {
        tryDisconnect({
            // TODO: player?.let {
            //    it.onPostDisconnect(reason, null)
            // }
            doServerDisconnect(reason, notify)
        }, reason)
    }

    @JvmOverloads
    fun transfer(address: InetSocketAddress, reason: String = "transfer") {
        tryDisconnect({
            sendDataPacket(
                TransferPacket().apply {
                    this.address = address.address.hostAddress
                    port = address.port
                },
                true
            )
            doServerDisconnect(reason, true)
        }, reason)
    }

    fun onPlayerDestroyed(reason: String) {
        tryDisconnect({
            doServerDisconnect(reason, true)
        }, reason)
    }

    private fun doServerDisconnect(reason: String?, notify: Boolean = true) {
        if (notify) {
            sendDataPacket(
                DisconnectPacket().apply {
                    isMessageSkipped = reason == null
                    kickMessage = reason
                }
            )
        }
        session.disconnect(reason)
    }

    fun onClientDisconnect(reason: String) {
        tryDisconnect({
            player?.let {
                // TODO: it.onPostDisconnect(reason, null)
            }
        }, reason)
    }

    private fun setAuthenticationStatus(authenticated: Boolean, authRequired: Boolean, error: String?, clientPublicKey: String?) {
        if (!connected) {
            return
        }
        if (error == null) {
            var error = error
            if (authenticated && info !is XboxLivePlayerInfo) {
                error = "Expected XUID but none found"
            } else if (clientPublicKey == null) {
                error = "Missing client public key" // failsafe
            }
        }
        if (error != null) {
            disconnect(
                server.language.translateString(
                    KnownTranslationKeys.POCKETMINE_DISCONNECT_INVALIDSESSION,
                    listOf(
                        server.language.translateString(error)
                    )
                )
            )
            return
        }
        this.authenticated = authenticated
        if (!this.authenticated) {
            if (authRequired) {
                disconnect(
                    server.language.translateString(KnownTranslationKeys.DISCONNECTIONSCREEN_NOTAUTHENTICATED)
                )
                return
            }
            if (info is XboxLivePlayerInfo) {
                logger.warn("Discarding unexpected XUID for non-authenticated player")
                this.info = (this.info as XboxLivePlayerInfo).withoutXboxData()
            }
        }
        logger.debug("Xbox Live authenticated: " + if (authenticated) "YES" else "NO")
        val checkXUID = server.configGroup.getProperty("player.verify-xuid").asBoolean(true)
        val myXUID = if (info is XboxLivePlayerInfo) (info as XboxLivePlayerInfo).xuid else ""
        val kickForXUIDMismatch = kickForXUIDMismatch@{ xuid: String ->
            if (checkXUID && myXUID != xuid) {
                logger.debug("XUID mismatch: expected '$xuid', but got '$myXUID'")
                // TODO: Longer term, we should be identifying playerdata using something more reliable, like XUID or UUID.
                // However, that would be a very disruptive change, so this will serve as a stopgap for now.
                // Side note: this will also prevent offline players hijacking XBL playerdata on online servers, since their
                // XUID will always be empty.
                disconnect("XUID does not match (possible impersonation attempt)")
                return@kickForXUIDMismatch true
            }
            return@kickForXUIDMismatch false
        }
        sessionManager.sessions.forEach {
            if (it == this) {
                return@forEach
            }
            val info = it.info
            if (info != null && (info.username == this.info!!.username || info.uuid == this.info!!.uuid)) {
                if (kickForXUIDMismatch(if (info is XboxLivePlayerInfo) info.xuid else "")) {
                    return
                }
                val ev = PlayerDuplicateLoginEvent(this, it)
                ev.call()
                if (ev.isCancelled) {
                    disconnect(ev.disconnectMessage)
                    return
                }

                it.disconnect(ev.disconnectMessage)
            }
        }
        cachedOfflinePlayerData = server.getOfflinePlayerData(info!!.username)
        if (checkXUID) {
            val recordedXUID = if (cachedOfflinePlayerData != null) cachedOfflinePlayerData!!.getTag("LastKnownXUID") else null
            if (recordedXUID !is StringTag) {
                logger.debug("No previous XUID recorded, no choice but to trust this player")
            } else if (!kickForXUIDMismatch(recordedXUID.value)) {
                logger.debug("XUID match")
            }
        }
        // TODO: encryption
        onServerLoginSuccess()
    }

    private fun onServerLoginSuccess() {
        loggedIn = true
        sendDataPacket(
            PlayStatusPacket().apply {
                status = PlayStatusPacket.Status.LOGIN_SUCCESS
            }
        )
        logger.debug("Initiating resource packs phase")
        // TODO: Implement resource pack phase when we have proper resource pack handler
        // TODO: setHandler(ResourcePacksPacketHandler(this, server.resourcePackManager, {
        //     createPlayer()
        //  }))
    }

    private fun beginSpawnSequence() {
        setHandler(PreSpawnPacketHandler(server, player!!, this, invManager!!))
    }

    fun notifyTerrainReady() {
        logger.debug("Sending spawn notification, waiting for spawn response")
        sendDataPacket(
            PlayStatusPacket().apply {
                status = PlayStatusPacket.Status.PLAYER_SPAWN
            }
        )
        setHandler(SpawnResponsePacketHandler(::onClientSpawnResponse))
    }

    private fun onClientSpawnResponse() {
        logger.debug("Received spawn response, entering in-game phase")
        player?.immobile =
            false // TODO: HACK: we set this during the spawn sequence to prevent the client sending junk movements
        // TODO: player!!.doFirstSpawn()
        // TODO: setHandler(InGamePacketHandler(player!!, this, invManager!!))
    }

    fun onServerDeath() {
        /* TODO:
         if (handler is InGamePacketHandler) {
             setHandler(DeathPacketHandler(player!!, this, invManager ?: throw AssertionError("invManager must not be null")))
         }
         */
    }

    fun onServerRespawn() {
        player?.sendData(null)

        // TODO: syncAdventureSettings(player)
    }

    @JvmOverloads
    fun syncMovement(pos: Vector3, yaw: Float?, pitch: Float?, mode: MovePlayerPacket.Mode = MovePlayerPacket.Mode.NORMAL) {
        player?.let {
            val location = it.location
            val yaw = yaw ?: location.yaw
            val pitch = pitch ?: location.pitch

            sendDataPacket(
                MovePlayerPacket().apply {
                    this.mode = mode
                    position = Vector3f.ZERO.add(pos.x, pos.y, pos.z)
                    rotation = Vector3f.ZERO.add(yaw.toDouble(), pitch.toDouble(), 0.0)
                    isOnGround = it.onGround
                    ridingRuntimeEntityId = 0 // TODO: riding entity ID
                    tick = 0 // TODO: tick
                }
            )
        }
        /*
        TODO:
        if (handler is InGamePacketHandler) {
            handler.forceMoveSync = true
        }
         */
    }

    fun syncViewRadius(distance: Int) {
        sendDataPacket(
            ChunkRadiusUpdatedPacket().apply {
                radius = distance
            }
        )
    }

    fun syncViewAreaCenterPoint(newPos: Vector3, viewDisttance: Int) {
        sendDataPacket(
            NetworkChunkPublisherUpdatePacket().apply {
                position = Vector3i.ZERO.add(newPos.x, newPos.y, newPos.z)
                radius = viewDisttance.toDouble().pow(2.0).toInt()
            }
        )
    }

    fun syncPlayerSpawnPoint(newSpawn: Vector3) {
        sendDataPacket(
            SetSpawnPositionPacket().apply {
                spawnType = SetSpawnPositionPacket.Type.PLAYER_SPAWN
                spawnPosition = Vector3i.ZERO.add(newSpawn.x, newSpawn.y, newSpawn.z)
                dimensionId = DimensionIds.OVERWORLD.id
                blockPosition = Vector3i.ZERO.add(newSpawn.x, newSpawn.y, newSpawn.z)
            }
        )
    }

    fun syncWorldSpawnPoint(newSpawn: Vector3) {
        sendDataPacket(
            SetSpawnPositionPacket().apply {
                spawnType = SetSpawnPositionPacket.Type.WORLD_SPAWN
                spawnPosition = Vector3i.ZERO.add(newSpawn.x, newSpawn.y, newSpawn.z)
                dimensionId = DimensionIds.OVERWORLD.id
                blockPosition = Vector3i.ZERO.add(newSpawn.x, newSpawn.y, newSpawn.z)
            }
        )
    }

    fun syncGameMode(mode: GameMode, isRollBack: Boolean) {
        sendDataPacket(
            SetPlayerGameTypePacket().apply {
                gamemode = mode.id()
            }
        )
        player?.let {
            syncAdventureSettings(it)
        }
        if (!isRollBack && invManager != null) {
            invManager!!.syncCreative()
        }
    }

    fun syncAdventureSettings(player: Player) {
        val isOp = player.hasPermission(DefaultPermissions.ROOT_OPERATOR)
        val pk = AdventureSettingsPacket().apply {
            uniqueEntityId = player.getId()
            commandPermission = if (isOp) {
                CommandPermission.OPERATOR
            } else {
                CommandPermission.NORMAL
            }
            playerPermission = if (isOp) {
                PlayerPermission.OPERATOR
            } else {
                PlayerPermission.MEMBER
            }
            /*
            TODO:
            if (player.isSpectator()) {
                settings.add(AdventureSetting.WORLD_IMMUTABLE)
                settings.add(AdventureSetting.NO_PVM)
                settings.add(AdventureSetting.NO_MVP)
                settings.add(AdventureSetting.NO_CLIP)
            }

            if (player.hasAutoJump()) {
                settings.add(AdventureSetting.AUTO_JUMP)
            }
            if (player.getAllowFlight()) {
                settings.add(AdventureSetting.MAY_FLY)
            }
            if (player.isFlying()) {
                settings.add(AdventureSetting.FLYING)
            }

             */
        }
        sendDataPacket(pk)
    }

    fun syncAttributes(entity: Living, attributes: Map<Attribute.Identifier, Attribute>) {
        if (attributes.isNotEmpty()) {

            val networkAttributes: MutableList<AttributeData> = mutableListOf()
            attributes.forEach { (id, attribute) ->
                networkAttributes.add(
                    AttributeData(
                        id.name,
                        attribute.minValue,
                        attribute.maxValue,
                        attribute.currentValue,
                        attribute.defaultValue
                    )
                )
            }
            val pk = UpdateAttributesPacket()
            pk.attributes = networkAttributes
            pk.runtimeEntityId = 0 // TODO: Entity runtime id
            sendDataPacket(pk)
        }
    }

    fun syncActorData(entity: Entity, properties: Map<Int, MetadataProperty>) {
        sendDataPacket(
            SetEntityDataPacket().apply {
                runtimeEntityId = entity.getId()
                properties.forEach { (id, property) ->
                    when (property.id) {
                        EntityMetadataTypes.FLOAT -> {
                            val value = (property as FloatMetadataProperty).value
                            metadata[KookieToNukkitProtocolConverter.toEntityData(id, value)] = value
                        }
                        EntityMetadataTypes.BYTE -> {
                            val value = (property as ByteMetadataProperty).value
                            metadata[KookieToNukkitProtocolConverter.toEntityData(id, value)] = value
                        }
                        EntityMetadataTypes.SHORT -> {
                            val value = (property as ShortMetadataProperty).value
                            metadata[KookieToNukkitProtocolConverter.toEntityData(id, value)] = value
                        }
                        EntityMetadataTypes.INT -> {
                            val value = (property as IntMetadataProperty).value
                            metadata[KookieToNukkitProtocolConverter.toEntityData(id, value)] = value
                        }
                        EntityMetadataTypes.STRING -> {
                            val value = (property as StringMetadataProperty).value
                            metadata[KookieToNukkitProtocolConverter.toEntityData(id, value)] = value
                        }
                        EntityMetadataTypes.COMPOUND_TAG -> {
                            val value = (property as CompoundMetadataProperty).value
                            val builder = NbtMap.builder()
                            value.value.forEach {
                                builder[it.key] = it.value
                            }
                            metadata[KookieToNukkitProtocolConverter.toEntityData(id, value)] = builder.build()
                        }
                        EntityMetadataTypes.POS -> {
                            val value = (property as Vec3MetadataProperty).value
                            metadata[KookieToNukkitProtocolConverter.toEntityData(id, value)] =
                                Vector3i.ZERO.add(value.x, value.y, value.z)
                        }
                        EntityMetadataTypes.LONG -> {
                            val value = (property as LongMetadataProperty).value
                            metadata[KookieToNukkitProtocolConverter.toEntityData(id, value)] = value
                        }
                        EntityMetadataTypes.VECTOR3F -> {
                            val value = (property as Vec3MetadataProperty).value
                            metadata[KookieToNukkitProtocolConverter.toEntityData(id, value)] =
                                Vector3f.ZERO.add(value.x, value.y, value.z)
                        }
                    }
                }
                tick = 0
            }
        )
    }

    fun onEntityEffectAdded(entity: Entity, effect: EffectInstance, replaceOldEffect: Boolean) {
        sendDataPacket(
            MobEffectPacket().apply {
                runtimeEntityId = entity.getId()
                effectId = effect.effectType.internalRuntimeId
                amplifier = effect.amplifier
                duration = effect.duration
                event = if (replaceOldEffect) MobEffectPacket.Event.MODIFY else MobEffectPacket.Event.ADD
                isParticles = effect.visible
            }
        )
    }

    fun onEntityEffectRemoved(entity: Entity, effect: EffectInstance) {
        sendDataPacket(
            MobEffectPacket().apply {
                runtimeEntityId = entity.getId()
                effectId = effect.effectType.internalRuntimeId
                amplifier = effect.amplifier
                duration = effect.duration
                event = MobEffectPacket.Event.REMOVE
                isParticles = effect.visible
            }
        )
    }

    fun onEntityRemoved(entity: Entity) {
        sendDataPacket(
            RemoveEntityPacket().apply {
                uniqueEntityId = entity.getId()
            }
        )
    }

    fun syncAvailableCommands() {
        // TODO
        sendDataPacket(
            AvailableCommandsPacket().apply {
                // TODO
            }
        )
    }

    fun onRawChatMessage(message: String) {
        sendDataPacket(
            TextPacket().apply {
                type = TextPacket.Type.RAW
                this.message = message
            }
        )
    }

    fun onTranslatedChatMessage(key: String, parameters: List<String>) {
        sendDataPacket(
            TextPacket().apply {
                type = TextPacket.Type.TRANSLATION
                this.message = key
                this.parameters = parameters
                isNeedsTranslation = true
            }
        )
    }

    fun onJukeboxPopup(key: String, parameters: List<String>) {
        sendDataPacket(
            TextPacket().apply {
                type = TextPacket.Type.JUKEBOX_POPUP
                this.message = key
                this.parameters = parameters
                isNeedsTranslation = true
            }
        )
    }

    fun onPopup(message: String) {
        sendDataPacket(
            TextPacket().apply {
                type = TextPacket.Type.POPUP
                this.message = message
            }
        )
    }

    fun translatedPopup(key: String, parameters: List<String>) {
        sendDataPacket(
            TextPacket().apply {
                type = TextPacket.Type.POPUP
                this.message = key
                this.parameters = parameters
                isNeedsTranslation = true
            }
        )
    }

    fun onTip(message: String) {
        sendDataPacket(
            TextPacket().apply {
                type = TextPacket.Type.TIP
                this.message = message
            }
        )
    }

    fun onFormSent(id: Int, form: Form) {
        sendDataPacket(
            ModalFormRequestPacket().apply {
                formId = id
                formData = jsonMapper.writeValueAsString(form.jsonSerialize())
            }
        )
    }

    fun startUsingChunk(chunkX: Int, chunkZ: Int, onCompletion: () -> Unit) {
        val world = player!!.world

        /*
        TODO: This will need a re-implement of ChunkCache with the NukkitX Protocol implementaion
        ChunkCache.getInstance(world).request(chunkX, chunkZ).onResolve(
            listOf {
                if (!isConnected()) {
                    return@listOf
                }
                val currentWorld = player!!.location.world!!
                val status = player!!.getUsedChunkStatus(chunkX, chunkZ)
                if (world != currentWorld || status == null) {
                    logger.debug("Tried to send no-longer-active chunk $chunkX $chunkZ in world ${world.folderName}")
                    return@listOf
                }
                if (status != UsedChunkStatus.REQUESTED_SENDING) {
                    // TODO: make this an error
                    // this could be triggered due to the shitty way that chunk resends are handled
                    // right now - not because of the spammy re-requesting, but because the chunk status reverts
                    // to NEEDED if they want to be resent.
                    return@listOf
                }
                world.timings.syncChunkSend.startTiming()
            }
        )
         */
    }

    fun stopUsingChunk(chunkX: Int, chunkZ: Int) {
    }

    fun onEnterWorld() {
        player?.let {
            val world = it.world
            syncWorldTime(world.time)
            syncWorldDifficulty(world.difficulty)
            syncWorldSpawnPoint(world.spawnLocation)
        }
    }

    fun syncWorldTime(time: Long) {
        sendDataPacket(
            SetTimePacket().apply {
                this.time = time.toInt()
            }
        )
    }

    fun syncWorldDifficulty(difficulty: Int) {
        sendDataPacket(
            SetDifficultyPacket().apply {
                this.difficulty = difficulty
            }
        )
    }

    fun tick(): Boolean {
        info?.let {
            if (connectedTime.time <= Date().time + 10) {
                disconnect("Login Timeout")
                return false
            }
            return true
        }

        player?.let {
            it.doChunkRequest()
            val dirtyAttributes = it.attributeMap.needSend()
            syncAttributes(it, dirtyAttributes)
            dirtyAttributes.values.forEach { attribute ->
                attribute.markSynchronized()
            }
        }

        return true
    }

    companion object {
        @JvmStatic
        val jsonMapper: ObjectMapper = JsonMapper().registerKotlinModule()
    }
}
