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
import be.zvz.kookie.entity.Living
import be.zvz.kookie.entity.effect.EffectInstance
import be.zvz.kookie.event.player.PlayerDuplicateLoginEvent
import be.zvz.kookie.lang.KnownTranslationKeys
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.network.mcpe.handler.LoginPacketHandler
import be.zvz.kookie.network.mcpe.handler.PreSpawnPacketHandler
import be.zvz.kookie.network.mcpe.handler.SpawnResponsePacketHandler
import be.zvz.kookie.player.Player
import be.zvz.kookie.player.PlayerInfo
import be.zvz.kookie.player.XboxLivePlayerInfo
import be.zvz.kookie.utils.TextFormat
import com.nukkitx.math.vector.Vector3f
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.BedrockServerSession
import com.nukkitx.protocol.bedrock.data.AttributeData
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket
import com.nukkitx.protocol.bedrock.packet.TransferPacket
import com.nukkitx.protocol.bedrock.packet.UpdateAttributesPacket
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.Date

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
}
