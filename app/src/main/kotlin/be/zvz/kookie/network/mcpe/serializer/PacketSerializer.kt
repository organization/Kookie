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
package be.zvz.kookie.network.mcpe.serializer

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.types.skin.*
import be.zvz.kookie.utils.BinaryStream
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class PacketSerializer(buffer: String = "", offset: Int = 0) : BinaryStream(buffer, offset) {

    var shieldRuntimeId: Int = 0

    fun getString(): String = get(getUnsignedVarInt())

    fun putString(v: String) {
        putUnsignedVarInt(v.length)
        put(v)
    }

    fun getUUID(): UUID {
        val p1 = get(8).reversed()
        val p2 = get(8).reversed()
        return UUID.fromString(p1 + p2)
    }

    fun putUUID(uuid: UUID) {
        val bytes = uuid.toString()
        put(bytes.substring(0, 8).reversed())
        put(bytes.substring(8, 8).reversed())
    }

    fun getBlockPosition(x: AtomicInteger, y: AtomicInteger, z: AtomicInteger) {
        x.set(getVarInt())
        y.set(getUnsignedVarInt())
        z.set(getVarInt())
    }

    fun putBlockPosition(x: Int, y: Int, z: Int) {
        putVarInt(x)
        putUnsignedVarInt(y)
        putVarInt(z)
    }

    // TODO: SkinData

    fun getSkin(): SkinData {
        val skinId = getString()
        val playFabId = getString()
        val skinResourcePatch = getString()
        val skinData = getSkinImage()
        val animations = mutableListOf<SkinAnimation>().apply {
            val animationCount = getLInt()
            for (i in 0..animationCount) {
                val skinImage = getSkinImage()
                val animationType = getLInt()
                val animationFrames = getLFloat()
                val expressionType = getLInt()
                add(SkinAnimation(skinImage, animationType, animationFrames, expressionType))
            }
        }
        val capeData = getSkinImage()
        val geometryData = getString()
        val animationData = getString()
        val premium = getBoolean()
        val persona = getBoolean()
        val capeOnClassic = getBoolean()
        val capeId = getString()
        val fullSkinId = getString()
        val armSize = getString()
        val skinColor = getString()
        val personaPieces = mutableListOf<PersonaSkinPiece>().apply {
            val personaPieceCount = getLInt()
            for (i in 0..personaPieceCount) {
                val pieceId = getString()
                val pieceType = getString()
                val packId = getString()
                val isDefaultPiece = getBoolean()
                val productId = getString()
                add(PersonaSkinPiece(pieceId, pieceType, packId, isDefaultPiece, productId))
            }
        }

        val pieceTintColors = mutableListOf<PersonaPieceTintColor>().apply {
            val pieceTintColorCount = getLInt()
            for (i in 0..pieceTintColorCount) {
                val pieceType = getString()
                add(
                    PersonaPieceTintColor(
                        pieceType,
                        mutableListOf<String>().apply {
                            val colorCount = getLInt()
                            for (j in 0..colorCount) {
                                add(getString())
                            }
                        }
                    )
                )
            }
        }

        return SkinData(
            skinId,
            playFabId,
            skinResourcePatch,
            skinData,
            animations,
            capeData,
            geometryData,
            animationData,
            premium,
            persona,
            capeOnClassic,
            capeId,
            fullSkinId,
            armSize,
            skinColor,
            personaPieces,
            pieceTintColors
        )
    }

    fun putSkin(skin: SkinData) {
        putString(skin.skinId)
        putString(skin.playFabId)
        putString(skin.resourcePatch)
        putLInt(skin.animations.size)
        skin.animations.forEach {
            putSkinImage(it.getImage())
            putLInt(it.getType())
            putLFloat(it.getFrames())
            putLInt(it.getExpressionType())
        }
        skin.capeImage?.let { putSkinImage(it) }
        putString(skin.geometryData)
        putString(skin.animationData)
        putBoolean(skin.premium)
        putBoolean(skin.persona)
        putBoolean(skin.personaCapeOnClassic)
        putString(skin.capeId)
        skin.fullSkinId?.let { putString(it) }
        putString(skin.armSize)
        putString(skin.skinColor)
        putLInt(skin.personaPieces.size)
        skin.personaPieces.forEach {
            putString(it.getPieceId())
            putString(it.getPieceType())
            putString(it.getPackId())
            putBoolean(it.isDefaultPiece())
            putString(it.getProductId())
        }
        putLInt(skin.pieceTintColors.size)
        skin.pieceTintColors.forEach {
            putString(it.getPieceType())
            putLInt(it.getColors().size)
            it.getColors().forEach { color ->
                putString(color)
            }
        }
    }

    fun getSkinImage(): SkinImage {
        val width = getLInt()
        val height = getLInt()
        val data = getString()
        return SkinImage(width, height, data)
    }

    fun putSkinImage(skinImage: SkinImage) {
        putLInt(skinImage.getWidth())
        putLInt(skinImage.getHeight())
        putString(skinImage.getData())
    }

    // TODO: ItemStack

    fun getEntityUniqueId(): Long {
        return getVarLong()
    }

    fun putEntityUniqueId(id: Long) {
        putVarLong(id)
    }

    fun getEntityRuntimeId(): Long {
        return getUnsignedVarLong()
    }

    fun putEntityRuntimeId(id: Long) {
        putUnsignedVarLong(id)
    }

    fun getVector3(): Vector3 {
        return Vector3(getLFloat(), getLFloat(), getLFloat())
    }

    fun putVector3Nullable(vector: Vector3?) {
        if (vector != null) {
            putVector3(vector)
        } else {
            putLFloat(0F)
            putLFloat(0F)
            putLFloat(0F)
        }
    }

    fun putVector3(vector: Vector3) {
        putLFloat(vector.x)
        putLFloat(vector.y)
        putLFloat(vector.z)
    }
}
