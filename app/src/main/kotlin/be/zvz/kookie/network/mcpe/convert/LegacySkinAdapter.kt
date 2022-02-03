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
package be.zvz.kookie.network.mcpe.convert

import be.zvz.kookie.entity.Skin
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinData
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinGeometry
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinImage
import be.zvz.kookie.utils.Json
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.readValue
import com.nukkitx.protocol.bedrock.data.skin.AnimatedTextureType
import com.nukkitx.protocol.bedrock.data.skin.AnimationData
import com.nukkitx.protocol.bedrock.data.skin.ImageData
import com.nukkitx.protocol.bedrock.data.skin.SerializedSkin
import java.util.Random

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
        val resourcePatch = Json.jsonMapper.writeValueAsString(data)
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
            val arr = ByteArray(3).apply(Random()::nextBytes)
            return Skin("Standard_Custom", ("$arr\\xff").repeat(2048))
        }
        val capeData = if (data.personaCapeOnClassic) {
            ""
        } else {
            data.capeImage?.getData().toString()
        }

        try {
            val geometry: SkinGeometry = Json.jsonMapper.readValue(data.resourcePatch)
            val geometryName = geometry.geometry.getValue("default")

            return Skin(data.skinId, data.skinImage.getData(), capeData, geometryName, data.geometryData)
        } catch (ignored: JsonProcessingException) {
            throw SkinAdapterException("Missing geometry name field")
        }
    }

    override fun toSerializedSkinData(skinData: SkinData): SerializedSkin {
        val skin = SerializedSkin.of(
            skinData.skinId,
            skinData.playFabId,
            skinData.resourcePatch,
            ImageData.of(
                skinData.skinImage.getData().toByteArray(),
            ),
            skinData.animations.map {
                AnimationData(
                    ImageData.of(
                        it.getImage().getWidth(),
                        it.getImage().getHeight(),
                        it.getImage().getData().toByteArray()
                    ),
                    when (it.getImage().getWidth() * it.getImage().getHeight()) {
                        128 * 128 -> AnimatedTextureType.BODY_128X128
                        32 * 32 -> AnimatedTextureType.BODY_32X32
                        else -> AnimatedTextureType.NONE
                    },
                    it.getFrames()
                )
            },
            ImageData.of(
                skinData.capeImage!!.getWidth(),
                skinData.capeImage!!.getHeight(),
                skinData.capeImage!!.getData().toByteArray()
            ),
            skinData.geometryData,
            skinData.animationData,
            skinData.premium,
            skinData.persona,
            skinData.personaCapeOnClassic,
            skinData.capeId,
            skinData.fullSkinId
        )
        return skin
    }

    class SkinAdapterException(message: String) : RuntimeException(message)
}
