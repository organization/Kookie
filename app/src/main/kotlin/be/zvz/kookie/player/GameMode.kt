package be.zvz.kookie.player

enum class GameMode(val magicNumber: Int, val modeName: String, val transKey: String, val aliases: Array<String>) : IGameMode {
    SURVIVAL(0, "Survival", "gameMode.survival", arrayOf("s", "0")) {
        override fun id(): Int = this.magicNumber
        override fun getTranslationKey(): String = "%" + this.transKey
    },
    CREATIVE(1, "Creative", "gameMode.creative", arrayOf("c", "1")) {
        override fun id(): Int = this.magicNumber
        override fun getTranslationKey(): String = "%" + this.transKey
    },
    ADVENTURE(2, "Adventure", "gameMode.adventure", arrayOf("a", "2")) {
        override fun id(): Int = this.magicNumber
        override fun getTranslationKey(): String = "%" + this.transKey
    },
    SPECTATOR(3, "Spectator", "gameMode.spectator", arrayOf("v", "view", "3")) {
        override fun id(): Int = this.magicNumber
        override fun getTranslationKey(): String = "%" + this.transKey
    }
}
