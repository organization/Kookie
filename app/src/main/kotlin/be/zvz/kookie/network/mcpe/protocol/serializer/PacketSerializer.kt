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
import be.zvz.kookie.network.mcpe.protocol.types.BooleanGameRule
import be.zvz.kookie.network.mcpe.protocol.types.FloatGameRule
import be.zvz.kookie.network.mcpe.protocol.types.GameRule
import be.zvz.kookie.network.mcpe.protocol.types.GameRuleType
import be.zvz.kookie.network.mcpe.protocol.types.IntGameRule
import be.zvz.kookie.network.mcpe.protocol.types.StructureEditorData
import be.zvz.kookie.network.mcpe.protocol.types.StructureSettings
import be.zvz.kookie.network.mcpe.protocol.types.command.CommandOriginData
import be.zvz.kookie.network.mcpe.protocol.types.entity.BlockPosMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.ByteMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.CompoundMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityLink
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataTypes
import be.zvz.kookie.network.mcpe.protocol.types.entity.FloatMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.IntMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.LongMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.MetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.NetworkAttribute
import be.zvz.kookie.network.mcpe.protocol.types.entity.ShortMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.StringMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.Vec3MetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStack
import be.zvz.kookie.network.mcpe.protocol.types.recipe.RecipeIngredient
import be.zvz.kookie.network.mcpe.protocol.types.skin.PersonaPieceTintColor
import be.zvz.kookie.network.mcpe.protocol.types.skin.PersonaSkinPiece
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinAnimation
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinData
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinImage
import be.zvz.kookie.utils.BinaryStream
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjObjMaps
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

class PacketSerializer @JvmOverloads constructor(
    buffer: String = "",
    offset: AtomicInteger = AtomicInteger(0)
) : BinaryStream(buffer, offset) {

    private val shieldItemRuntimeId: Int =
        GlobalItemTypeDictionary.dictionary.fromStringId("minecraft:shield")

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

    fun getSignedBlockPosition(): BlockPosition = BlockPosition(getVarInt(), getVarInt(), getVarInt())
    fun getSignedBlockPosition(pos: BlockPosition): BlockPosition = pos.apply {
        x = getVarInt()
        y = getVarInt()
        z = getVarInt()
    }

    fun putSignedBlockPosition(x: Int, y: Int, z: Int) {
        putVarInt(x)
        putVarInt(y)
        putVarInt(z)
    }

    fun putSignedBlockPosition(pos: BlockPosition) {
        putVarInt(pos.x)
        putVarInt(pos.y)
        putVarInt(pos.z)
    }

    // TODO: SkinData
    fun getSkin(): SkinData {
        val skinId = getString()
        val playFabId = getString()
        val skinResourcePatch = getString()
        val skinData = getSkinImage()
        val animations = mutableListOf<SkinAnimation>().apply {
            repeat(getLInt()) {
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
            repeat(getLInt()) {
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
            repeat(pieceTintColorCount) {
                val pieceType = getString()
                add(
                    PersonaPieceTintColor(
                        pieceType,
                        mutableListOf<String>().apply {
                            val colorCount = getLInt()
                            repeat(colorCount) {
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
            repeat(extraData.getLInt()) {
                canPlaceOn.add(extraData.get(extraData.getLShort()))
            }

            val canDestroy = mutableListOf<String>()
            repeat(extraData.getLInt()) {
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
        putLFloat(vector.x.toFloat())
        putLFloat(vector.y.toFloat())
        putLFloat(vector.z.toFloat())
    }

    fun readGameRule(type: Int, playerModifiable: Boolean): GameRule = when (type) {
        GameRuleType.BOOL -> BooleanGameRule.decode(this, playerModifiable)
        GameRuleType.INT -> IntGameRule.decode(this, playerModifiable)
        GameRuleType.FLOAT -> FloatGameRule.decode(this, playerModifiable)
        else -> throw PacketDecodeException("Unknown gamerule type $type")
    }

    fun getGameRules(): Map<String, GameRule> {
        val count = getUnsignedVarInt()
        val rules = HashObjObjMaps.newMutableMap<String, GameRule>()
        repeat(count) {
            val name = getString()
            val type = getUnsignedVarInt()
            val playerModifiable = getBoolean()
            rules[name] = readGameRule(type, playerModifiable)
        }
        return rules
    }

    fun putGameRules(gameRules: Map<String, GameRule>) {
        putUnsignedVarInt(gameRules.size)
        gameRules.forEach { (name, gameRule) ->
            putString(name)
            putUnsignedVarInt(gameRule.typeId)
            putBoolean(gameRule.playerModifiable)
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
            return LittleEndianNbtSerializer().read(buffer.toString(), offset, 512)
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

    fun getRecipeIngredient(): RecipeIngredient {
        val id = getVarInt()
        if (id == 0) {
            return RecipeIngredient(0, 0, 0)
        }

        val meta = getVarInt()
        val count = getVarInt()

        return RecipeIngredient(id, meta, count)
    }

    fun putRecipeIngredient(ingredient: RecipeIngredient) {
        if (ingredient.id == 0) {
            putVarInt(0)
        } else {
            putVarInt(ingredient.id)
            putVarInt(ingredient.meta)
            putVarInt(ingredient.count)
        }
    }

    fun getEntityMetadataProperty(): MutableMap<Int, MetadataProperty> {
        val properties: MutableMap<Int, MetadataProperty> = HashIntObjMaps.newMutableMap()
        repeat(getUnsignedVarInt()) {
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

    fun getStructureSettings(): StructureSettings = StructureSettings().apply {
        paletteName = getString()
        ignoreEntities = getBoolean()
        ignoreBlocks = getBoolean()
        val structureSize = getBlockPosition()
        structureSizeX = structureSize.x
        structureSizeY = structureSize.y
        structureSizeZ = structureSize.z
        val structureOffset = getBlockPosition()
        structureOffsetX = structureOffset.x
        structureOffsetY = structureOffset.y
        structureOffsetZ = structureOffset.z
        lastTouchedByPlayerID = getEntityUniqueId()
        rotation = getByte()
        mirror = getByte()
        integrityValue = getFloat()
        integritySeed = getInt()
        pivot = getVector3()
    }

    fun putStructureSettings(structureSettings: StructureSettings) {
        putString(structureSettings.paletteName)

        putBoolean(structureSettings.ignoreEntities)
        putBoolean(structureSettings.ignoreBlocks)

        putBlockPosition(
            structureSettings.structureSizeX,
            structureSettings.structureSizeY,
            structureSettings.structureSizeZ
        )
        putBlockPosition(
            structureSettings.structureOffsetX,
            structureSettings.structureOffsetY,
            structureSettings.structureOffsetZ
        )

        putEntityUniqueId(structureSettings.lastTouchedByPlayerID)
        putByte(structureSettings.rotation)
        putByte(structureSettings.mirror)
        putFloat(structureSettings.integrityValue)
        putInt(structureSettings.integritySeed)
        putVector3(structureSettings.pivot)
    }

    fun getStructureEditorData(): StructureEditorData = StructureEditorData().apply {
        structureName = getString()
        structureDataField = getString()

        includePlayers = getBoolean()
        showBoundingBox = getBoolean()

        structureBlockType = getVarInt()
        structureSettings = getStructureSettings()
        structureRedstoneSaveMove = getVarInt()
    }

    fun putStructureEditorData(structureEditorData: StructureEditorData) {
        putString(structureEditorData.structureName)
        putString(structureEditorData.structureDataField)

        putBoolean(structureEditorData.includePlayers)
        putBoolean(structureEditorData.showBoundingBox)

        putVarInt(structureEditorData.structureBlockType)
        putStructureSettings(structureEditorData.structureSettings)
        putVarInt(structureEditorData.structureRedstoneSaveMove)
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
        repeat(getUnsignedVarInt()) {
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

    fun readGenericTypeNetworkId(): Int {
        return getVarInt()
    }

    fun writeGenericTypeNetworkId(id: Int) {
        putVarInt(id)
    }

    data class BlockPosition @JvmOverloads constructor(var x: Int = 0, var y: Int = 0, var z: Int = 0)
}
