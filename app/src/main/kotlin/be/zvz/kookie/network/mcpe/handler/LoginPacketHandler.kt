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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.handler

import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.player.PlayerInfo
import com.nimbusds.jose.shaded.json.JSONArray
import com.nimbusds.jose.shaded.json.JSONObject
import com.nimbusds.jose.shaded.json.parser.JSONParser
import com.nukkitx.protocol.bedrock.packet.LoginPacket
import com.nukkitx.protocol.bedrock.util.EncryptionUtils

class LoginPacketHandler(
    val networkSession: NetworkSession,
    val playerInfoConsumer: (PlayerInfo) -> Unit,
    val authenticatedConsumer: (Boolean, Boolean, String?, String?) -> Unit
) : PacketHandler() {

    override fun handle(packet: LoginPacket): Boolean {
        val parser =
            JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE)
        val json = parser.parse(packet.chainData.toString()) as JSONObject
        val chainData = json["chain"] as JSONArray
        if (chainData.size != 3) {
            networkSession.disconnect("Invalid JWT chains received, expected 3, got ${chainData.size}")
            return false
        }
        if (!EncryptionUtils.verifyChain(chainData)) {
            networkSession.disconnect("disconnectionScreen.notAuthenticated")
            return false
        }
        // TODO: playerInfoConsumer(playerInfo)
        // TODO: XUID
        // TODO: displayName
        // TODO: clientUUID
        return true
    }
}
