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
package be.zvz.kookie.lang

enum class KnownTranslationKeys(val key: String) {
    ABILITY_FLIGHT("ability.flight"),
    ABILITY_NOCLIP("ability.noclip"),
    ACCEPT_LICENSE("accept_license"),
    CHAT_TYPE_ACHIEVEMENT("chat.type.achievement"),
    CHAT_TYPE_ADMIN("chat.type.admin"),
    CHAT_TYPE_ANNOUNCEMENT("chat.type.announcement"),
    CHAT_TYPE_EMOTE("chat.type.emote"),
    CHAT_TYPE_TEXT("chat.type.text"),
    COMMANDS_BAN_SUCCESS("commands.ban.success"),
    COMMANDS_BAN_USAGE("commands.ban.usage"),
    COMMANDS_BANIP_INVALID("commands.banip.invalid"),
    COMMANDS_BANIP_SUCCESS("commands.banip.success"),
    COMMANDS_BANIP_SUCCESS_PLAYERS("commands.banip.success.players"),
    COMMANDS_BANIP_USAGE("commands.banip.usage"),
    COMMANDS_BANLIST_IPS("commands.banlist.ips"),
    COMMANDS_BANLIST_PLAYERS("commands.banlist.players"),
    COMMANDS_BANLIST_USAGE("commands.banlist.usage"),
    COMMANDS_CLEAR_FAILURE_NO_ITEMS("commands.clear.failure.no.items"),
    COMMANDS_CLEAR_SUCCESS("commands.clear.success"),
    COMMANDS_CLEAR_TESTING("commands.clear.testing"),
    COMMANDS_DEFAULTGAMEMODE_SUCCESS("commands.defaultgamemode.success"),
    COMMANDS_DEFAULTGAMEMODE_USAGE("commands.defaultgamemode.usage"),
    COMMANDS_DEOP_SUCCESS("commands.deop.success"),
    COMMANDS_DEOP_USAGE("commands.deop.usage"),
    COMMANDS_DIFFICULTY_SUCCESS("commands.difficulty.success"),
    COMMANDS_DIFFICULTY_USAGE("commands.difficulty.usage"),
    COMMANDS_EFFECT_FAILURE_NOTACTIVE("commands.effect.failure.notActive"),
    COMMANDS_EFFECT_FAILURE_NOTACTIVE_ALL("commands.effect.failure.notActive.all"),
    COMMANDS_EFFECT_NOTFOUND("commands.effect.notFound"),
    COMMANDS_EFFECT_SUCCESS("commands.effect.success"),
    COMMANDS_EFFECT_SUCCESS_REMOVED("commands.effect.success.removed"),
    COMMANDS_EFFECT_SUCCESS_REMOVED_ALL("commands.effect.success.removed.all"),
    COMMANDS_EFFECT_USAGE("commands.effect.usage"),
    COMMANDS_ENCHANT_NOITEM("commands.enchant.noItem"),
    COMMANDS_ENCHANT_NOTFOUND("commands.enchant.notFound"),
    COMMANDS_ENCHANT_SUCCESS("commands.enchant.success"),
    COMMANDS_ENCHANT_USAGE("commands.enchant.usage"),
    COMMANDS_GAMEMODE_SUCCESS_OTHER("commands.gamemode.success.other"),
    COMMANDS_GAMEMODE_SUCCESS_SELF("commands.gamemode.success.self"),
    COMMANDS_GAMEMODE_USAGE("commands.gamemode.usage"),
    COMMANDS_GENERIC_NOTFOUND("commands.generic.notFound"),
    COMMANDS_GENERIC_NUM_TOOBIG("commands.generic.num.tooBig"),
    COMMANDS_GENERIC_NUM_TOOSMALL("commands.generic.num.tooSmall"),
    COMMANDS_GENERIC_PERMISSION("commands.generic.permission"),
    COMMANDS_GENERIC_PLAYER_NOTFOUND("commands.generic.player.notFound"),
    COMMANDS_GENERIC_USAGE("commands.generic.usage"),
    COMMANDS_GIVE_ITEM_NOTFOUND("commands.give.item.notFound"),
    COMMANDS_GIVE_SUCCESS("commands.give.success"),
    COMMANDS_GIVE_TAGERROR("commands.give.tagError"),
    COMMANDS_HELP_HEADER("commands.help.header"),
    COMMANDS_HELP_USAGE("commands.help.usage"),
    COMMANDS_KICK_SUCCESS("commands.kick.success"),
    COMMANDS_KICK_SUCCESS_REASON("commands.kick.success.reason"),
    COMMANDS_KICK_USAGE("commands.kick.usage"),
    COMMANDS_KILL_SUCCESSFUL("commands.kill.successful"),
    COMMANDS_ME_USAGE("commands.me.usage"),
    COMMANDS_MESSAGE_DISPLAY_INCOMING("commands.message.display.incoming"),
    COMMANDS_MESSAGE_DISPLAY_OUTGOING("commands.message.display.outgoing"),
    COMMANDS_MESSAGE_SAMETARGET("commands.message.sameTarget"),
    COMMANDS_MESSAGE_USAGE("commands.message.usage"),
    COMMANDS_OP_SUCCESS("commands.op.success"),
    COMMANDS_OP_USAGE("commands.op.usage"),
    COMMANDS_PARTICLE_NOTFOUND("commands.particle.notFound"),
    COMMANDS_PARTICLE_SUCCESS("commands.particle.success"),
    COMMANDS_PLAYERS_LIST("commands.players.list"),
    COMMANDS_PLAYERS_USAGE("commands.players.usage"),
    COMMANDS_SAVE_OFF_USAGE("commands.save-off.usage"),
    COMMANDS_SAVE_ON_USAGE("commands.save-on.usage"),
    COMMANDS_SAVE_DISABLED("commands.save.disabled"),
    COMMANDS_SAVE_ENABLED("commands.save.enabled"),
    COMMANDS_SAVE_START("commands.save.start"),
    COMMANDS_SAVE_SUCCESS("commands.save.success"),
    COMMANDS_SAVE_USAGE("commands.save.usage"),
    COMMANDS_SAY_USAGE("commands.say.usage"),
    COMMANDS_SEED_SUCCESS("commands.seed.success"),
    COMMANDS_SEED_USAGE("commands.seed.usage"),
    COMMANDS_SETWORLDSPAWN_SUCCESS("commands.setworldspawn.success"),
    COMMANDS_SETWORLDSPAWN_USAGE("commands.setworldspawn.usage"),
    COMMANDS_SPAWNPOINT_SUCCESS("commands.spawnpoint.success"),
    COMMANDS_SPAWNPOINT_USAGE("commands.spawnpoint.usage"),
    COMMANDS_STOP_START("commands.stop.start"),
    COMMANDS_STOP_USAGE("commands.stop.usage"),
    COMMANDS_TIME_ADDED("commands.time.added"),
    COMMANDS_TIME_QUERY("commands.time.query"),
    COMMANDS_TIME_SET("commands.time.set"),
    COMMANDS_TITLE_SUCCESS("commands.title.success"),
    COMMANDS_TITLE_USAGE("commands.title.usage"),
    COMMANDS_TP_SUCCESS("commands.tp.success"),
    COMMANDS_TP_SUCCESS_COORDINATES("commands.tp.success.coordinates"),
    COMMANDS_TP_USAGE("commands.tp.usage"),
    COMMANDS_UNBAN_SUCCESS("commands.unban.success"),
    COMMANDS_UNBAN_USAGE("commands.unban.usage"),
    COMMANDS_UNBANIP_INVALID("commands.unbanip.invalid"),
    COMMANDS_UNBANIP_SUCCESS("commands.unbanip.success"),
    COMMANDS_UNBANIP_USAGE("commands.unbanip.usage"),
    COMMANDS_WHITELIST_ADD_SUCCESS("commands.whitelist.add.success"),
    COMMANDS_WHITELIST_ADD_USAGE("commands.whitelist.add.usage"),
    COMMANDS_WHITELIST_DISABLED("commands.whitelist.disabled"),
    COMMANDS_WHITELIST_ENABLED("commands.whitelist.enabled"),
    COMMANDS_WHITELIST_LIST("commands.whitelist.list"),
    COMMANDS_WHITELIST_RELOADED("commands.whitelist.reloaded"),
    COMMANDS_WHITELIST_REMOVE_SUCCESS("commands.whitelist.remove.success"),
    COMMANDS_WHITELIST_REMOVE_USAGE("commands.whitelist.remove.usage"),
    COMMANDS_WHITELIST_USAGE("commands.whitelist.usage"),
    DEATH_ATTACK_ARROW("death.attack.arrow"),
    DEATH_ATTACK_ARROW_ITEM("death.attack.arrow.item"),
    DEATH_ATTACK_CACTUS("death.attack.cactus"),
    DEATH_ATTACK_DROWN("death.attack.drown"),
    DEATH_ATTACK_EXPLOSION("death.attack.explosion"),
    DEATH_ATTACK_EXPLOSION_PLAYER("death.attack.explosion.player"),
    DEATH_ATTACK_FALL("death.attack.fall"),
    DEATH_ATTACK_GENERIC("death.attack.generic"),
    DEATH_ATTACK_INFIRE("death.attack.inFire"),
    DEATH_ATTACK_INWALL("death.attack.inWall"),
    DEATH_ATTACK_LAVA("death.attack.lava"),
    DEATH_ATTACK_MAGIC("death.attack.magic"),
    DEATH_ATTACK_MOB("death.attack.mob"),
    DEATH_ATTACK_ONFIRE("death.attack.onFire"),
    DEATH_ATTACK_OUTOFWORLD("death.attack.outOfWorld"),
    DEATH_ATTACK_PLAYER("death.attack.player"),
    DEATH_ATTACK_PLAYER_ITEM("death.attack.player.item"),
    DEATH_ATTACK_WITHER("death.attack.wither"),
    DEATH_FELL_ACCIDENT_GENERIC("death.fell.accident.generic"),
    DEFAULT_GAMEMODE("default_gamemode"),
    DEFAULT_VALUES_INFO("default_values_info"),
    DISCONNECTIONSCREEN_INVALIDNAME("disconnectionScreen.invalidName"),
    DISCONNECTIONSCREEN_INVALIDSKIN("disconnectionScreen.invalidSkin"),
    DISCONNECTIONSCREEN_NOREASON("disconnectionScreen.noReason"),
    DISCONNECTIONSCREEN_NOTAUTHENTICATED("disconnectionScreen.notAuthenticated"),
    DISCONNECTIONSCREEN_OUTDATEDCLIENT("disconnectionScreen.outdatedClient"),
    DISCONNECTIONSCREEN_OUTDATEDSERVER("disconnectionScreen.outdatedServer"),
    DISCONNECTIONSCREEN_RESOURCEPACK("disconnectionScreen.resourcePack"),
    DISCONNECTIONSCREEN_SERVERFULL("disconnectionScreen.serverFull"),
    GAMEMODE_ADVENTURE("gameMode.adventure"),
    GAMEMODE_CHANGED("gameMode.changed"),
    GAMEMODE_CREATIVE("gameMode.creative"),
    GAMEMODE_SPECTATOR("gameMode.spectator"),
    GAMEMODE_SURVIVAL("gameMode.survival"),
    GAMEMODE_INFO("gamemode_info"),
    INVALID_PORT("invalid_port"),
    IP_CONFIRM("ip_confirm"),
    IP_GET("ip_get"),
    IP_WARNING("ip_warning"),
    KICK_ADMIN("kick.admin"),
    KICK_ADMIN_REASON("kick.admin.reason"),
    KICK_REASON_CHEAT("kick.reason.cheat"),
    LANGUAGE_NAME("language.name"),
    LANGUAGE_SELECTED("language.selected"),
    LANGUAGE_HAS_BEEN_SELECTED("language_has_been_selected"),
    MAX_PLAYERS("max_players"),
    MULTIPLAYER_PLAYER_JOINED("multiplayer.player.joined"),
    MULTIPLAYER_PLAYER_LEFT("multiplayer.player.left"),
    NAME_YOUR_SERVER("name_your_server"),
    OP_INFO("op_info"),
    OP_WARNING("op_warning"),
    OP_WHO("op_who"),
    POCKETMINE_COMMAND_ALIAS_ILLEGAL("pocketmine.command.alias.illegal"),
    POCKETMINE_COMMAND_ALIAS_NOTFOUND("pocketmine.command.alias.notFound"),
    POCKETMINE_COMMAND_ALIAS_RECURSIVE("pocketmine.command.alias.recursive"),
    POCKETMINE_COMMAND_BAN_IP_DESCRIPTION("pocketmine.command.ban.ip.description"),
    POCKETMINE_COMMAND_BAN_PLAYER_DESCRIPTION("pocketmine.command.ban.player.description"),
    POCKETMINE_COMMAND_BANLIST_DESCRIPTION("pocketmine.command.banlist.description"),
    POCKETMINE_COMMAND_CLEAR_DESCRIPTION("pocketmine.command.clear.description"),
    POCKETMINE_COMMAND_CLEAR_USAGE("pocketmine.command.clear.usage"),
    POCKETMINE_COMMAND_DEFAULTGAMEMODE_DESCRIPTION("pocketmine.command.defaultgamemode.description"),
    POCKETMINE_COMMAND_DEOP_DESCRIPTION("pocketmine.command.deop.description"),
    POCKETMINE_COMMAND_DIFFICULTY_DESCRIPTION("pocketmine.command.difficulty.description"),
    POCKETMINE_COMMAND_EFFECT_DESCRIPTION("pocketmine.command.effect.description"),
    POCKETMINE_COMMAND_ENCHANT_DESCRIPTION("pocketmine.command.enchant.description"),
    POCKETMINE_COMMAND_EXCEPTION("pocketmine.command.exception"),
    POCKETMINE_COMMAND_GAMEMODE_DESCRIPTION("pocketmine.command.gamemode.description"),
    POCKETMINE_COMMAND_GC_DESCRIPTION("pocketmine.command.gc.description"),
    POCKETMINE_COMMAND_GC_USAGE("pocketmine.command.gc.usage"),
    POCKETMINE_COMMAND_GIVE_DESCRIPTION("pocketmine.command.give.description"),
    POCKETMINE_COMMAND_GIVE_USAGE("pocketmine.command.give.usage"),
    POCKETMINE_COMMAND_HELP_DESCRIPTION("pocketmine.command.help.description"),
    POCKETMINE_COMMAND_KICK_DESCRIPTION("pocketmine.command.kick.description"),
    POCKETMINE_COMMAND_KILL_DESCRIPTION("pocketmine.command.kill.description"),
    POCKETMINE_COMMAND_KILL_USAGE("pocketmine.command.kill.usage"),
    POCKETMINE_COMMAND_LIST_DESCRIPTION("pocketmine.command.list.description"),
    POCKETMINE_COMMAND_ME_DESCRIPTION("pocketmine.command.me.description"),
    POCKETMINE_COMMAND_OP_DESCRIPTION("pocketmine.command.op.description"),
    POCKETMINE_COMMAND_PARTICLE_DESCRIPTION("pocketmine.command.particle.description"),
    POCKETMINE_COMMAND_PARTICLE_USAGE("pocketmine.command.particle.usage"),
    POCKETMINE_COMMAND_PLUGINS_DESCRIPTION("pocketmine.command.plugins.description"),
    POCKETMINE_COMMAND_PLUGINS_SUCCESS("pocketmine.command.plugins.success"),
    POCKETMINE_COMMAND_PLUGINS_USAGE("pocketmine.command.plugins.usage"),
    POCKETMINE_COMMAND_SAVE_DESCRIPTION("pocketmine.command.save.description"),
    POCKETMINE_COMMAND_SAVEOFF_DESCRIPTION("pocketmine.command.saveoff.description"),
    POCKETMINE_COMMAND_SAVEON_DESCRIPTION("pocketmine.command.saveon.description"),
    POCKETMINE_COMMAND_SAY_DESCRIPTION("pocketmine.command.say.description"),
    POCKETMINE_COMMAND_SEED_DESCRIPTION("pocketmine.command.seed.description"),
    POCKETMINE_COMMAND_SETWORLDSPAWN_DESCRIPTION("pocketmine.command.setworldspawn.description"),
    POCKETMINE_COMMAND_SPAWNPOINT_DESCRIPTION("pocketmine.command.spawnpoint.description"),
    POCKETMINE_COMMAND_STATUS_DESCRIPTION("pocketmine.command.status.description"),
    POCKETMINE_COMMAND_STATUS_USAGE("pocketmine.command.status.usage"),
    POCKETMINE_COMMAND_STOP_DESCRIPTION("pocketmine.command.stop.description"),
    POCKETMINE_COMMAND_TELL_DESCRIPTION("pocketmine.command.tell.description"),
    POCKETMINE_COMMAND_TIME_DESCRIPTION("pocketmine.command.time.description"),
    POCKETMINE_COMMAND_TIME_USAGE("pocketmine.command.time.usage"),
    POCKETMINE_COMMAND_TIMINGS_ALREADYENABLED("pocketmine.command.timings.alreadyEnabled"),
    POCKETMINE_COMMAND_TIMINGS_DESCRIPTION("pocketmine.command.timings.description"),
    POCKETMINE_COMMAND_TIMINGS_DISABLE("pocketmine.command.timings.disable"),
    POCKETMINE_COMMAND_TIMINGS_ENABLE("pocketmine.command.timings.enable"),
    POCKETMINE_COMMAND_TIMINGS_PASTEERROR("pocketmine.command.timings.pasteError"),
    POCKETMINE_COMMAND_TIMINGS_RESET("pocketmine.command.timings.reset"),
    POCKETMINE_COMMAND_TIMINGS_TIMINGSDISABLED("pocketmine.command.timings.timingsDisabled"),
    POCKETMINE_COMMAND_TIMINGS_TIMINGSREAD("pocketmine.command.timings.timingsRead"),
    POCKETMINE_COMMAND_TIMINGS_TIMINGSUPLOAD("pocketmine.command.timings.timingsUpload"),
    POCKETMINE_COMMAND_TIMINGS_TIMINGSWRITE("pocketmine.command.timings.timingsWrite"),
    POCKETMINE_COMMAND_TIMINGS_USAGE("pocketmine.command.timings.usage"),
    POCKETMINE_COMMAND_TITLE_DESCRIPTION("pocketmine.command.title.description"),
    POCKETMINE_COMMAND_TP_DESCRIPTION("pocketmine.command.tp.description"),
    POCKETMINE_COMMAND_TRANSFERSERVER_DESCRIPTION("pocketmine.command.transferserver.description"),
    POCKETMINE_COMMAND_TRANSFERSERVER_USAGE("pocketmine.command.transferserver.usage"),
    POCKETMINE_COMMAND_UNBAN_IP_DESCRIPTION("pocketmine.command.unban.ip.description"),
    POCKETMINE_COMMAND_UNBAN_PLAYER_DESCRIPTION("pocketmine.command.unban.player.description"),
    POCKETMINE_COMMAND_VERSION_DESCRIPTION("pocketmine.command.version.description"),
    POCKETMINE_COMMAND_VERSION_NOSUCHPLUGIN("pocketmine.command.version.noSuchPlugin"),
    POCKETMINE_COMMAND_VERSION_USAGE("pocketmine.command.version.usage"),
    POCKETMINE_COMMAND_WHITELIST_DESCRIPTION("pocketmine.command.whitelist.description"),
    POCKETMINE_CRASH_ARCHIVE("pocketmine.crash.archive"),
    POCKETMINE_CRASH_CREATE("pocketmine.crash.create"),
    POCKETMINE_CRASH_ERROR("pocketmine.crash.error"),
    POCKETMINE_CRASH_SUBMIT("pocketmine.crash.submit"),
    POCKETMINE_DATA_PLAYERCORRUPTED("pocketmine.data.playerCorrupted"),
    POCKETMINE_DATA_PLAYERNOTFOUND("pocketmine.data.playerNotFound"),
    POCKETMINE_DATA_PLAYEROLD("pocketmine.data.playerOld"),
    POCKETMINE_DATA_SAVEERROR("pocketmine.data.saveError"),
    POCKETMINE_DEBUG_ENABLE("pocketmine.debug.enable"),
    POCKETMINE_DISCONNECT_INCOMPATIBLEPROTOCOL("pocketmine.disconnect.incompatibleProtocol"),
    POCKETMINE_DISCONNECT_INVALIDSESSION("pocketmine.disconnect.invalidSession"),
    POCKETMINE_DISCONNECT_INVALIDSESSION_BADSIGNATURE("pocketmine.disconnect.invalidSession.badSignature"),
    POCKETMINE_DISCONNECT_INVALIDSESSION_MISSINGKEY("pocketmine.disconnect.invalidSession.missingKey"),
    POCKETMINE_DISCONNECT_INVALIDSESSION_TOOEARLY("pocketmine.disconnect.invalidSession.tooEarly"),
    POCKETMINE_DISCONNECT_INVALIDSESSION_TOOLATE("pocketmine.disconnect.invalidSession.tooLate"),
    POCKETMINE_LEVEL_AMBIGUOUSFORMAT("pocketmine.level.ambiguousFormat"),
    POCKETMINE_LEVEL_BACKGROUNDGENERATION("pocketmine.level.backgroundGeneration"),
    POCKETMINE_LEVEL_BADDEFAULTFORMAT("pocketmine.level.badDefaultFormat"),
    POCKETMINE_LEVEL_DEFAULTERROR("pocketmine.level.defaultError"),
    POCKETMINE_LEVEL_GENERATIONERROR("pocketmine.level.generationError"),
    POCKETMINE_LEVEL_LOADERROR("pocketmine.level.loadError"),
    POCKETMINE_LEVEL_NOTFOUND("pocketmine.level.notFound"),
    POCKETMINE_LEVEL_PREPARING("pocketmine.level.preparing"),
    POCKETMINE_LEVEL_UNKNOWNFORMAT("pocketmine.level.unknownFormat"),
    POCKETMINE_LEVEL_UNLOADING("pocketmine.level.unloading"),
    POCKETMINE_PLAYER_INVALIDENTITY("pocketmine.player.invalidEntity"),
    POCKETMINE_PLAYER_INVALIDMOVE("pocketmine.player.invalidMove"),
    POCKETMINE_PLAYER_LOGIN("pocketmine.player.logIn"),
    POCKETMINE_PLAYER_LOGOUT("pocketmine.player.logOut"),
    POCKETMINE_PLUGIN_ALIASERROR("pocketmine.plugin.aliasError"),
    POCKETMINE_PLUGIN_AMBIGUOUSMINAPI("pocketmine.plugin.ambiguousMinAPI"),
    POCKETMINE_PLUGIN_CIRCULARDEPENDENCY("pocketmine.plugin.circularDependency"),
    POCKETMINE_PLUGIN_COMMANDERROR("pocketmine.plugin.commandError"),
    POCKETMINE_PLUGIN_DEPRECATEDEVENT("pocketmine.plugin.deprecatedEvent"),
    POCKETMINE_PLUGIN_DISABLE("pocketmine.plugin.disable"),
    POCKETMINE_PLUGIN_DUPLICATEERROR("pocketmine.plugin.duplicateError"),
    POCKETMINE_PLUGIN_ENABLE("pocketmine.plugin.enable"),
    POCKETMINE_PLUGIN_FILEERROR("pocketmine.plugin.fileError"),
    POCKETMINE_PLUGIN_GENERICLOADERROR("pocketmine.plugin.genericLoadError"),
    POCKETMINE_PLUGIN_INCOMPATIBLEAPI("pocketmine.plugin.incompatibleAPI"),
    POCKETMINE_PLUGIN_INCOMPATIBLEOS("pocketmine.plugin.incompatibleOS"),
    POCKETMINE_PLUGIN_INCOMPATIBLEPHPVERSION("pocketmine.plugin.incompatiblePhpVersion"),
    POCKETMINE_PLUGIN_INCOMPATIBLEPROTOCOL("pocketmine.plugin.incompatibleProtocol"),
    POCKETMINE_PLUGIN_LOAD("pocketmine.plugin.load"),
    POCKETMINE_PLUGIN_LOADERROR("pocketmine.plugin.loadError"),
    POCKETMINE_PLUGIN_RESTRICTEDNAME("pocketmine.plugin.restrictedName"),
    POCKETMINE_PLUGIN_SPACESDISCOURAGED("pocketmine.plugin.spacesDiscouraged"),
    POCKETMINE_PLUGIN_UNKNOWNDEPENDENCY("pocketmine.plugin.unknownDependency"),
    POCKETMINE_SAVE_START("pocketmine.save.start"),
    POCKETMINE_SAVE_SUCCESS("pocketmine.save.success"),
    POCKETMINE_SERVER_AUTH_DISABLED("pocketmine.server.auth.disabled"),
    POCKETMINE_SERVER_AUTH_ENABLED("pocketmine.server.auth.enabled"),
    POCKETMINE_SERVER_AUTHPROPERTY_DISABLED("pocketmine.server.authProperty.disabled"),
    POCKETMINE_SERVER_AUTHPROPERTY_ENABLED("pocketmine.server.authProperty.enabled"),
    POCKETMINE_SERVER_AUTHWARNING("pocketmine.server.authWarning"),
    POCKETMINE_SERVER_DEFAULTGAMEMODE("pocketmine.server.defaultGameMode"),
    POCKETMINE_SERVER_DEVBUILD_ERROR1("pocketmine.server.devBuild.error1"),
    POCKETMINE_SERVER_DEVBUILD_ERROR2("pocketmine.server.devBuild.error2"),
    POCKETMINE_SERVER_DEVBUILD_ERROR3("pocketmine.server.devBuild.error3"),
    POCKETMINE_SERVER_DEVBUILD_ERROR4("pocketmine.server.devBuild.error4"),
    POCKETMINE_SERVER_DEVBUILD_ERROR5("pocketmine.server.devBuild.error5"),
    POCKETMINE_SERVER_DEVBUILD_WARNING1("pocketmine.server.devBuild.warning1"),
    POCKETMINE_SERVER_DEVBUILD_WARNING2("pocketmine.server.devBuild.warning2"),
    POCKETMINE_SERVER_DEVBUILD_WARNING3("pocketmine.server.devBuild.warning3"),
    POCKETMINE_SERVER_DONATE("pocketmine.server.donate"),
    POCKETMINE_SERVER_INFO("pocketmine.server.info"),
    POCKETMINE_SERVER_INFO_EXTENDED("pocketmine.server.info.extended"),
    POCKETMINE_SERVER_LICENSE("pocketmine.server.license"),
    POCKETMINE_SERVER_NETWORKSTART("pocketmine.server.networkStart"),
    POCKETMINE_SERVER_QUERY_RUNNING("pocketmine.server.query.running"),
    POCKETMINE_SERVER_START("pocketmine.server.start"),
    POCKETMINE_SERVER_STARTFINISHED("pocketmine.server.startFinished"),
    POCKETMINE_SERVER_TICKOVERLOAD("pocketmine.server.tickOverload"),
    POCKETMINE_PLUGINS("pocketmine_plugins"),
    POCKETMINE_WILL_START("pocketmine_will_start"),
    PORT_WARNING("port_warning"),
    POTION_ABSORPTION("potion.absorption"),
    POTION_BLINDNESS("potion.blindness"),
    POTION_CONDUITPOWER("potion.conduitPower"),
    POTION_CONFUSION("potion.confusion"),
    POTION_DAMAGEBOOST("potion.damageBoost"),
    POTION_DIGSLOWDOWN("potion.digSlowDown"),
    POTION_DIGSPEED("potion.digSpeed"),
    POTION_FIRERESISTANCE("potion.fireResistance"),
    POTION_HARM("potion.harm"),
    POTION_HEAL("potion.heal"),
    POTION_HEALTHBOOST("potion.healthBoost"),
    POTION_HUNGER("potion.hunger"),
    POTION_INVISIBILITY("potion.invisibility"),
    POTION_JUMP("potion.jump"),
    POTION_LEVITATION("potion.levitation"),
    POTION_MOVESLOWDOWN("potion.moveSlowdown"),
    POTION_MOVESPEED("potion.moveSpeed"),
    POTION_NIGHTVISION("potion.nightVision"),
    POTION_POISON("potion.poison"),
    POTION_REGENERATION("potion.regeneration"),
    POTION_RESISTANCE("potion.resistance"),
    POTION_SATURATION("potion.saturation"),
    POTION_WATERBREATHING("potion.waterBreathing"),
    POTION_WEAKNESS("potion.weakness"),
    POTION_WITHER("potion.wither"),
    QUERY_DISABLE("query_disable"),
    QUERY_WARNING1("query_warning1"),
    QUERY_WARNING2("query_warning2"),
    SERVER_PORT("server_port"),
    SERVER_PROPERTIES("server_properties"),
    SETTING_UP_SERVER_NOW("setting_up_server_now"),
    SKIP_INSTALLER("skip_installer"),
    TILE_BED_NOSLEEP("tile.bed.noSleep"),
    TILE_BED_OCCUPIED("tile.bed.occupied"),
    TILE_BED_TOOFAR("tile.bed.tooFar"),
    WELCOME_TO_POCKETMINE("welcome_to_pocketmine"),
    WHITELIST_ENABLE("whitelist_enable"),
    WHITELIST_INFO("whitelist_info"),
    WHITELIST_WARNING("whitelist_warning"),
    YOU_HAVE_FINISHED("you_have_finished"),
    YOU_HAVE_TO_ACCEPT_THE_LICENSE("you_have_to_accept_the_license"),
}
