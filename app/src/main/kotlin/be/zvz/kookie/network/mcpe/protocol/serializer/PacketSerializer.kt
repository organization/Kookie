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
package be.zvz.kookie.network.mcpe.protocol.serializer

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.LittleEndianNbtSerializer
import be.zvz.kookie.nbt.NbtDataException
import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.convert.GlobalItemTypeDictionary
import be.zvz.kookie.network.mcpe.protocol.PacketDecodeException
import be.zvz.kookie.network.mcpe.protocol.types.GameRule
import be.zvz.kookie.network.mcpe.protocol.types.command.CommandOriginData
import be.zvz.kookie.network.mcpe.protocol.types.entity.*
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStack
import be.zvz.kookie.network.mcpe.protocol.types.skin.*
import be.zvz.kookie.utils.BinaryStream
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class PacketSerializer(buffer: String = "", offset: AtomicInteger = AtomicInteger(0)) : BinaryStream(buffer, offset) {

    private val shieldItemRuntimeId: Int = GlobalItemTypeDictionary.getInstance().dictionary.fromStringId("minecraft:shield")

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

    fun getBlockPosition(): BlockPosition = BlockPosition(getVarInt(), getUnsignedVarInt(), getVarInt())
    fun getBlockPosition(pos: BlockPosition): BlockPosition = pos.apply {
        x = getVarInt()
        y = getUnsignedVarInt()
        z = getVarInt()
    }

    fun putBlockPosition(x: Int, y: Int, z: Int) {
        putVarInt(x)
        putUnsignedVarInt(y)
        putVarInt(z)
    }

    fun putBlockPosition(pos: BlockPosition) {
        putVarInt(pos.x)
        putUnsignedVarInt(pos.y)
        putVarInt(pos.z)
    }

    // TODO: SkinData

    fun getSkin(): SkinData {
        val skinId = getString()
        val playFabId = getString()
        val skinResourcePatch = getString()
        val skinData = getSkinImage()
        val animations = mutableListOf<SkinAnimation>().apply {
            val animationCount = getLInt()
            for (i in 0 until animationCount) {
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
            for (i in 0 until personaPieceCount) {
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
            for (i in 0 until pieceTintColorCount) {
                val pieceType = getString()
                add(
                    PersonaPieceTintColor(
                        pieceType,
                        mutableListOf<String>().apply {
                            val colorCount = getLInt()
                            for (j in 0 until colorCount) {
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

    fun getItemStackWithoutStackId(): ItemStack {
        return getItemStack {}
    }

    fun putItemStackWithoutStackId(item: ItemStack) {
        putItemStack(item) {}
    }

    fun putItemStack(item: ItemStack, writeExtraCrapInTheMiddle: (PacketSerializer) -> Unit) {
        if (item.id == 0) {
            putVarInt(0)
            return
        }

        putVarInt(item.id)
        putLShort(item.count)
        putUnsignedVarInt(item.meta)

        writeExtraCrapInTheMiddle(this)

        putVarInt(item.blockRuntimeId)
        val shieldItemRuntimeId = this.shieldItemRuntimeId
        putString(
            run {
                val extraData = PacketSerializer()
                val nbt = item.nbt
                if (nbt !== null) {
                    extraData.putLShort(0xffff)
                    extraData.putByte(1)
                    extraData.put(LittleEndianNbtSerializer().write(TreeRoot(nbt)))
                } else {
                    extraData.putLShort(0)
                }

                extraData.putLInt(item.canPlaceOn.size)
                item.canPlaceOn.forEach { entry ->
                    extraData.putLShort(entry.length)
                    extraData.put(entry)
                }
                extraData.putLInt(item.canDestroy.size)
                item.canPlaceOn.forEach { entry ->
                    extraData.putLShort(entry.length)
                    extraData.put(entry)
                }

                val blockingTick = item.shieldBlockingTick
                if (item.id == shieldItemRuntimeId) {
                    extraData.putLLong(blockingTick ?: 0)
                }
                return@run extraData.buffer.toString()
            }
        )
    }

    fun getItemStack(readExtraCrapInTheMiddle: (PacketSerializer) -> Unit): ItemStack {
        val id = getVarInt()
        if (id == 0) {
            return ItemStack.empty()
        }
        val count = getLShort()
        val meta = getUnsignedVarInt()

        readExtraCrapInTheMiddle(this)

        val blockRuntimeId = getVarInt()
        val extraData = PacketSerializer(getString())
        val shieldItemRuntimeId = this.shieldItemRuntimeId
        return run {
            val nbtLen = extraData.getLShort()
            var compound: CompoundTag? = null
            if (nbtLen == 0xffff) {
                val nbtDataVersion = extraData.getByte()
                if (nbtDataVersion != 1) {
                    throw PacketDecodeException("Unexpected NBT data version $nbtDataVersion")
                }
                val offset = extraData.offset
                try {
                    compound =
                        LittleEndianNbtSerializer().read(extraData.buffer.toString(), offset, 512).mustGetCompoundTag()
                } catch (e: NbtDataException) {
                    throw PacketDecodeException.wrap(e, "Failed decoding NBT root")
                } finally {
                    extraData.offset.set(offset.get())
                }
            } else if (nbtLen != 0) {
                throw PacketDecodeException("Unexpected fake NBT length $nbtLen")
            }
            val canPlaceOn = mutableListOf<String>()
            for (i in 0 until extraData.getLInt()) {
                canPlaceOn.add(extraData.get(extraData.getLShort()))
            }

            val canDestroy = mutableListOf<String>()
            for (i in 0 until extraData.getLInt()) {
                canDestroy.add(extraData.get(extraData.getLShort()))
            }

            val shieldBlockingTick: Long? = if (id == shieldItemRuntimeId) {
                extraData.getLLong()
            } else {
                null
            }
            if (!extraData.feof()) {
                throw PacketDecodeException("Unexpected trailing extradata for network item $id")
            }
            return@run ItemStack(
                id,
                meta,
                count,
                blockRuntimeId,
                compound,
                canPlaceOn,
                canDestroy,
                shieldBlockingTick
            )
        }
    }

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

    fun readGenericTypeNetworkId(): Int {
        return getVarInt()
    }

    fun writeGenericTypeNetworkId(id: Int) {
        putVarInt(id)
    }

    fun putGameRules(gameRules: MutableMap<String, GameRule>) {
        putUnsignedVarInt(gameRules.size)
        gameRules.forEach { (name, gameRule) ->
            putString(name)
            putUnsignedVarInt(gameRule.typeId)
            gameRule.encode(this)
        }
    }

    fun getNbtCompoundRoot(): CompoundTag {
        try {
            return getNbtRoot().mustGetCompoundTag()
        } catch (ignored: NbtDataException) {
            throw PacketDecodeException.wrap(ignored, "Expected Compound NBT root")
        }
    }

    fun getNbtRoot(): TreeRoot {
        try {
            return (LittleEndianNbtSerializer()).read(buffer.toString(), offset, 512)
        } catch (ignored: NbtDataException) {
            throw PacketDecodeException.wrap(ignored, "Failed to decode NBT root")
        }
    }

    private fun readMetadataProperty(type: Int): MetadataProperty {
        return when (type) {
            EntityMetadataTypes.BYTE -> ByteMetadataProperty.read(this)
            EntityMetadataTypes.SHORT -> ShortMetadataProperty.read(this)
            EntityMetadataTypes.INT -> IntMetadataProperty.read(this)
            EntityMetadataTypes.FLOAT -> FloatMetadataProperty.read(this)
            EntityMetadataTypes.STRING -> StringMetadataProperty.read(this)
            EntityMetadataTypes.COMPOUND_TAG -> CompoundMetadataProperty.read(this)
            EntityMetadataTypes.POS -> BlockPosMetadataProperty.read(this)
            EntityMetadataTypes.LONG -> LongMetadataProperty.read(this)
            EntityMetadataTypes.VECTOR3F -> Vec3MetadataProperty.read(this)
            else -> throw PacketDecodeException("Unknown entity metadata type $type")
        }
    }

    fun getEntityMetadataProperty(): MutableMap<Int, MetadataProperty> {
        val properties: MutableMap<Int, MetadataProperty> = mutableMapOf()
        for (i in 0 until getUnsignedVarInt()) {
            val key = getUnsignedVarInt()
            val type = getUnsignedVarInt()
            properties[key] = readMetadataProperty(type)
        }
        return properties
    }

    fun putEntityMetadata(metadata: MutableMap<Int, MetadataProperty>) {
        putUnsignedVarInt(metadata.size)
        metadata.forEach { (key, metadata) ->
            putUnsignedVarInt(key)
            putUnsignedVarInt(metadata.id)
            metadata.write(this)
        }
    }

    fun getEntityLink(): EntityLink {
        return EntityLink(
            getEntityUniqueId(),
            getEntityUniqueId(),
            getByte(),
            getBoolean(),
            getBoolean()
        )
    }

    fun putEntityLink(link: EntityLink) {
        putEntityUniqueId(link.fromEntityId)
        putEntityUniqueId(link.toEntityId)
        putByte(link.type)
        putBoolean(link.immediate)
        putBoolean(link.causedByRider)
    }

    fun getCommandOriginData(): CommandOriginData = CommandOriginData().apply {
        this.type = CommandOriginData.Origin.from(getUnsignedVarInt())
        this.uuid = getUUID()
        this.requestId = getString()

        if (this.type === CommandOriginData.Origin.DEV_CONSOLE || this.type === CommandOriginData.Origin.TEST) {
            this.playerEntityUniqueId = getVarLong()
        }
    }

    fun putCommandOriginData(data: CommandOriginData) {
        putUnsignedVarInt(data.type.id)
        putUUID(data.uuid)
        putString(data.requestId)
        if (data.type === CommandOriginData.Origin.DEV_CONSOLE || data.type === CommandOriginData.Origin.TEST) {
            putVarLong(data.playerEntityUniqueId)
        }
    }

    fun getByteRotation(): Float = (getByte() * (360 / 256)).toFloat()

    fun putByteRotation(rotation: Float) {
        putByte((rotation / (360 / 256)).toInt())
    }

    fun putAttributeList(attributes: MutableList<NetworkAttribute>) {
        putUnsignedVarInt(attributes.size)
        attributes.forEach {
            putLFloat(it.min)
            putLFloat(it.max)
            putLFloat(it.current)
            putLFloat(it.default)
            putString(it.id)
        }
    }

    fun getAttributeList(): MutableList<NetworkAttribute> {
        val list: MutableList<NetworkAttribute> = mutableListOf()
        for (i in 0 until getUnsignedVarInt()) {
            val min = getLFloat()
            val max = getLFloat()
            val current = getLFloat()
            val default = getLFloat()
            val id = getString()
            list.add(
                NetworkAttribute(
                    id,
                    min,
                    max,
                    current,
                    default
                )
            )
        }
        return list
    }

    data class BlockPosition @JvmOverloads constructor(var x: Int = 0, var y: Int = 0, var z: Int = 0)
}
