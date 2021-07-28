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
import be.zvz.kookie.block.util.RecordType
import be.zvz.kookie.block.util.SkullType
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

        register(Apple(ItemIdentifier(VanillaItems.APPLE.id, 0), "Apple"))
        register(Arrow(ItemIdentifier(VanillaItems.ARROW.id, 0), "Arrow"))

        register(BakedPotato(ItemIdentifier(VanillaItems.BAKED_POTATO.id, 0), "Baked Potato"))
        register(Bamboo(ItemIdentifier(VanillaItems.BAMBOO.id, 0), "Bamboo"), true)
        register(Beetroot(ItemIdentifier(VanillaItems.BEETROOT.id, 0), "Beetroot"))
        register(BeetrootSeeds(ItemIdentifier(VanillaItems.BEETROOT_SEEDS.id, 0), "Beetroot Seeds"))
        register(BeetrootSoup(ItemIdentifier(VanillaItems.BEETROOT_SOUP.id, 0), "Beetroot Soup"))
        register(BlazeRod(ItemIdentifier(VanillaItems.BLAZE_ROD.id, 0), "Blaze Rod"))
        register(Book(ItemIdentifier(VanillaItems.BOOK.id, 0), "Book"))
        register(Bow(ItemIdentifier(VanillaItems.BOW.id, 0), "Bow"))
        register(Bowl(ItemIdentifier(VanillaItems.BOWL.id, 0), "Bowl"))
        register(Bread(ItemIdentifier(VanillaItems.BREAD.id, 0), "Bread"))
        register(Bucket(ItemIdentifier(VanillaItems.BUCKET.id, 0), "Bucket"))
        register(Carrot(ItemIdentifier(VanillaItems.CARROT.id, 0), "Carrot"))
        register(ChorusFruit(ItemIdentifier(VanillaItems.CHORUS_FRUIT.id, 0), "Chorus Fruit"))
        register(Clock(ItemIdentifier(VanillaItems.CLOCK.id, 0), "Clock"))
        register(Clownfish(ItemIdentifier(VanillaItems.CLOWNFISH.id, 0), "Clownfish"))
        register(Coal(ItemIdentifier(VanillaItems.COAL.id, 0), "Coal"))

        // TODO: how?
        /*
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.CORAL_FAN.id, 0),
                (VanillaBlocks.CORAL_FAN).setCoralType(CoralType.TUBE),
                VanillaBlocks.WALL_CORAL_FAN.setCoralType(CoralType.TUBE())
            ), true
        )
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.CORAL_FAN.id, 1),
                VanillaBlocks.CORAL_FAN.setCoralType(CoralType.BRAIN),
                VanillaBlocks.WALL_CORAL_FAN.setCoralType(CoralType.BRAIN())
            ), true
        )
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.CORAL_FAN.id, 2),
                VanillaBlocks.CORAL_FAN.setCoralType(CoralType.BUBBLE),
                VanillaBlocks.WALL_CORAL_FAN.setCoralType(CoralType.BUBBLE())
            ), true
        )
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.CORAL_FAN.id, 3),
                VanillaBlocks.CORAL_FAN.setCoralType(CoralType.FIRE),
                VanillaBlocks.WALL_CORAL_FAN.setCoralType(CoralType.FIRE())
            ), true
        )
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.CORAL_FAN.id, 4),
                VanillaBlocks.CORAL_FAN.setCoralType(CoralType.HORN),
                VanillaBlocks.WALL_CORAL_FAN.setCoralType(CoralType.HORN())
            ), true
        )
        */
        register(Coal(ItemIdentifier(VanillaItems.COAL.id, 1), "Charcoal"))
        register(CocoaBeans(ItemIdentifier(VanillaItems.DYE.id, 3), "Cocoa Beans"))
        register(Compass(ItemIdentifier(VanillaItems.COMPASS.id, 0), "Compass"))
        register(CookedChicken(ItemIdentifier(VanillaItems.COOKED_CHICKEN.id, 0), "Cooked Chicken"))
        register(CookedFish(ItemIdentifier(VanillaItems.COOKED_FISH.id, 0), "Cooked Fish"))
        register(CookedMutton(ItemIdentifier(VanillaItems.COOKED_MUTTON.id, 0), "Cooked Mutton"))
        register(CookedPorkchop(ItemIdentifier(VanillaItems.COOKED_PORKCHOP.id, 0), "Cooked Porkchop"))
        register(CookedRabbit(ItemIdentifier(VanillaItems.COOKED_RABBIT.id, 0), "Cooked Rabbit"))
        register(CookedSalmon(ItemIdentifier(VanillaItems.COOKED_SALMON.id, 0), "Cooked Salmon"))
        register(Cookie(ItemIdentifier(VanillaItems.COOKIE.id, 0), "Cookie"))
        register(DriedKelp(ItemIdentifier(VanillaItems.DRIED_KELP.id, 0), "Dried Kelp"))
        register(Egg(ItemIdentifier(VanillaItems.EGG.id, 0), "Egg"))
        register(EnderPearl(ItemIdentifier(VanillaItems.ENDER_PEARL.id, 0), "Ender Pearl"))
        register(ExperienceBottle(ItemIdentifier(VanillaItems.BOTTLE_O_ENCHANTING.id, 0), "Bottle o' Enchanting"))
        register(Fertilizer(ItemIdentifier(VanillaItems.DYE.id, 15), "Bone Meal"))
        register(FishingRod(ItemIdentifier(VanillaItems.FISHING_ROD.id, 0), "Fishing Rod"))
        register(FlintSteel(ItemIdentifier(VanillaItems.FLINT_AND_STEEL.id, 0), "Flint and Steel"))
        register(GlassBottle(ItemIdentifier(VanillaItems.GLASS_BOTTLE.id, 0), "Glass Bottle"))
        register(GoldenApple(ItemIdentifier(VanillaItems.GOLDEN_APPLE.id, 0), "Golden Apple"))
        register(GoldenAppleEnchanted(ItemIdentifier(VanillaItems.ENCHANTED_GOLDEN_APPLE.id, 0), "Enchanted Golden Apple"))
        register(GoldenCarrot(ItemIdentifier(VanillaItems.GOLDEN_CARROT.id, 0), "Golden Carrot"))
        register(Item(ItemIdentifier(VanillaItems.BLAZE_POWDER.id, 0), "Blaze Powder"))
        register(Item(ItemIdentifier(VanillaItems.BLEACH.id, 0), "Bleach")) // EDU

        register(Item(ItemIdentifier(VanillaItems.BONE.id, 0), "Bone"))
        register(Item(ItemIdentifier(VanillaItems.BRICK.id, 0), "Brick"))
        register(Item(ItemIdentifier(VanillaItems.POPPED_CHORUS_FRUIT.id, 0), "Popped Chorus Fruit"))
        register(Item(ItemIdentifier(VanillaItems.CLAY.id, 0), "Clay"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 0), "Salt"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 1), "Sodium Oxide"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 2), "Sodium Hydroxide"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 3), "Magnesium Nitrate"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 4), "Iron Sulphide"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 5), "Lithium Hydride"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 6), "Sodium Hydride"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 7), "Calcium Bromide"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 8), "Magnesium Oxide"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 9), "Sodium Acetate"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 10), "Luminol"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 11), "Charcoal")) // ??? maybe bug

        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 12), "Sugar")) // ??? maybe bug

        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 13), "Aluminium Oxide"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 14), "Boron Trioxide"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 15), "Soap"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 16), "Polyethylene"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 17), "Rubbish"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 18), "Magnesium Salts"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 19), "Sulphate"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 20), "Barium Sulphate"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 21), "Potassium Chloride"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 22), "Mercuric Chloride"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 23), "Cerium Chloride"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 24), "Tungsten Chloride"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 25), "Calcium Chloride"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 26), "Water")) // ???

        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 27), "Glue"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 28), "Hypochlorite"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 29), "Crude Oil"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 30), "Latex"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 31), "Potassium Iodide"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 32), "Sodium Fluoride"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 33), "Benzene"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 34), "Ink"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 35), "Hydrogen Peroxide"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 36), "Ammonia"))
        register(Item(ItemIdentifier(VanillaItems.COMPOUND.id, 37), "Sodium Hypochlorite"))
        register(Item(ItemIdentifier(VanillaItems.DIAMOND.id, 0), "Diamond"))
        register(Item(ItemIdentifier(VanillaItems.DRAGON_BREATH.id, 0), "Dragon's Breath"))
        register(Item(ItemIdentifier(VanillaItems.DYE.id, 0), "Ink Sac"))
        register(Item(ItemIdentifier(VanillaItems.DYE.id, 4), "Lapis Lazuli"))
        register(Item(ItemIdentifier(VanillaItems.EMERALD.id, 0), "Emerald"))
        register(Item(ItemIdentifier(VanillaItems.FEATHER.id, 0), "Feather"))
        register(Item(ItemIdentifier(VanillaItems.FERMENTED_SPIDER_EYE.id, 0), "Fermented Spider Eye"))
        register(Item(ItemIdentifier(VanillaItems.FLINT.id, 0), "Flint"))
        register(Item(ItemIdentifier(VanillaItems.GHAST_TEAR.id, 0), "Ghast Tear"))
        register(Item(ItemIdentifier(VanillaItems.GLISTERING_MELON_SLICE.id, 0), "Glistering Melon"))
        register(Item(ItemIdentifier(VanillaItems.GLOWSTONE_DUST.id, 0), "Glowstone Dust"))
        register(Item(ItemIdentifier(VanillaItems.GOLD_INGOT.id, 0), "Gold Ingot"))
        register(Item(ItemIdentifier(VanillaItems.GOLD_NUGGET.id, 0), "Gold Nugget"))
        register(Item(ItemIdentifier(VanillaItems.GUNPOWDER.id, 0), "Gunpowder"))
        register(Item(ItemIdentifier(VanillaItems.HEART_OF_THE_SEA.id, 0), "Heart of the Sea"))
        register(Item(ItemIdentifier(VanillaItems.IRON_INGOT.id, 0), "Iron Ingot"))
        register(Item(ItemIdentifier(VanillaItems.IRON_NUGGET.id, 0), "Iron Nugget"))
        register(Item(ItemIdentifier(VanillaItems.LEATHER.id, 0), "Leather"))
        register(Item(ItemIdentifier(VanillaItems.MAGMA_CREAM.id, 0), "Magma Cream"))
        register(Item(ItemIdentifier(VanillaItems.NAUTILUS_SHELL.id, 0), "Nautilus Shell"))
        register(Item(ItemIdentifier(VanillaItems.NETHER_BRICK.id, 0), "Nether Brick"))
        register(Item(ItemIdentifier(VanillaItems.NETHER_QUARTZ.id, 0), "Nether Quartz"))
        register(Item(ItemIdentifier(VanillaItems.NETHER_STAR.id, 0), "Nether Star"))
        register(Item(ItemIdentifier(VanillaItems.PAPER.id, 0), "Paper"))
        register(Item(ItemIdentifier(VanillaItems.PRISMARINE_CRYSTALS.id, 0), "Prismarine Crystals"))
        register(Item(ItemIdentifier(VanillaItems.PRISMARINE_SHARD.id, 0), "Prismarine Shard"))
        register(Item(ItemIdentifier(VanillaItems.RABBIT_FOOT.id, 0), "Rabbit's Foot"))
        register(Item(ItemIdentifier(VanillaItems.RABBIT_HIDE.id, 0), "Rabbit Hide"))
        register(Item(ItemIdentifier(VanillaItems.SHULKER_SHELL.id, 0), "Shulker Shell"))
        register(Item(ItemIdentifier(VanillaItems.SLIME_BALL.id, 0), "Slimeball"))
        register(Item(ItemIdentifier(VanillaItems.SUGAR.id, 0), "Sugar"))
        register(Item(ItemIdentifier(VanillaItems.SCUTE.id, 0), "Scute"))
        register(Item(ItemIdentifier(VanillaItems.WHEAT.id, 0), "Wheat"))
        register(ItemBlock(ItemIdentifier(VanillaItems.ACACIA_DOOR.id, 0), VanillaBlocks.ACACIA_DOOR.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.BIRCH_DOOR.id, 0), VanillaBlocks.BIRCH_DOOR.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.BREWING_STAND.id, 0), VanillaBlocks.BREWING_STAND.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.CAKE.id, 0), VanillaBlocks.CAKE.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.COMPARATOR.id, 0), VanillaBlocks.UNPOWERED_COMPARATOR.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.DARK_OAK_DOOR.id, 0), VanillaBlocks.DARK_OAK_DOOR.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.FLOWER_POT.id, 0), VanillaBlocks.FLOWER_POT.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.HOPPER.id, 0), VanillaBlocks.HOPPER.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.IRON_DOOR.id, 0), VanillaBlocks.IRON_DOOR.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.ITEM_FRAME.id, 0), VanillaBlocks.ITEM_FRAME.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.JUNGLE_DOOR.id, 0), VanillaBlocks.JUNGLE_DOOR.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.NETHER_WART.id, 0), VanillaBlocks.NETHER_WART.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.OAK_DOOR.id, 0), VanillaBlocks.OAK_DOOR.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.REPEATER.id, 0), VanillaBlocks.UNPOWERED_REPEATER.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.SPRUCE_DOOR.id, 0), VanillaBlocks.SPRUCE_DOOR.block))
        register(ItemBlock(ItemIdentifier(VanillaItems.SUGARCANE.id, 0), VanillaBlocks.SUGAR_CANE.block))

        // the meta values for buckets are intentionally hardcoded because block IDs will change in the future
        val waterBucket =
            LiquidBucket(
                ItemIdentifier(VanillaItems.BUCKET.id, 8),
                "Water Bucket"
            ) // TODO: Liquid block (VanillaBlocks.WATER.block)
        register(waterBucket)
        remap(ItemIdentifier(VanillaItems.BUCKET.id, 9), waterBucket)
        val lavaBucket =
            LiquidBucket(
                ItemIdentifier(VanillaItems.BUCKET.id, 10),
                "Lava Bucket"
            ) // TODO: Liquid block (VanillaBlocks.LAVA.block)
        register(lavaBucket)
        remap(ItemIdentifier(VanillaItems.BUCKET.id, 11), lavaBucket)
        register(Melon(ItemIdentifier(VanillaItems.MELON.id, 0), "Melon"))
        register(MelonSeeds(ItemIdentifier(VanillaItems.MELON_SEEDS.id, 0), "Melon Seeds"))
        register(MilkBucket(ItemIdentifier(VanillaItems.BUCKET.id, 1), "Milk Bucket"))
        register(Minecart(ItemIdentifier(VanillaItems.MINECART.id, 0), "Minecart"))
        register(MushroomStew(ItemIdentifier(VanillaItems.MUSHROOM_STEW.id, 0), "Mushroom Stew"))
        register(PaintingItem(ItemIdentifier(VanillaItems.PAINTING.id, 0), "Painting"))
        register(PoisonousPotato(ItemIdentifier(VanillaItems.POISONOUS_POTATO.id, 0), "Poisonous Potato"))
        register(Potato(ItemIdentifier(VanillaItems.POTATO.id, 0), "Potato"))
        register(Pufferfish(ItemIdentifier(VanillaItems.PUFFERFISH.id, 0), "Pufferfish"))
        register(PumpkinPie(ItemIdentifier(VanillaItems.PUMPKIN_PIE.id, 0), "Pumpkin Pie"))
        register(PumpkinSeeds(ItemIdentifier(VanillaItems.PUMPKIN_SEEDS.id, 0), "Pumpkin Seeds"))
        register(RabbitStew(ItemIdentifier(VanillaItems.RABBIT_STEW.id, 0), "Rabbit Stew"))
        register(RawBeef(ItemIdentifier(VanillaItems.RAW_BEEF.id, 0), "Raw Beef"))
        register(RawChicken(ItemIdentifier(VanillaItems.RAW_CHICKEN.id, 0), "Raw Chicken"))
        register(RawFish(ItemIdentifier(VanillaItems.RAW_FISH.id, 0), "Raw Fish"))
        register(RawMutton(ItemIdentifier(VanillaItems.RAW_MUTTON.id, 0), "Raw Mutton"))
        register(RawPorkchop(ItemIdentifier(VanillaItems.RAW_PORKCHOP.id, 0), "Raw Porkchop"))
        register(RawRabbit(ItemIdentifier(VanillaItems.RAW_RABBIT.id, 0), "Raw Rabbit"))
        register(RawSalmon(ItemIdentifier(VanillaItems.RAW_SALMON.id, 0), "Raw Salmon"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_13.id, 0), RecordType.DISK_13, "Record 13"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_CAT.id, 0), RecordType.DISK_CAT, "Record Cat"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_BLOCKS.id, 0), RecordType.DISK_BLOCKS, "Record Blocks"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_CHIRP.id, 0), RecordType.DISK_CHIRP, "Record Chirp"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_FAR.id, 0), RecordType.DISK_FAR, "Record Far"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_MALL.id, 0), RecordType.DISK_MALL, "Record Mall"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_MELLOHI.id, 0), RecordType.DISK_MELLOHI, "Record Mellohi"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_STAL.id, 0), RecordType.DISK_STAL, "Record Stal"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_STRAD.id, 0), RecordType.DISK_STRAD, "Record Strad"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_WARD.id, 0), RecordType.DISK_WARD, "Record Ward"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_11.id, 0), RecordType.DISK_11, "Record 11"))
        register(Record(ItemIdentifier(VanillaItems.RECORD_WAIT.id, 0), RecordType.DISK_WAIT, "Record Wait"))
        register(Redstone(ItemIdentifier(VanillaItems.REDSTONE.id, 0), "Redstone"))
        register(RottenFlesh(ItemIdentifier(VanillaItems.ROTTEN_FLESH.id, 0), "Rotten Flesh"))
        register(Shears(ItemIdentifier(VanillaItems.SHEARS.id, 0), "Shears"))
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.SIGN.id, 0),
                VanillaBlocks.OAK_STANDING_SIGN.block,
                VanillaBlocks.OAK_WALL_SIGN.block
            )
        )
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.SPRUCE_SIGN.id, 0),
                VanillaBlocks.SPRUCE_STANDING_SIGN.block,
                VanillaBlocks.SPRUCE_WALL_SIGN.block
            )
        )
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.BIRCH_SIGN.id, 0),
                VanillaBlocks.BIRCH_STANDING_SIGN.block,
                VanillaBlocks.BIRCH_WALL_SIGN.block
            )
        )
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.JUNGLE_SIGN.id, 0),
                VanillaBlocks.JUNGLE_STANDING_SIGN.block,
                VanillaBlocks.JUNGLE_WALL_SIGN.block
            )
        )
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.ACACIA_SIGN.id, 0),
                VanillaBlocks.ACACIA_STANDING_SIGN.block,
                VanillaBlocks.ACACIA_WALL_SIGN.block
            )
        )
        register(
            ItemBlockWallOrFloor(
                ItemIdentifier(VanillaItems.DARKOAK_SIGN.id, 0),
                VanillaBlocks.DARK_OAK_STANDING_SIGN.block,
                VanillaBlocks.DARK_OAK_WALL_SIGN.block
            )
        )
        register(Snowball(ItemIdentifier(VanillaItems.SNOWBALL.id, 0), "Snowball"))
        register(SpiderEye(ItemIdentifier(VanillaItems.SPIDER_EYE.id, 0), "Spider Eye"))
        register(Steak(ItemIdentifier(VanillaItems.COOKED_BEEF.id, 0), "Steak"))
        register(Stick(ItemIdentifier(VanillaItems.STICK.id, 0), "Stick"))
        register(StringItem(ItemIdentifier(VanillaItems.STRING.id, 0), "String"))
        register(Totem(ItemIdentifier(VanillaItems.TOTEM.id, 0), "Totem of Undying"))
        register(WheatSeeds(ItemIdentifier(VanillaItems.WHEAT_SEEDS.id, 0), "Wheat Seeds"))
        register(WritableBook(ItemIdentifier(VanillaItems.WRITABLE_BOOK.id, 0), "Book & Quill"))
        register(WrittenBook(ItemIdentifier(VanillaItems.WRITTEN_BOOK.id, 0), "Written Book"))

        SkullType.values().forEach {
            register(Skull(ItemIdentifier(VanillaItems.SKULL.id, it.magicNumber), it.displayName, it))
        }

        TODO("Add Items")
    }

    private fun registerSpawnEggs() {
        // TODO: the meta values should probably be hardcoded; they won't change, but the EntityLegacyIds might

        // TODO: Set meta from EntityLegacyIds
        register(object : SpawnEgg(ItemIdentifier(VanillaItems.SPAWN_EGG.id, 0), "Zombie Spawn Egg") {
            override fun createEntity(world: World, pos: Vector3, yaw: Float, pitch: Float): Entity {
                TODO("Not yet implemented")
            }
        })
        register(object : SpawnEgg(ItemIdentifier(VanillaItems.SPAWN_EGG.id, 0), "Squid Spawn Egg") {
            override fun createEntity(world: World, pos: Vector3, yaw: Float, pitch: Float): Entity {
                TODO("Not yet implemented")
            }
        })
        register(object : SpawnEgg(ItemIdentifier(VanillaItems.SPAWN_EGG.id, 0), "Villager Spawn Egg") {
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
        register(Axe(ItemIdentifier(VanillaItems.NETHERITE_AXE.id, 0), "Netherite Axe", ToolTier.NETHERITE))
        register(Axe(ItemIdentifier(VanillaItems.DIAMOND_AXE.id, 0), "Diamond Axe", ToolTier.DIAMOND))
        register(Axe(ItemIdentifier(VanillaItems.GOLDEN_AXE.id, 0), "Golden Axe", ToolTier.GOLD))
        register(Axe(ItemIdentifier(VanillaItems.IRON_AXE.id, 0), "Iron Axe", ToolTier.IRON))
        register(Axe(ItemIdentifier(VanillaItems.STONE_AXE.id, 0), "Stone Axe", ToolTier.STONE))
        register(Axe(ItemIdentifier(VanillaItems.WOODEN_AXE.id, 0), "Wooden Axe", ToolTier.WOOD))

        register(Hoe(ItemIdentifier(VanillaItems.NETHERITE_HOE.id, 0), "Netherite Hoe", ToolTier.NETHERITE))
        register(Hoe(ItemIdentifier(VanillaItems.DIAMOND_HOE.id, 0), "Diamond Hoe", ToolTier.DIAMOND))
        register(Hoe(ItemIdentifier(VanillaItems.GOLDEN_HOE.id, 0), "Golden Hoe", ToolTier.GOLD))
        register(Hoe(ItemIdentifier(VanillaItems.IRON_HOE.id, 0), "Iron Hoe", ToolTier.IRON))
        register(Hoe(ItemIdentifier(VanillaItems.STONE_HOE.id, 0), "Stone Hoe", ToolTier.STONE))
        register(Hoe(ItemIdentifier(VanillaItems.WOODEN_HOE.id, 0), "Wooden Hoe", ToolTier.WOOD))

        register(Pickaxe(ItemIdentifier(VanillaItems.NETHERITE_PICKAXE.id, 0), "Netherite Pickaxe", ToolTier.NETHERITE))
        register(Pickaxe(ItemIdentifier(VanillaItems.DIAMOND_PICKAXE.id, 0), "Diamond Pickaxe", ToolTier.DIAMOND))
        register(Pickaxe(ItemIdentifier(VanillaItems.GOLDEN_PICKAXE.id, 0), "Golden Pickaxe", ToolTier.GOLD))
        register(Pickaxe(ItemIdentifier(VanillaItems.IRON_PICKAXE.id, 0), "Iron Pickaxe", ToolTier.IRON))
        register(Pickaxe(ItemIdentifier(VanillaItems.STONE_PICKAXE.id, 0), "Stone Pickaxe", ToolTier.STONE))
        register(Pickaxe(ItemIdentifier(VanillaItems.WOODEN_PICKAXE.id, 0), "Wooden Pickaxe", ToolTier.WOOD))

        register(Shovel(ItemIdentifier(VanillaItems.NETHERITE_SHOVEL.id, 0), "Netherite Shovel", ToolTier.NETHERITE))
        register(Shovel(ItemIdentifier(VanillaItems.DIAMOND_SHOVEL.id, 0), "Diamond Shovel", ToolTier.DIAMOND))
        register(Shovel(ItemIdentifier(VanillaItems.GOLDEN_SHOVEL.id, 0), "Golden Shovel", ToolTier.GOLD))
        register(Shovel(ItemIdentifier(VanillaItems.IRON_SHOVEL.id, 0), "Iron Shovel", ToolTier.IRON))
        register(Shovel(ItemIdentifier(VanillaItems.STONE_SHOVEL.id, 0), "Stone Shovel", ToolTier.STONE))
        register(Shovel(ItemIdentifier(VanillaItems.WOODEN_SHOVEL.id, 0), "Wooden Shovel", ToolTier.WOOD))

        register(Sword(ItemIdentifier(VanillaItems.NETHERITE_SWORD.id, 0), "Netherite Sword", ToolTier.NETHERITE))
        register(Sword(ItemIdentifier(VanillaItems.DIAMOND_SWORD.id, 0), "Diamond Sword", ToolTier.DIAMOND))
        register(Sword(ItemIdentifier(VanillaItems.GOLDEN_SWORD.id, 0), "Golden Sword", ToolTier.GOLD))
        register(Sword(ItemIdentifier(VanillaItems.IRON_SWORD.id, 0), "Iron Sword", ToolTier.IRON))
        register(Sword(ItemIdentifier(VanillaItems.STONE_SWORD.id, 0), "Stone Sword", ToolTier.STONE))
        register(Sword(ItemIdentifier(VanillaItems.WOODEN_SWORD.id, 0), "Wooden Sword", ToolTier.WOOD))
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
