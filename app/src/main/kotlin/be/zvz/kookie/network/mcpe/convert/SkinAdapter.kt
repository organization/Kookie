package be.zvz.kookie.network.mcpe.convert

import be.zvz.kookie.entity.Skin
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinData

interface SkinAdapter {

    fun toSkinData(skin: Skin): SkinData

    fun fromSkinData(data: SkinData): Skin
}
