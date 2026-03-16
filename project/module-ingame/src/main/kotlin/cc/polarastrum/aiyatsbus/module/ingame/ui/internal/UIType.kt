package cc.polarastrum.aiyatsbus.module.ingame.ui.internal

import cc.polarastrum.aiyatsbus.core.asLangOrNull
import org.bukkit.command.CommandSender

enum class UIType {
    ANVIL,
    ENCHANT_INFO,
    ENCHANT_SEARCH,
    FILTER_GROUP,
    FILTER_RARITY,
    FILTER_TARGET,
    ITEM_CHECK,
    MAIN_MENU,
    FAVORITE,
    UNKNOWN;

    fun display(sender: CommandSender): String? = sender.asLangOrNull("ui-type-${name.lowercase()}")
}