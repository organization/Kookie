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

import be.zvz.kookie.command.CommandSender
import be.zvz.kookie.entity.Human
import be.zvz.kookie.permission.Permission
import be.zvz.kookie.permission.PermissionAttachment
import be.zvz.kookie.permission.PermissionAttachmentInfo
import be.zvz.kookie.plugin.Plugin

class Player : Human(), CommandSender {

    fun doChunkRequest() {
        TODO("Not yet implemented")
    }

    override fun sendMessage(message: String) {
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

    override fun getPermissionRecalculationCallbacks(): Set<(changedPermissionsOldValues: Map<String, Boolean>) -> Unit> {
        TODO("Not yet implemented")
    }

    override fun getEffectivePermissions(): List<PermissionAttachmentInfo> {
        TODO("Not yet implemented")
    }
}
