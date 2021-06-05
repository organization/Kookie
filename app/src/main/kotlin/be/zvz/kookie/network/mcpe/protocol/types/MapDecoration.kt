package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.color.Color

data class MapDecoration(
    val icon: Int,
    val rotation: Int,
    val xOffset: Int,
    val yOffset: Int,
    val label: String,
    val color: Color
)
