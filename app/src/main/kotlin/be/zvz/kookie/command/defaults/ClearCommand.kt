package be.zvz.kookie.command.defaults

import be.zvz.kookie.command.CommandSender
import be.zvz.kookie.command.utils.InvalidCommandSyntaxException
import be.zvz.kookie.item.Item
import be.zvz.kookie.item.LegacyStringToItemParser
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.player.Player
import be.zvz.kookie.utils.TextFormat
import be.zvz.kookie.utils.Union

class ClearCommand(name: String) : VanillaCommand(
    name,
    "%pocketmine.command.clear.description",
    "%command.clear.usage"
) {
    init {
        permission = "pocketmine.command.clear"
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: List<String>): Boolean {
        if (!testPermission(sender)) {
            return true
        }
        if (args.size > 3) {
            throw InvalidCommandSyntaxException()
        }
        val target = when {
            args.getOrNull(0) != null -> {
                sender.server.getPlayerByPrefix(args[0]).apply {
                    if (this === null) {
                        sender.sendMessage(TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"))
                        return true
                    } else if (this != sender && !sender.hasPermission("pocketmine.command.clear.other")) {
                        sender.sendMessage(sender.language.translateString(TextFormat.RED + "%command.generic.permission"))
                        return true
                    }
                }
            }
            sender is Player -> {
                if (!sender.hasPermission("pocketmine.command.clear.self")) {
                    sender.sendMessage(sender.language.translateString(TextFormat.RED + "%commands.generic.permission"))
                    return true
                }
                sender
            }
            else -> {
                throw InvalidCommandSyntaxException()
            }
        }
        var maxCount = -1

        val item: Item? = if (args.getOrNull(1) !== null) {
            try {
                val item = LegacyStringToItemParser.parse(args[1])
                if (args.getOrNull(2) != null) {
                    maxCount = getInteger(sender, args[2], 0)
                    item.count = maxCount
                }
                item
            } catch (_: IllegalArgumentException) {
                sender.sendMessage(
                    TranslationContainer(
                        TextFormat.RED + "%commands.give.item.notFound",
                        listOf(
                            Union.U3.ofA(args[1])
                        )
                    )
                )
                return true
            }
        } else {
            null
        }

        if (item !== null && maxCount == 0) {
            val count = 0
        }
        TODO("Implement this when Entity has been merged")
        /*
		//checking players inventory for all the items matching the criteria
		if($item !== null and $maxCount === 0){
			$count = 0;
			$contents = array_merge($target->getInventory()->all($item), $target->getArmorInventory()->all($item));
			foreach($contents as $content){
				$count += $content->getCount();
			}

			if($count > 0){
				$sender->sendMessage(new TranslationContainer("%commands.clear.testing", [$target->getName(), $count]));
			}else{
				$sender->sendMessage(new TranslationContainer(TextFormat::RED . "%commands.clear.failure.no.items", [$target->getName()]));
			}

			return true;
		}

		$cleared = 0;

		//clear everything from the targets inventory
		if($item === null){
			$contents = array_merge($target->getInventory()->getContents(), $target->getArmorInventory()->getContents());
			foreach($contents as $content){
				$cleared += $content->getCount();
			}

			$target->getInventory()->clearAll();
			$target->getArmorInventory()->clearAll();
			//TODO: should the cursor inv be cleared?
		}else{
			//clear the item from targets inventory irrelevant of the count
			if($maxCount === -1){
				if(($slot = $target->getArmorInventory()->first($item)) !== -1){
					$cleared++;
					$target->getArmorInventory()->clear($slot);
				}

				foreach($target->getInventory()->all($item) as $index => $i){
					$cleared += $i->getCount();
					$target->getInventory()->clear($index);
				}
			}else{
				//clear only the given amount of that particular item from targets inventory
				if(($slot = $target->getArmorInventory()->first($item)) !== -1){
					$cleared++;
					$maxCount--;
					$target->getArmorInventory()->clear($slot);
				}

				if($maxCount > 0){
					foreach($target->getInventory()->all($item) as $index => $i){
						if($i->getCount() >= $maxCount){
							$i->pop($maxCount);
							$cleared += $maxCount;
							$target->getInventory()->setItem($index, $i);
							break;
						}

						if($maxCount <= 0){
							break;
						}

						$cleared += $i->getCount();
						$maxCount -= $i->getCount();
						$target->getInventory()->clear($index);
					}
				}
			}
		}

		if($cleared > 0){
			Command::broadcastCommandMessage($sender, new TranslationContainer("%commands.clear.success", [$target->getName(), $cleared]));
		}else{
			$sender->sendMessage(new TranslationContainer(TextFormat::RED . "%commands.clear.failure.no.items", [$target->getName()]));
		}

         */
    }
}
