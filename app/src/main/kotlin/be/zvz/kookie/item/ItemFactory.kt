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

import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.block.utils.RecordType
import be.zvz.kookie.block.utils.SkullType
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.inventory.ArmorInventory
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.world.World
import com.koloboke.collect.map.hash.HashIntObjMaps

object ItemFactory {
    val list: MutableMap<Int, Item> = HashIntObjMaps.newMutableMap()

    init {
        registerArmorItems()
        registerSpawnEggs()
        registerTierToolItems()

        register(Apple(VanillaItems.APPLE.identifier, "Apple"))
        register(Arrow(VanillaItems.ARROW.identifier, "Arrow"))

        register(BakedPotato(VanillaItems.BAKED_POTATO.identifier, "Baked Potato"))
        register(Bamboo(VanillaItems.BAMBOO.identifier, "Bamboo"), true)
        register(Beetroot(VanillaItems.BEETROOT.identifier, "Beetroot"))
        register(BeetrootSeeds(VanillaItems.BEETROOT_SEEDS.identifier, "Beetroot Seeds"))
        register(BeetrootSoup(VanillaItems.BEETROOT_SOUP.identifier, "Beetroot Soup"))
        register(BlazeRod(VanillaItems.BLAZE_ROD.identifier, "Blaze Rod"))
        register(Book(VanillaItems.BOOK.identifier, "Book"))
        register(Bow(VanillaItems.BOW.identifier, "Bow"))
        register(Bowl(VanillaItems.BOWL.identifier, "Bowl"))
        register(Bread(VanillaItems.BREAD.identifier, "Bread"))
        register(Bucket(VanillaItems.BUCKET.identifier, "Bucket"))
        register(Carrot(VanillaItems.CARROT.identifier, "Carrot"))
        register(ChorusFruit(VanillaItems.CHORUS_FRUIT.identifier, "Chorus Fruit"))
        register(Clock(VanillaItems.CLOCK.identifier, "Clock"))
        register(Clownfish(VanillaItems.CLOWNFISH.identifier, "Clownfish"))
        register(Coal(VanillaItems.COAL.identifier, "Coal"))

        // TODO: how?
        /*
        register(
            ItemBlockWallOrFloor(
                VanillaItems.CORAL_FAN.identifier,
                (VanillaBlocks.CORAL_FAN).setCoralType(CoralType.TUBE),
                VanillaBlocks.WALL_CORAL_FAN.setCoralType(CoralType.TUBE())
            ), true
        )
        register(
            ItemBlockWallOrFloor(
                VanillaItems.CORAL_FAN.identifier,
                VanillaBlocks.CORAL_FAN.setCoralType(CoralType.BRAIN),
                VanillaBlocks.WALL_CORAL_FAN.setCoralType(CoralType.BRAIN())
            ), true
        )
        register(
            ItemBlockWallOrFloor(
                VanillaItems.CORAL_FAN.identifier,
                VanillaBlocks.CORAL_FAN.setCoralType(CoralType.BUBBLE),
                VanillaBlocks.WALL_CORAL_FAN.setCoralType(CoralType.BUBBLE())
            ), true
        )
        register(
            ItemBlockWallOrFloor(
                VanillaItems.CORAL_FAN.identifier,
                VanillaBlocks.CORAL_FAN.setCoralType(CoralType.FIRE),
                VanillaBlocks.WALL_CORAL_FAN.setCoralType(CoralType.FIRE())
            ), true
        )
        register(
            ItemBlockWallOrFloor(
                VanillaItems.CORAL_FAN.identifier,
                VanillaBlocks.CORAL_FAN.setCoralType(CoralType.HORN),
                VanillaBlocks.WALL_CORAL_FAN.setCoralType(CoralType.HORN())
            ), true
        )
        */
        register(Coal(VanillaItems.CHARCOAL.identifier, "Charcoal"))
        register(CocoaBeans(VanillaItems.COCOA_BEANS.identifier, "Cocoa Beans"))
        register(Compass(VanillaItems.COMPASS.identifier, "Compass"))
        register(CookedChicken(VanillaItems.COOKED_CHICKEN.identifier, "Cooked Chicken"))
        register(CookedFish(VanillaItems.COOKED_FISH.identifier, "Cooked Fish"))
        register(CookedMutton(VanillaItems.COOKED_MUTTON.identifier, "Cooked Mutton"))
        register(CookedPorkchop(VanillaItems.COOKED_PORKCHOP.identifier, "Cooked Porkchop"))
        register(CookedRabbit(VanillaItems.COOKED_RABBIT.identifier, "Cooked Rabbit"))
        register(CookedSalmon(VanillaItems.COOKED_SALMON.identifier, "Cooked Salmon"))
        register(Cookie(VanillaItems.COOKIE.identifier, "Cookie"))
        register(DriedKelp(VanillaItems.DRIED_KELP.identifier, "Dried Kelp"))
        register(Egg(VanillaItems.EGG.identifier, "Egg"))
        register(EnderPearl(VanillaItems.ENDER_PEARL.identifier, "Ender Pearl"))
        register(ExperienceBottle(VanillaItems.BOTTLE_O_ENCHANTING.identifier, "Bottle o' Enchanting"))
        register(Fertilizer(VanillaItems.BONE_MEAL.identifier, "Bone Meal"))
        register(FishingRod(VanillaItems.FISHING_ROD.identifier, "Fishing Rod"))
        register(FlintSteel(VanillaItems.FLINT_AND_STEEL.identifier, "Flint and Steel"))
        register(GlassBottle(VanillaItems.GLASS_BOTTLE.identifier, "Glass Bottle"))
        register(GoldenApple(VanillaItems.GOLDEN_APPLE.identifier, "Golden Apple"))
        register(GoldenAppleEnchanted(VanillaItems.ENCHANTED_GOLDEN_APPLE.identifier, "Enchanted Golden Apple"))
        register(GoldenCarrot(VanillaItems.GOLDEN_CARROT.identifier, "Golden Carrot"))
        register(Item(VanillaItems.BLAZE_POWDER.identifier, "Blaze Powder"))
        register(Item(VanillaItems.BLEACH.identifier, "Bleach")) // EDU

        register(Item(VanillaItems.BONE.identifier, "Bone"))
        register(Item(VanillaItems.BRICK.identifier, "Brick"))
        register(Item(VanillaItems.POPPED_CHORUS_FRUIT.identifier, "Popped Chorus Fruit"))
        register(Item(VanillaItems.CLAY.identifier, "Clay"))
        register(Item(VanillaItems.CHEMICAL_SALT.identifier, "Salt"))
        register(Item(VanillaItems.CHEMICAL_SODIUM_OXIDE.identifier, "Sodium Oxide"))
        register(Item(VanillaItems.CHEMICAL_SODIUM_HYDROXIDE.identifier, "Sodium Hydroxide"))
        register(Item(VanillaItems.CHEMICAL_MAGNESIUM_NITRATE.identifier, "Magnesium Nitrate"))
        register(Item(VanillaItems.CHEMICAL_IRON_SULPHIDE.identifier, "Iron Sulphide"))
        register(Item(VanillaItems.CHEMICAL_LITHIUM_HYDRIDE.identifier, "Lithium Hydride"))
        register(Item(VanillaItems.CHEMICAL_SODIUM_HYDRIDE.identifier, "Sodium Hydride"))
        register(Item(VanillaItems.CHEMICAL_CALCIUM_BROMIDE.identifier, "Calcium Bromide"))
        register(Item(VanillaItems.CHEMICAL_MAGNESIUM_OXIDE.identifier, "Magnesium Oxide"))
        register(Item(VanillaItems.CHEMICAL_SODIUM_ACETATE.identifier, "Sodium Acetate"))
        register(Item(VanillaItems.CHEMICAL_LUMINOL.identifier, "Luminol"))
        register(Item(VanillaItems.CHEMICAL_CHARCOAL.identifier, "Charcoal"))
        register(Item(VanillaItems.CHEMICAL_SUGAR.identifier, "Sugar"))
        register(Item(VanillaItems.CHEMICAL_ALUMINIUM_OXIDE.identifier, "Aluminium Oxide"))
        register(Item(VanillaItems.CHEMICAL_BORON_TRIOXIDE.identifier, "Boron Trioxide"))
        register(Item(VanillaItems.CHEMICAL_SOAP.identifier, "Soap"))
        register(Item(VanillaItems.CHEMICAL_POLYETHYLENE.identifier, "Polyethylene"))
        register(Item(VanillaItems.CHEMICAL_RUBBISH.identifier, "Rubbish"))
        register(Item(VanillaItems.CHEMICAL_MAGNESIUM_SALTS.identifier, "Magnesium Salts"))
        register(Item(VanillaItems.CHEMICAL_SULPHATE.identifier, "Sulphate"))
        register(Item(VanillaItems.CHEMICAL_BARIUM_SULPHATE.identifier, "Barium Sulphate"))
        register(Item(VanillaItems.CHEMICAL_POTASSIUM_CHLORIDE.identifier, "Potassium Chloride"))
        register(Item(VanillaItems.CHEMICAL_MERCURIC_CHLORIDE.identifier, "Mercuric Chloride"))
        register(Item(VanillaItems.CHEMICAL_CERIUM_CHLORIDE.identifier, "Cerium Chloride"))
        register(Item(VanillaItems.CHEMICAL_TUNGSTEN_CHLORIDE.identifier, "Tungsten Chloride"))
        register(Item(VanillaItems.CHEMICAL_CALCIUM_CHLORIDE.identifier, "Calcium Chloride"))
        register(Item(VanillaItems.CHEMICAL_WATER.identifier, "Water")) // ???
        register(Item(VanillaItems.CHEMICAL_GLUE.identifier, "Glue"))
        register(Item(VanillaItems.CHEMICAL_HYPOCHLORITE.identifier, "Hypochlorite"))
        register(Item(VanillaItems.CHEMICAL_CRUDE_OIL.identifier, "Crude Oil"))
        register(Item(VanillaItems.CHEMICAL_LATEX.identifier, "Latex"))
        register(Item(VanillaItems.CHEMICAL_POTASSIUM_IODIDE.identifier, "Potassium Iodide"))
        register(Item(VanillaItems.CHEMICAL_SODIUM_FLUORIDE.identifier, "Sodium Fluoride"))
        register(Item(VanillaItems.CHEMICAL_BENZENE.identifier, "Benzene"))
        register(Item(VanillaItems.CHEMICAL_INK.identifier, "Ink"))
        register(Item(VanillaItems.CHEMICAL_HYDROGEN_PEROXIDE.identifier, "Hydrogen Peroxide"))
        register(Item(VanillaItems.CHEMICAL_AMMONIA.identifier, "Ammonia"))
        register(Item(VanillaItems.CHEMICAL_SODIUM_HYPOCHLORITE.identifier, "Sodium Hypochlorite"))
        register(Item(VanillaItems.DIAMOND.identifier, "Diamond"))
        register(Item(VanillaItems.DRAGON_BREATH.identifier, "Dragon's Breath"))
        register(Item(VanillaItems.DYE.identifier, "Ink Sac"))
        register(Item(VanillaItems.LAPIS_LAZULI.identifier, "Lapis Lazuli"))
        register(Item(VanillaItems.EMERALD.identifier, "Emerald"))
        register(Item(VanillaItems.FEATHER.identifier, "Feather"))
        register(Item(VanillaItems.FERMENTED_SPIDER_EYE.identifier, "Fermented Spider Eye"))
        register(Item(VanillaItems.FLINT.identifier, "Flint"))
        register(Item(VanillaItems.GHAST_TEAR.identifier, "Ghast Tear"))
        register(Item(VanillaItems.GLISTERING_MELON_SLICE.identifier, "Glistering Melon"))
        register(Item(VanillaItems.GLOWSTONE_DUST.identifier, "Glowstone Dust"))
        register(Item(VanillaItems.GOLD_INGOT.identifier, "Gold Ingot"))
        register(Item(VanillaItems.GOLD_NUGGET.identifier, "Gold Nugget"))
        register(Item(VanillaItems.GUNPOWDER.identifier, "Gunpowder"))
        register(Item(VanillaItems.HEART_OF_THE_SEA.identifier, "Heart of the Sea"))
        register(Item(VanillaItems.IRON_INGOT.identifier, "Iron Ingot"))
        register(Item(VanillaItems.IRON_NUGGET.identifier, "Iron Nugget"))
        register(Item(VanillaItems.LEATHER.identifier, "Leather"))
        register(Item(VanillaItems.MAGMA_CREAM.identifier, "Magma Cream"))
        register(Item(VanillaItems.NAUTILUS_SHELL.identifier, "Nautilus Shell"))
        register(Item(VanillaItems.NETHER_BRICK.identifier, "Nether Brick"))
        register(Item(VanillaItems.NETHER_QUARTZ.identifier, "Nether Quartz"))
        register(Item(VanillaItems.NETHER_STAR.identifier, "Nether Star"))
        register(Item(VanillaItems.PAPER.identifier, "Paper"))
        register(Item(VanillaItems.PRISMARINE_CRYSTALS.identifier, "Prismarine Crystals"))
        register(Item(VanillaItems.PRISMARINE_SHARD.identifier, "Prismarine Shard"))
        register(Item(VanillaItems.RABBIT_FOOT.identifier, "Rabbit's Foot"))
        register(Item(VanillaItems.RABBIT_HIDE.identifier, "Rabbit Hide"))
        register(Item(VanillaItems.SHULKER_SHELL.identifier, "Shulker Shell"))
        register(Item(VanillaItems.SLIME_BALL.identifier, "Slimeball"))
        register(Item(VanillaItems.SUGAR.identifier, "Sugar"))
        register(Item(VanillaItems.SCUTE.identifier, "Scute"))
        register(Item(VanillaItems.WHEAT.identifier, "Wheat"))
        register(ItemBlock(VanillaItems.ACACIA_DOOR.identifier, VanillaBlocks.ACACIA_DOOR.block))
        register(ItemBlock(VanillaItems.BIRCH_DOOR.identifier, VanillaBlocks.BIRCH_DOOR.block))
        register(ItemBlock(VanillaItems.BREWING_STAND.identifier, VanillaBlocks.BREWING_STAND.block))
        register(ItemBlock(VanillaItems.CAKE.identifier, VanillaBlocks.CAKE.block))
        register(ItemBlock(VanillaItems.COMPARATOR.identifier, VanillaBlocks.UNPOWERED_COMPARATOR.block))
        register(ItemBlock(VanillaItems.DARK_OAK_DOOR.identifier, VanillaBlocks.DARK_OAK_DOOR.block))
        register(ItemBlock(VanillaItems.FLOWER_POT.identifier, VanillaBlocks.FLOWER_POT.block))
        register(ItemBlock(VanillaItems.HOPPER.identifier, VanillaBlocks.HOPPER.block))
        register(ItemBlock(VanillaItems.IRON_DOOR.identifier, VanillaBlocks.IRON_DOOR.block))
        register(ItemBlock(VanillaItems.ITEM_FRAME.identifier, VanillaBlocks.ITEM_FRAME.block))
        register(ItemBlock(VanillaItems.JUNGLE_DOOR.identifier, VanillaBlocks.JUNGLE_DOOR.block))
        register(ItemBlock(VanillaItems.NETHER_WART.identifier, VanillaBlocks.NETHER_WART.block))
        register(ItemBlock(VanillaItems.OAK_DOOR.identifier, VanillaBlocks.OAK_DOOR.block))
        register(ItemBlock(VanillaItems.REPEATER.identifier, VanillaBlocks.UNPOWERED_REPEATER.block))
        register(ItemBlock(VanillaItems.SPRUCE_DOOR.identifier, VanillaBlocks.SPRUCE_DOOR.block))
        register(ItemBlock(VanillaItems.SUGARCANE.identifier, VanillaBlocks.SUGAR_CANE.block))

        // the meta values for buckets are intentionally hardcoded because block IDs will change in the future
        val waterBucket =
            LiquidBucket(
                VanillaItems.BUCKET.identifier,
                "Water Bucket"
            ) // TODO: Liquid block (VanillaBlocks.WATER.block)
        register(waterBucket)
        remap(VanillaItems.BUCKET.identifier, waterBucket)
        val lavaBucket =
            LiquidBucket(
                VanillaItems.BUCKET.identifier,
                "Lava Bucket"
            ) // TODO: Liquid block (VanillaBlocks.LAVA.block)
        register(lavaBucket)
        remap(VanillaItems.LAVA_BUCKET.identifier, lavaBucket)
        register(Melon(VanillaItems.MELON.identifier, "Melon"))
        register(MelonSeeds(VanillaItems.MELON_SEEDS.identifier, "Melon Seeds"))
        register(MilkBucket(VanillaItems.BUCKET.identifier, "Milk Bucket"))
        register(Minecart(VanillaItems.MINECART.identifier, "Minecart"))
        register(MushroomStew(VanillaItems.MUSHROOM_STEW.identifier, "Mushroom Stew"))
        register(PaintingItem(VanillaItems.PAINTING.identifier, "Painting"))
        register(PoisonousPotato(VanillaItems.POISONOUS_POTATO.identifier, "Poisonous Potato"))
        register(Potato(VanillaItems.POTATO.identifier, "Potato"))
        register(Pufferfish(VanillaItems.PUFFERFISH.identifier, "Pufferfish"))
        register(PumpkinPie(VanillaItems.PUMPKIN_PIE.identifier, "Pumpkin Pie"))
        register(PumpkinSeeds(VanillaItems.PUMPKIN_SEEDS.identifier, "Pumpkin Seeds"))
        register(RabbitStew(VanillaItems.RABBIT_STEW.identifier, "Rabbit Stew"))
        register(RawBeef(VanillaItems.RAW_BEEF.identifier, "Raw Beef"))
        register(RawChicken(VanillaItems.RAW_CHICKEN.identifier, "Raw Chicken"))
        register(RawFish(VanillaItems.RAW_FISH.identifier, "Raw Fish"))
        register(RawMutton(VanillaItems.RAW_MUTTON.identifier, "Raw Mutton"))
        register(RawPorkchop(VanillaItems.RAW_PORKCHOP.identifier, "Raw Porkchop"))
        register(RawRabbit(VanillaItems.RAW_RABBIT.identifier, "Raw Rabbit"))
        register(RawSalmon(VanillaItems.RAW_SALMON.identifier, "Raw Salmon"))
        register(Record(VanillaItems.RECORD_13.identifier, RecordType.DISK_13, "Record 13"))
        register(Record(VanillaItems.RECORD_CAT.identifier, RecordType.DISK_CAT, "Record Cat"))
        register(Record(VanillaItems.RECORD_BLOCKS.identifier, RecordType.DISK_BLOCKS, "Record Blocks"))
        register(Record(VanillaItems.RECORD_CHIRP.identifier, RecordType.DISK_CHIRP, "Record Chirp"))
        register(Record(VanillaItems.RECORD_FAR.identifier, RecordType.DISK_FAR, "Record Far"))
        register(Record(VanillaItems.RECORD_MALL.identifier, RecordType.DISK_MALL, "Record Mall"))
        register(Record(VanillaItems.RECORD_MELLOHI.identifier, RecordType.DISK_MELLOHI, "Record Mellohi"))
        register(Record(VanillaItems.RECORD_STAL.identifier, RecordType.DISK_STAL, "Record Stal"))
        register(Record(VanillaItems.RECORD_STRAD.identifier, RecordType.DISK_STRAD, "Record Strad"))
        register(Record(VanillaItems.RECORD_WARD.identifier, RecordType.DISK_WARD, "Record Ward"))
        register(Record(VanillaItems.RECORD_11.identifier, RecordType.DISK_11, "Record 11"))
        register(Record(VanillaItems.RECORD_WAIT.identifier, RecordType.DISK_WAIT, "Record Wait"))
        register(Redstone(VanillaItems.REDSTONE.identifier, "Redstone"))
        register(RottenFlesh(VanillaItems.ROTTEN_FLESH.identifier, "Rotten Flesh"))
        register(Shears(VanillaItems.SHEARS.identifier, "Shears"))
        register(
            ItemBlockWallOrFloor(
                VanillaItems.SIGN.identifier,
                VanillaBlocks.OAK_STANDING_SIGN.block,
                VanillaBlocks.OAK_WALL_SIGN.block
            )
        )
        register(
            ItemBlockWallOrFloor(
                VanillaItems.SPRUCE_SIGN.identifier,
                VanillaBlocks.SPRUCE_STANDING_SIGN.block,
                VanillaBlocks.SPRUCE_WALL_SIGN.block
            )
        )
        register(
            ItemBlockWallOrFloor(
                VanillaItems.BIRCH_SIGN.identifier,
                VanillaBlocks.BIRCH_STANDING_SIGN.block,
                VanillaBlocks.BIRCH_WALL_SIGN.block
            )
        )
        register(
            ItemBlockWallOrFloor(
                VanillaItems.JUNGLE_SIGN.identifier,
                VanillaBlocks.JUNGLE_STANDING_SIGN.block,
                VanillaBlocks.JUNGLE_WALL_SIGN.block
            )
        )
        register(
            ItemBlockWallOrFloor(
                VanillaItems.ACACIA_SIGN.identifier,
                VanillaBlocks.ACACIA_STANDING_SIGN.block,
                VanillaBlocks.ACACIA_WALL_SIGN.block
            )
        )
        register(
            ItemBlockWallOrFloor(
                VanillaItems.DARKOAK_SIGN.identifier,
                VanillaBlocks.DARK_OAK_STANDING_SIGN.block,
                VanillaBlocks.DARK_OAK_WALL_SIGN.block
            )
        )
        register(Snowball(VanillaItems.SNOWBALL.identifier, "Snowball"))
        register(SpiderEye(VanillaItems.SPIDER_EYE.identifier, "Spider Eye"))
        register(Steak(VanillaItems.COOKED_BEEF.identifier, "Steak"))
        register(Stick(VanillaItems.STICK.identifier, "Stick"))
        register(StringItem(VanillaItems.STRING.identifier, "String"))
        register(Totem(VanillaItems.TOTEM.identifier, "Totem of Undying"))
        register(WheatSeeds(VanillaItems.WHEAT_SEEDS.identifier, "Wheat Seeds"))
        register(WritableBook(VanillaItems.WRITABLE_BOOK.identifier, "Book & Quill"))
        register(WrittenBook(VanillaItems.WRITTEN_BOOK.identifier, "Written Book"))

        SkullType.values().forEach {
            register(Skull(ItemIdentifier(VanillaItems.SKULL.id, it.magicNumber), it.displayName, it))
        }

        TODO("Add Items")
    }

    private fun registerSpawnEggs() {
        // TODO: the meta values should probably be hardcoded; they won't change, but the EntityLegacyIds might

        // TODO: Set meta from EntityLegacyIds
        register(object : SpawnEgg(VanillaItems.SPAWN_EGG.identifier, "Zombie Spawn Egg") {
            override fun createEntity(world: World, pos: Vector3, yaw: Float, pitch: Float): Entity {
                TODO("Not yet implemented")
            }
        })
        register(object : SpawnEgg(VanillaItems.SPAWN_EGG.identifier, "Squid Spawn Egg") {
            override fun createEntity(world: World, pos: Vector3, yaw: Float, pitch: Float): Entity {
                TODO("Not yet implemented")
            }
        })
        register(object : SpawnEgg(VanillaItems.SPAWN_EGG.identifier, "Villager Spawn Egg") {
            override fun createEntity(world: World, pos: Vector3, yaw: Float, pitch: Float): Entity {
                TODO("Not yet implemented")
            }
        })
    }

    private fun registerArmorItems() {
        val registerArmor = { id: Int, name: String, defencePoints: Int, maxDurability: Int, slot: Int ->
            register(Armor(ItemIdentifier(id, 0), name, ArmorTypeInfo(defencePoints, maxDurability, slot)))
        }

        registerArmor(VanillaItems.CHAINMAIL_BOOTS.id, "Chainmail Boots", 1, 196, ArmorInventory.SLOT_FEET)
        registerArmor(VanillaItems.DIAMOND_BOOTS.id, "Diamond Boots", 3, 430, ArmorInventory.SLOT_FEET)
        registerArmor(VanillaItems.GOLDEN_BOOTS.id, "Golden Boots", 1, 92, ArmorInventory.SLOT_FEET)
        registerArmor(VanillaItems.IRON_BOOTS.id, "Iron Boots", 2, 196, ArmorInventory.SLOT_FEET)
        registerArmor(VanillaItems.LEATHER_BOOTS.id, "Leather Boots", 1, 66, ArmorInventory.SLOT_FEET)
        registerArmor(VanillaItems.NETHERITE_BOOTS.id, "Netherite Boots", 3, 481, ArmorInventory.SLOT_FEET)

        registerArmor(VanillaItems.CHAINMAIL_CHESTPLATE.id, "Chainmail Chestplate", 5, 241, ArmorInventory.SLOT_CHEST)
        registerArmor(VanillaItems.DIAMOND_CHESTPLATE.id, "Diamond Chestplate", 8, 529, ArmorInventory.SLOT_CHEST)
        registerArmor(VanillaItems.GOLDEN_CHESTPLATE.id, "Golden Chestplate", 5, 113, ArmorInventory.SLOT_CHEST)
        registerArmor(VanillaItems.IRON_CHESTPLATE.id, "Iron Chestplate", 6, 241, ArmorInventory.SLOT_CHEST)
        registerArmor(VanillaItems.LEATHER_CHESTPLATE.id, "Leather Tunic", 3, 81, ArmorInventory.SLOT_CHEST)
        registerArmor(VanillaItems.NETHERITE_CHESTPLATE.id, "Netherite Chestplate", 8, 592, ArmorInventory.SLOT_CHEST)

        registerArmor(VanillaItems.CHAINMAIL_HELMET.id, "Chainmail Helmet", 2, 166, ArmorInventory.SLOT_HEAD)
        registerArmor(VanillaItems.DIAMOND_HELMET.id, "Diamond Helmet", 3, 364, ArmorInventory.SLOT_HEAD)
        registerArmor(VanillaItems.GOLDEN_HELMET.id, "Golden Helmet", 2, 78, ArmorInventory.SLOT_HEAD)
        registerArmor(VanillaItems.IRON_HELMET.id, "Iron Helmet", 2, 166, ArmorInventory.SLOT_HEAD)
        registerArmor(VanillaItems.LEATHER_HELMET.id, "Leather Cap", 1, 56, ArmorInventory.SLOT_HEAD)
        registerArmor(VanillaItems.NETHERITE_HELMET.id, "Netherite Helmet", 3, 407, ArmorInventory.SLOT_HEAD)

        registerArmor(VanillaItems.CHAINMAIL_LEGGINGS.id, "Chainmail Leggings", 4, 226, ArmorInventory.SLOT_LEGS)
        registerArmor(VanillaItems.DIAMOND_LEGGINGS.id, "Diamond Leggings", 6, 496, ArmorInventory.SLOT_LEGS)
        registerArmor(VanillaItems.GOLDEN_LEGGINGS.id, "Golden Leggings", 3, 106, ArmorInventory.SLOT_LEGS)
        registerArmor(VanillaItems.IRON_LEGGINGS.id, "Iron Leggings", 5, 226, ArmorInventory.SLOT_LEGS)
        registerArmor(VanillaItems.LEATHER_LEGGINGS.id, "Leather Pants", 2, 76, ArmorInventory.SLOT_LEGS)
        registerArmor(VanillaItems.NETHERITE_LEGGINGS.id, "Netherite Leggings", 6, 555, ArmorInventory.SLOT_LEGS)
    }

    private fun registerTierToolItems() {
        register(Axe(VanillaItems.NETHERITE_AXE.identifier, "Netherite Axe", ToolTier.NETHERITE))
        register(Axe(VanillaItems.DIAMOND_AXE.identifier, "Diamond Axe", ToolTier.DIAMOND))
        register(Axe(VanillaItems.GOLDEN_AXE.identifier, "Golden Axe", ToolTier.GOLD))
        register(Axe(VanillaItems.IRON_AXE.identifier, "Iron Axe", ToolTier.IRON))
        register(Axe(VanillaItems.STONE_AXE.identifier, "Stone Axe", ToolTier.STONE))
        register(Axe(VanillaItems.WOODEN_AXE.identifier, "Wooden Axe", ToolTier.WOOD))

        register(Hoe(VanillaItems.NETHERITE_HOE.identifier, "Netherite Hoe", ToolTier.NETHERITE))
        register(Hoe(VanillaItems.DIAMOND_HOE.identifier, "Diamond Hoe", ToolTier.DIAMOND))
        register(Hoe(VanillaItems.GOLDEN_HOE.identifier, "Golden Hoe", ToolTier.GOLD))
        register(Hoe(VanillaItems.IRON_HOE.identifier, "Iron Hoe", ToolTier.IRON))
        register(Hoe(VanillaItems.STONE_HOE.identifier, "Stone Hoe", ToolTier.STONE))
        register(Hoe(VanillaItems.WOODEN_HOE.identifier, "Wooden Hoe", ToolTier.WOOD))

        register(Pickaxe(VanillaItems.NETHERITE_PICKAXE.identifier, "Netherite Pickaxe", ToolTier.NETHERITE))
        register(Pickaxe(VanillaItems.DIAMOND_PICKAXE.identifier, "Diamond Pickaxe", ToolTier.DIAMOND))
        register(Pickaxe(VanillaItems.GOLDEN_PICKAXE.identifier, "Golden Pickaxe", ToolTier.GOLD))
        register(Pickaxe(VanillaItems.IRON_PICKAXE.identifier, "Iron Pickaxe", ToolTier.IRON))
        register(Pickaxe(VanillaItems.STONE_PICKAXE.identifier, "Stone Pickaxe", ToolTier.STONE))
        register(Pickaxe(VanillaItems.WOODEN_PICKAXE.identifier, "Wooden Pickaxe", ToolTier.WOOD))

        register(Shovel(VanillaItems.NETHERITE_SHOVEL.identifier, "Netherite Shovel", ToolTier.NETHERITE))
        register(Shovel(VanillaItems.DIAMOND_SHOVEL.identifier, "Diamond Shovel", ToolTier.DIAMOND))
        register(Shovel(VanillaItems.GOLDEN_SHOVEL.identifier, "Golden Shovel", ToolTier.GOLD))
        register(Shovel(VanillaItems.IRON_SHOVEL.identifier, "Iron Shovel", ToolTier.IRON))
        register(Shovel(VanillaItems.STONE_SHOVEL.identifier, "Stone Shovel", ToolTier.STONE))
        register(Shovel(VanillaItems.WOODEN_SHOVEL.identifier, "Wooden Shovel", ToolTier.WOOD))

        register(Sword(VanillaItems.NETHERITE_SWORD.identifier, "Netherite Sword", ToolTier.NETHERITE))
        register(Sword(VanillaItems.DIAMOND_SWORD.identifier, "Diamond Sword", ToolTier.DIAMOND))
        register(Sword(VanillaItems.GOLDEN_SWORD.identifier, "Golden Sword", ToolTier.GOLD))
        register(Sword(VanillaItems.IRON_SWORD.identifier, "Iron Sword", ToolTier.IRON))
        register(Sword(VanillaItems.STONE_SWORD.identifier, "Stone Sword", ToolTier.STONE))
        register(Sword(VanillaItems.WOODEN_SWORD.identifier, "Wooden Sword", ToolTier.WOOD))
    }

    @JvmStatic
    @JvmOverloads
    fun register(item: Item, override: Boolean = false) {
        val id: Int = item.getId()
        val variant = item.getMeta()

        if (!override && isRegistered(id, variant)) {
            throw RuntimeException("Trying to overwrite an already registered item")
        }
        list[getListOffset(id, variant)] = item.clone()
    }

    @JvmStatic
    @JvmOverloads
    fun remap(identifier: ItemIdentifier, item: Item, override: Boolean = false) {
        if (!override && isRegistered(identifier.id, identifier.meta)) {
            throw RuntimeException("Trying to overwrite an already registered item")
        }

        list[getListOffset(identifier.id, identifier.meta)] = item.clone()
    }

    @JvmStatic
    @JvmOverloads
    fun get(id: Int, meta: Int = 0, count: Int = 1, tags: CompoundTag? = null): Item {
        val offset = getListOffset(id, meta)
        val zero = getListOffset(id, 0)

        var item: Item? = when {
            meta != -1 ->
                list[offset] ?: list[zero]?.let {
                    val durableItem = list[zero]
                    if (durableItem is Durable && meta <= durableItem.maxDurability) {
                        val clone = durableItem.clone() as Durable
                        clone.damage = meta
                        clone
                    } else {
                        null
                    }
                }
                    ?: if (id < 256) ItemBlock(ItemIdentifier(id, meta), BlockFactory.get(if (id < 0) -id else id, meta and 0xf))
                    else null
            else -> null
        }

        if (item === null) {
            item = Item(ItemIdentifier(id, meta))
        }

        item.count = count
        if (tags != null) {
            item.setNamedTag(tags)
        }

        return item
    }

    @JvmStatic
    fun air(): Item {
        return get(VanillaItems.AIR.id, 0, 0)
    }

    @JvmStatic
    @JvmOverloads
    fun isRegistered(id: Int, variant: Int = 0): Boolean {
        if (id < 256) {
            return BlockFactory.isRegistered(id)
        }

        return list.containsKey(getListOffset(id, variant))
    }

    private fun getListOffset(id: Int, variant: Int): Int {
        if (id !in -0x8000..0x7fff) {
            throw IllegalArgumentException("ID must be in range ${0x8000} - ${0x7fff}")
        }

        return ((id and 0xffff) shl 16) or (variant and 0xffff)
    }
}
