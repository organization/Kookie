package be.zvz.kookie.network.mcpe.handler

import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.player.PlayerInfo
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler
import com.nukkitx.protocol.bedrock.packet.LoginPacket
import com.nukkitx.protocol.bedrock.util.EncryptionUtils
import net.minidev.json.JSONArray
import net.minidev.json.JSONObject
import net.minidev.json.parser.JSONParser

class LoginPacketHandler(
    val networkSession: NetworkSession,
    val playerInfoConsumer: (PlayerInfo) -> Unit,
    val authenticatedConsumer: () -> Unit
) : BedrockPacketHandler {

    override fun handle(packet: LoginPacket): Boolean {
        val parser = JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE)
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
