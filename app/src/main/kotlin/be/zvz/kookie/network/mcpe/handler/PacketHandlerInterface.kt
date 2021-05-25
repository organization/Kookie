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

    fun handleResourcePackClientResponse(packet: ResourcePackClientResponsePacket): Boolean

    fun handleText(packet: TextPacket): Boolean

    fun handleSetTime(packet: SetTimePacket): Boolean

    fun handleStartGame(packet: StartGamePacket): Boolean

    fun handleAddPlayer(packet: AddPlayerPacket): Boolean
}
