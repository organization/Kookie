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
package be.zvz.kookie.permission

import be.zvz.kookie.plugin.Plugin
import be.zvz.kookie.plugin.PluginException
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjObjMaps
import com.koloboke.collect.set.hash.HashObjSets

class PermissibleBase(rootPermissionsMap: Map<String, Boolean>) : Permissible {
    override val permissionRecalculationCallbacks: MutableSet<(changedPermissionsOldValues: Map<String, Boolean>) -> Unit> =
        HashObjSets.newMutableSet()

    private val rootPermissions: MutableMap<String, Boolean> = HashMap(rootPermissionsMap)

    private val attachments: MutableMap<Int, PermissionAttachment> = HashIntObjMaps.newMutableMap()
    private val permissions: MutableMap<String, PermissionAttachmentInfo> = HashObjObjMaps.newMutableMap()

    override fun setBasePermission(name: String, grant: Boolean) {
        rootPermissions[name] = grant
        recalculatePermissions()
    }

    override fun unsetBasePermission(name: String) {
        rootPermissions.remove(name)
        recalculatePermissions()
    }

    override fun isPermissionSet(name: String): Boolean = permissions.containsKey(name)

    override fun hasPermission(name: String): Boolean = if (isPermissionSet(name)) {
        permissions.getValue(name).value
    } else {
        false
    }

    override fun addAttachment(plugin: Plugin, name: String?, value: Boolean?): PermissionAttachment {
        if (!plugin.enabled) {
            throw PluginException("Plugin ${plugin.description.name} is disabled")
        }

        val result = PermissionAttachment(plugin)
        attachments[result.hashCode()] = result
        if (name !== null && value !== null) {
            result.permissions[name] = value
        }

        result.subscribePermissible(this)
        recalculatePermissions()
        return result
    }

    override fun removeAttachment(attachment: PermissionAttachment) {
        if (attachments.containsKey(attachment.hashCode())) {
            attachments.remove(attachment.hashCode())
            attachment.unsubscribePermissible(this)
            attachment.removalCallback?.attachmentRemoved(this, attachment)
            recalculatePermissions()
        }
    }

    override fun recalculatePermissions(): Map<String, Boolean> {
        // TODO: Timings permissibleCalculation
        PermissionManager.unsubscribeFromAllPermissions(this)
        val oldPermission = HashMap(permissions)
        permissions.clear()

        rootPermissions.forEach { (name, isGranted) ->
            val perm = PermissionManager.getPermission(name)
            if (perm === null) {
                throw IllegalStateException("Unregistered root permission $name")
            }
            permissions[name] = PermissionAttachmentInfo(name, null, isGranted, null)
            PermissionManager.subscribeToPermission(name, this)
            calculateChildPermissions(perm.children, !isGranted, null, permissions[name])
        }

        attachments.forEach { (_, attachment) ->
            calculateChildPermissions(attachment.permissions, false, attachment, null)
        }

        // TODO: Timings permissibleCalculationDiff
        // TODO: Timings permissibleCalculationCallback
        // TODO: Timings permissibleCalculation
        return HashObjObjMaps.newMutableMap()
    }

    private fun calculateChildPermissions(
        children: Map<String, Boolean>,
        invert: Boolean,
        attachment: PermissionAttachment?,
        parent: PermissionAttachmentInfo?
    ) {
        children.forEach { (name, v) ->
            val perm = PermissionManager.getPermission(name)
            val value = v xor invert
            permissions[name] = PermissionAttachmentInfo(name, attachment, value, parent)
            PermissionManager.subscribeToPermission(name, this)

            if (perm is Permission) {
                calculateChildPermissions(perm.children, !value, attachment, permissions[name])
            }
        }
    }

    override fun getEffectivePermissions(): Map<String, PermissionAttachmentInfo> = permissions
}
