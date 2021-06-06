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
package be.zvz.kookie.network.mcpe.protocol.types.inventory.stackrequest

enum class ItemStackRequestActionType(val type: Int) {
    TAKE(0),
    PLACE(1),
    SWAP(2),
    DROP(3),
    DESTROY(4),
    CRAFTING_CONSUME_INPUT(5),
    CRAFTING_MARK_SECONDARY_RESULT_SLOT(6),
    LAB_TABLE_COMBINE(7),
    BEACON_PAYMENT(8),
    MINE_BLOCK(9),
    CRAFTING_RECIPE(10),
    CRAFTING_RECIPE_AUTO(11), // recipe book?
    CREATIVE_CREATE(12),
    CRAFTING_RECIPE_OPTIONAL(13), // anvil/cartography table rename
    CRAFTING_NON_IMPLEMENTED_DEPRECATED_ASK_TY_LAING(14),
    CRAFTING_RESULTS_DEPRECATED_ASK_TY_LAING(15), // no idea what this is for
}
