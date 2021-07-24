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
package be.zvz.kookie.player

import be.zvz.kookie.Server
import be.zvz.kookie.command.CommandSender
import be.zvz.kookie.crafting.CraftingGrid
import be.zvz.kookie.entity.Human
import be.zvz.kookie.entity.Location
import be.zvz.kookie.entity.Skin
import be.zvz.kookie.lang.Language
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.network.mcpe.protocol.TextPacket
import be.zvz.kookie.permission.Permission
import be.zvz.kookie.permission.PermissionAttachment
import be.zvz.kookie.permission.PermissionAttachmentInfo
import be.zvz.kookie.plugin.Plugin

class Player(
    override var server: Server,
    val networkSession: NetworkSession,
    val playerInfo: PlayerInfo,
    val authenticated: Boolean,
    val spawnLocation: Any, // TODO: Location,
    val namedTag: CompoundTag,
    skin: Skin,
    location: Location
) : Human(skin, location), CommandSender, ChunkListener {
    override val language: Language get() = server.language
    val username = playerInfo.getUsername()
    var displayName = username
    override val name: String get() = username
    override val permissionRecalculationCallbacks: Set<(changedPermissionsOldValues: Map<String, Boolean>) -> Unit>
        get() = TODO("Not yet implemented")
    lateinit var craftingGrid: CraftingGrid
        private set


    override fun sendMessage(message: String) {
        networkSession.sendDataPacket(TextPacket.raw(message))
    }

    override fun sendMessage(message: TranslationContainer) {
        networkSession.sendDataPacket(TextPacket.translation(message.text, message.params.toMutableList()))
    }

    override fun getScreenLineHeight(): Int {
        TODO("Not yet implemented")
    }

    override fun setScreenLineHeight(height: Int?) {
        TODO("Not yet implemented")
    }

    override fun setBasePermission(name: String, grant: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setBasePermission(permission: Permission, grant: Boolean) {
        TODO("Not yet implemented")
    }

    override fun unsetBasePermission(name: String) {
        TODO("Not yet implemented")
    }

    override fun unsetBasePermission(permission: Permission) {
        TODO("Not yet implemented")
    }

    override fun isPermissionSet(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPermissionSet(permission: Permission): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPermission(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPermission(permission: Permission): Boolean {
        TODO("Not yet implemented")
    }

    override fun addAttachment(plugin: Plugin, name: String?, value: Boolean?): PermissionAttachment {
        TODO("Not yet implemented")
    }

    override fun removeAttachment(attachment: PermissionAttachment) {
        TODO("Not yet implemented")
    }

    override fun recalculatePermissions(): Map<String, Boolean> {
        TODO("Not yet implemented")
    }

    override fun getEffectivePermissions(): Map<String, PermissionAttachmentInfo> {
        TODO("Not yet implemented")
    }

    fun doChunkRequest() {
        TODO("Not yet implemented")
    }

    /** Requests chunks from the world to be sent, up to a set limit every tick. This operates on the results of the most recent chunk order. */
    private fun requestChunks() {
        TODO("Not yet implemented")
    }

    /**
     * Calculates which new chunks this player needs to use, and which currently-used chunks it needs to stop using.
     * This is based on factors including the player's current render radius and current position.
     */
    private fun orderChunks() {
        TODO("Not yet implemented")
    }

    private fun unloadChunk(chunkX: Int, chunkZ: Int, world: World = this.world) {
        TODO("Not yet implemented")
    }

    private fun spawnEntitiesOnAllChunks() {
        TODO("Not yet implemented")
    }

    private fun spawnEntitiesOnChunk(chunkX: Int, chunkZ: Int) {
        TODO("Not yet implemented")
    }

    /**
     * Returns whether the player is using the chunk with the given coordinates,
     * irrespective of whether the chunk has been sent yet.
     */
    fun isUsingChunk(chunkX: Int, chunkZ: Int): Boolean = TODO("Not yet implemented")

    /** Returns a usage status of the given chunk, or null if the player is not using the given chunk.  */
    fun getUsedChunkStatus(chunkX: Int, chunkZ: Int): UsedChunkStatus? = TODO("Not yet implemented")

    /** Returns whether the target chunk has been sent to this player. */
    fun hasReceivedChunk(chunkX: Int, chunkZ: Int): Boolean = TODO("Not yet implemented")

    override fun onChunkChanged(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        TODO("Not yet implemented")
    }

    override fun onChunkUnloaded(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        TODO("Not yet implemented")
    }
}
