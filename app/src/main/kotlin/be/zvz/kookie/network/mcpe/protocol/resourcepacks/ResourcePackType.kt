package be.zvz.kookie.network.mcpe.protocol.resourcepacks

enum class ResourcePackType(val value: Int) {
    INVALID(0),
    ADDON(1),
    CACHED(2),
    COPY_PROTECTED(3),
    BEHAVIORS(4),
    PERSONA_PIECE(5),
    RESOURCES(6),
    SKINS(7),
    WORLD_TEMPLATE(8);

    companion object {
        private val VALUES = values()
        @JvmStatic
        fun from(findValue: Int) = VALUES.first { it.value == findValue }
    }
}
