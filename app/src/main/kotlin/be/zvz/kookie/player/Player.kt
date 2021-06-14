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
import be.zvz.kookie.entity.Human
import be.zvz.kookie.inventory.ArmorInventory
import be.zvz.kookie.inventory.PlayerInventory
import be.zvz.kookie.entity.Location
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
    val session: NetworkSession,
    val playerInfo: PlayerInfo,
    val authenticated: Boolean,
    val spawnLocation: Any, // TODO: Location,
    val namedTag: CompoundTag,
    location: Location
) : Human(location), CommandSender {
    override val language: Language
        get() = server.language
    val username = playerInfo.getUsername()
    var displayName = username
    override val name: String
        get() = username

    override val permissionRecalculationCallbacks: Set<(changedPermissionsOldValues: Map<String, Boolean>) -> Unit>
        get() = TODO("Not yet implemented")

    val inventory: PlayerInventory = TODO("PlayerInventory()")
    val armorInventory: ArmorInventory = TODO("ArmorInventory()")

    fun doChunkRequest() {
        TODO("Not yet implemented")
    }

    override fun sendMessage(message: String) {
        session.sendDataPacket(TextPacket.raw(message))
    }

    override fun sendMessage(message: TranslationContainer) {
        session.sendDataPacket(TextPacket.translation(message.text, message.params.toMutableList()))
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
}
