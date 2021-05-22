package be.zvz.kookie.network.mcpe.convert

import be.zvz.kookie.entity.Skin
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinData
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinGeometry
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinImage
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import java.util.*

class LegacySkinAdapter : SkinAdapter {
    override fun toSkinData(skin: Skin): SkinData {
        val capeData = skin.capeData
        val skinImage = if (capeData == "") {
            SkinImage(0, 0, "")
        } else {
            SkinImage(32, 64, capeData)
        }
        var geometryName = skin.geometryName
        if (geometryName == "") {
            geometryName = "geometry.humanoid.custom"
        }
        val data = mapOf("geometry" to mapOf("default" to geometryName))
        val resourcePatch = jsonMapper.writeValueAsString(data)
        return SkinData(
            skin.skinId,
            "", // TODO: find out what is playFabId
            resourcePatch,
            SkinImage.fromLegacy(skin.skinData),
            mutableListOf(),
            skinImage,
            skin.geometryData
        )
    }

    override fun fromSkinData(data: SkinData): Skin {
        if (data.persona) {
            val arr = ByteArray(3).apply {
                Random().nextBytes(this)
            }
            return Skin("Standard_Custom", ("$arr\\xff").repeat(2048))
        }
        val capeData = if (data.personaCapeOnClassic) {
            ""
        } else {
            data.capeImage?.getData().toString()
        }

        try {
            val geometry = jsonMapper.readValue(data.resourcePatch, SkinGeometry::class.java)
            val geometryName = geometry.geometry["geometry"]?.get("default")
                ?: throw SkinAdapterException("Missing geometry name field")

            return Skin(data.skinId, data.skinImage.getData(), capeData, geometryName, data.geometryData)
        } catch (ignored: JsonProcessingException) {
        }
        throw SkinAdapterException("Failed to parse SkinData")
    }

    class SkinAdapterException(message: String) : RuntimeException(message)

    companion object {
        val jsonMapper = jacksonMapperBuilder()
            .addModule(AfterburnerModule())
            .build()
    }
}
