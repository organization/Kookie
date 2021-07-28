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
package be.zvz.kookie.item

enum class VanillaItems(val id: Int, val meta: Int = 0) {
    ACACIA_BOAT(333, 4),
    APPLE(260),
    ARROW(262),
    AWKWARD_POTION(373, 4),
    AWKWARD_SPLASH_POTION(438, 4),
    BAKED_POTATO(393),
    BEETROOT(457),
    BEETROOT_SEEDS(458),
    BEETROOT_SOUP(459),
    BIRCH_BOAT(333, 2),
    BLACK_BED(355, 15),
    BLACK_DYE(351, 16),
    BLAZE_POWDER(377),
    BLAZE_ROD(369),
    BLEACH(451),
    BLUE_BED(355, 11),
    BLUE_DYE(351, 18),
    BONE(352),
    BONE_MEAL(351, 15),
    BOOK(340),
    BOW(261),
    BOWL(281),
    BREAD(297),
    BRICK(336),
    BROWN_BED(355, 12),
    BROWN_DYE(351, 17),
    BUCKET(325),
    CARROT(391),
    CHAINMAIL_BOOTS(305),
    CHAINMAIL_CHESTPLATE(303),
    CHAINMAIL_HELMET(302),
    CHAINMAIL_LEGGINGS(304),
    CHARCOAL(263, 1),
    CHEMICAL_ALUMINIUM_OXIDE(499, 13),
    CHEMICAL_AMMONIA(499, 36),
    CHEMICAL_BARIUM_SULPHATE(499, 20),
    CHEMICAL_BENZENE(499, 33),
    CHEMICAL_BORON_TRIOXIDE(499, 14),
    CHEMICAL_CALCIUM_BROMIDE(499, 7),
    CHEMICAL_CALCIUM_CHLORIDE(499, 25),
    CHEMICAL_CERIUM_CHLORIDE(499, 23),
    CHEMICAL_CHARCOAL(499, 11),
    CHEMICAL_CRUDE_OIL(499, 29),
    CHEMICAL_GLUE(499, 27),
    CHEMICAL_HYDROGEN_PEROXIDE(499, 35),
    CHEMICAL_HYPOCHLORITE(499, 28),
    CHEMICAL_INK(499, 34),
    CHEMICAL_IRON_SULPHIDE(499, 4),
    CHEMICAL_LATEX(499, 30),
    CHEMICAL_LITHIUM_HYDRIDE(499, 5),
    CHEMICAL_LUMINOL(499, 10),
    CHEMICAL_MAGNESIUM_NITRATE(499, 3),
    CHEMICAL_MAGNESIUM_OXIDE(499, 8),
    CHEMICAL_MAGNESIUM_SALTS(499, 18),
    CHEMICAL_MERCURIC_CHLORIDE(499, 22),
    CHEMICAL_POLYETHYLENE(499, 16),
    CHEMICAL_POTASSIUM_CHLORIDE(499, 21),
    CHEMICAL_POTASSIUM_IODIDE(499, 31),
    CHEMICAL_RUBBISH(499, 17),
    CHEMICAL_SALT(499),
    CHEMICAL_SOAP(499, 15),
    CHEMICAL_SODIUM_ACETATE(499, 9),
    CHEMICAL_SODIUM_FLUORIDE(499, 32),
    CHEMICAL_SODIUM_HYDRIDE(499, 6),
    CHEMICAL_SODIUM_HYDROXIDE(499, 2),
    CHEMICAL_SODIUM_HYPOCHLORITE(499, 37),
    CHEMICAL_SODIUM_OXIDE(499, 1),
    CHEMICAL_SUGAR(499, 12),
    CHEMICAL_SULPHATE(499, 19),
    CHEMICAL_TUNGSTEN_CHLORIDE(499, 24),
    CHEMICAL_WATER(499, 26),
    CHORUS_FRUIT(432),
    CLAY(337),
    CLOCK(347),
    CLOWNFISH(461),
    COAL(263),
    COCOA_BEANS(351, 3),
    COMPASS(345),
    COOKED_CHICKEN(366),
    COOKED_FISH(350),
    COOKED_MUTTON(424),
    COOKED_PORKCHOP(320),
    COOKED_RABBIT(412),
    COOKED_SALMON(463),
    COOKIE(357),
    CREEPER_HEAD(397, 4),
    CYAN_BED(355, 9),
    CYAN_DYE(351, 6),
    DARK_OAK_BOAT(333, 5),
    DIAMOND(264),
    DIAMOND_AXE(279),
    DIAMOND_BOOTS(313),
    DIAMOND_CHESTPLATE(311),
    DIAMOND_HELMET(310),
    DIAMOND_HOE(293),
    DIAMOND_LEGGINGS(312),
    DIAMOND_PICKAXE(278),
    DIAMOND_SHOVEL(277),
    DIAMOND_SWORD(276),
    DRAGON_BREATH(437),
    DRAGON_HEAD(397, 5),
    DRIED_KELP(464),
    EGG(344),
    EMERALD(388),
    ENCHANTED_GOLDEN_APPLE(466),
    ENDER_PEARL(368),
    EXPERIENCE_BOTTLE(384),
    FEATHER(288),
    FERMENTED_SPIDER_EYE(376),
    FIRE_RESISTANCE_POTION(373, 12),
    FIRE_RESISTANCE_SPLASH_POTION(438, 12),
    FISHING_ROD(346),
    FLINT(318),
    FLINT_AND_STEEL(259),
    GHAST_TEAR(370),
    GLASS_BOTTLE(374),
    GLISTERING_MELON(382),
    GLOWSTONE_DUST(348),
    GOLD_INGOT(266),
    GOLD_NUGGET(371),
    GOLDEN_APPLE(322),
    GOLDEN_AXE(286),
    GOLDEN_BOOTS(317),
    GOLDEN_CARROT(396),
    GOLDEN_CHESTPLATE(315),
    GOLDEN_HELMET(314),
    GOLDEN_HOE(294),
    GOLDEN_LEGGINGS(316),
    GOLDEN_PICKAXE(285),
    GOLDEN_SHOVEL(284),
    GOLDEN_SWORD(283),
    GRAY_BED(355, 7),
    GRAY_DYE(351, 8),
    GREEN_BED(355, 13),
    GREEN_DYE(351, 2),
    GUNPOWDER(289),
    HARMING_POTION(373, 23),
    HARMING_SPLASH_POTION(438, 23),
    HEALING_POTION(373, 21),
    HEALING_SPLASH_POTION(438, 21),
    HEART_OF_THE_SEA(467),
    INK_SAC(351),
    INVISIBILITY_POTION(373, 7),
    INVISIBILITY_SPLASH_POTION(438, 7),
    IRON_AXE(258),
    IRON_BOOTS(309),
    IRON_CHESTPLATE(307),
    IRON_HELMET(306),
    IRON_HOE(292),
    IRON_INGOT(265),
    IRON_LEGGINGS(308),
    IRON_NUGGET(452),
    IRON_PICKAXE(257),
    IRON_SHOVEL(256),
    IRON_SWORD(267),
    JUNGLE_BOAT(333, 3),
    LAPIS_LAZULI(351, 4),
    LAVA_BUCKET(325, 10),
    LEAPING_POTION(373, 9),
    LEAPING_SPLASH_POTION(438, 9),
    LEATHER(334),
    LEATHER_BOOTS(301),
    LEATHER_CAP(298),
    LEATHER_PANTS(300),
    LEATHER_TUNIC(299),
    LIGHT_BLUE_BED(355, 3),
    LIGHT_BLUE_DYE(351, 12),
    LIGHT_GRAY_BED(355, 8),
    LIGHT_GRAY_DYE(351, 7),
    LIME_BED(355, 5),
    LIME_DYE(351, 10),
    LONG_FIRE_RESISTANCE_POTION(373, 13),
    LONG_FIRE_RESISTANCE_SPLASH_POTION(438, 13),
    LONG_INVISIBILITY_POTION(373, 8),
    LONG_INVISIBILITY_SPLASH_POTION(438, 8),
    LONG_LEAPING_POTION(373, 10),
    LONG_LEAPING_SPLASH_POTION(438, 10),
    LONG_MUNDANE_POTION(373, 2),
    LONG_MUNDANE_SPLASH_POTION(438, 2),
    LONG_NIGHT_VISION_POTION(373, 6),
    LONG_NIGHT_VISION_SPLASH_POTION(438, 6),
    LONG_POISON_POTION(373, 26),
    LONG_POISON_SPLASH_POTION(438, 26),
    LONG_REGENERATION_POTION(373, 29),
    LONG_REGENERATION_SPLASH_POTION(438, 29),
    LONG_SLOW_FALLING_POTION(373, 41),
    LONG_SLOW_FALLING_SPLASH_POTION(438, 41),
    LONG_SLOWNESS_POTION(373, 18),
    LONG_SLOWNESS_SPLASH_POTION(438, 18),
    LONG_STRENGTH_POTION(373, 32),
    LONG_STRENGTH_SPLASH_POTION(438, 32),
    LONG_SWIFTNESS_POTION(373, 15),
    LONG_SWIFTNESS_SPLASH_POTION(438, 15),
    LONG_TURTLE_MASTER_POTION(373, 38),
    LONG_TURTLE_MASTER_SPLASH_POTION(438, 38),
    LONG_WATER_BREATHING_POTION(373, 20),
    LONG_WATER_BREATHING_SPLASH_POTION(438, 20),
    LONG_WEAKNESS_POTION(373, 35),
    LONG_WEAKNESS_SPLASH_POTION(438, 35),
    MAGENTA_BED(355, 2),
    MAGENTA_DYE(351, 13),
    MAGMA_CREAM(378),
    MELON(360),
    MELON_SEEDS(362),
    MILK_BUCKET(325, 1),
    MINECART(328),
    MUNDANE_POTION(373, 1),
    MUNDANE_SPLASH_POTION(438, 1),
    MUSHROOM_STEW(282),
    NAUTILUS_SHELL(465),
    NETHER_BRICK(405),
    NETHER_QUARTZ(406),
    NETHER_STAR(399),
    NIGHT_VISION_POTION(373, 5),
    NIGHT_VISION_SPLASH_POTION(438, 5),
    OAK_BOAT(333),
    ORANGE_BED(355, 1),
    ORANGE_DYE(351, 14),
    PAINTING(321),
    PAPER(339),
    PINK_BED(355, 6),
    PINK_DYE(351, 9),
    PLAYER_HEAD(397, 3),
    POISON_POTION(373, 25),
    POISON_SPLASH_POTION(438, 25),
    POISONOUS_POTATO(394),
    POPPED_CHORUS_FRUIT(433),
    POTATO(392),
    PRISMARINE_CRYSTALS(422),
    PRISMARINE_SHARD(409),
    PUFFERFISH(462),
    PUMPKIN_PIE(400),
    PUMPKIN_SEEDS(361),
    PURPLE_BED(355, 10),
    PURPLE_DYE(351, 5),
    RABBIT_FOOT(414),
    RABBIT_HIDE(415),
    RABBIT_STEW(413),
    RAW_BEEF(363),
    RAW_CHICKEN(365),
    RAW_FISH(349),
    RAW_MUTTON(423),
    RAW_PORKCHOP(319),
    RAW_RABBIT(411),
    RAW_SALMON(460),
    RECORD_11(510),
    RECORD_13(500),
    RECORD_BLOCKS(502),
    RECORD_CAT(501),
    RECORD_CHIRP(503),
    RECORD_FAR(504),
    RECORD_MALL(505),
    RECORD_MELLOHI(506),
    RECORD_STAL(507),
    RECORD_STRAD(508),
    RECORD_WAIT(511),
    RECORD_WARD(509),
    RED_BED(355, 14),
    RED_DYE(351, 1),
    REDSTONE_DUST(331),
    REGENERATION_POTION(373, 28),
    REGENERATION_SPLASH_POTION(438, 28),
    ROTTEN_FLESH(367),
    SCUTE(468),
    SHEARS(359),
    SHULKER_SHELL(445),
    SKELETON_SKULL(397),
    SLIMEBALL(341),
    SLOW_FALLING_POTION(373, 40),
    SLOW_FALLING_SPLASH_POTION(438, 40),
    SLOWNESS_POTION(373, 17),
    SLOWNESS_SPLASH_POTION(438, 17),
    SNOWBALL(332),
    SPIDER_EYE(375),
    SPRUCE_BOAT(333, 1),
    SQUID_SPAWN_EGG(383, 17),
    STEAK(364),
    STICK(280),
    STONE_AXE(275),
    STONE_HOE(291),
    STONE_PICKAXE(274),
    STONE_SHOVEL(273),
    STONE_SWORD(272),
    STRENGTH_POTION(373, 31),
    STRENGTH_SPLASH_POTION(438, 31),
    STRING(287),
    STRONG_HARMING_POTION(373, 24),
    STRONG_HARMING_SPLASH_POTION(438, 24),
    STRONG_HEALING_POTION(373, 22),
    STRONG_HEALING_SPLASH_POTION(438, 22),
    STRONG_LEAPING_POTION(373, 11),
    STRONG_LEAPING_SPLASH_POTION(438, 11),
    STRONG_POISON_POTION(373, 27),
    STRONG_POISON_SPLASH_POTION(438, 27),
    STRONG_REGENERATION_POTION(373, 30),
    STRONG_REGENERATION_SPLASH_POTION(438, 30),
    STRONG_STRENGTH_POTION(373, 33),
    STRONG_STRENGTH_SPLASH_POTION(438, 33),
    STRONG_SWIFTNESS_POTION(373, 16),
    STRONG_SWIFTNESS_SPLASH_POTION(438, 16),
    STRONG_TURTLE_MASTER_POTION(373, 39),
    STRONG_TURTLE_MASTER_SPLASH_POTION(438, 39),
    SUGAR(353),
    SWIFTNESS_POTION(373, 14),
    SWIFTNESS_SPLASH_POTION(438, 14),
    THICK_POTION(373, 3),
    THICK_SPLASH_POTION(438, 3),
    TOTEM(450),
    TURTLE_MASTER_POTION(373, 37),
    TURTLE_MASTER_SPLASH_POTION(438, 37),
    VILLAGER_SPAWN_EGG(383, 15),
    WATER_BREATHING_POTION(373, 19),
    WATER_BREATHING_SPLASH_POTION(438, 19),
    WATER_BUCKET(325, 8),
    WATER_POTION(373),
    WATER_SPLASH_POTION(438),
    WEAKNESS_POTION(373, 34),
    WEAKNESS_SPLASH_POTION(438, 34),
    WHEAT(296),
    WHEAT_SEEDS(295),
    WHITE_BED(355),
    WHITE_DYE(351, 19),
    WITHER_POTION(373, 36),
    WITHER_SKELETON_SKULL(397, 1),
    WITHER_SPLASH_POTION(438, 36),
    WOODEN_AXE(271),
    WOODEN_HOE(290),
    WOODEN_PICKAXE(270),
    WOODEN_SHOVEL(269),
    WOODEN_SWORD(268),
    WRITABLE_BOOK(386),
    WRITTEN_BOOK(387),
    YELLOW_BED(355, 4),
    YELLOW_DYE(351, 11),
    ZOMBIE_HEAD(397, 2),
    ZOMBIE_SPAWN_EGG(383, 32);

    val item: Item get() = ItemFactory.get(id, meta)

    companion object {
        @JvmStatic
        @JvmOverloads
        fun fromId(id: Int, meta: Int? = null) = values().firstOrNull { it.id == id && (meta == null || it.meta == meta) }
    }
}
