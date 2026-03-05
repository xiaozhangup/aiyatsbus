@file:Suppress("UNCHECKED_CAST")

package cc.polarastrum.aiyatsbus.module.ingame.ui

import cc.polarastrum.aiyatsbus.core.*
import cc.polarastrum.aiyatsbus.core.util.toBuiltComponent
import cc.polarastrum.aiyatsbus.module.ingame.ui.internal.*
import cc.polarastrum.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import cc.polarastrum.aiyatsbus.module.ingame.ui.internal.feature.util.MenuFunctionBuilder
import cc.polarastrum.aiyatsbus.core.util.variables
import org.bukkit.entity.Player
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.PageableChest
import cc.polarastrum.aiyatsbus.module.ingame.ui.internal.UIType
import cc.polarastrum.aiyatsbus.module.ingame.ui.internal.record
import org.bukkit.Material
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.module.chat.Source
import taboolib.platform.util.modifyMeta
import kotlin.collections.set
import kotlin.system.measureTimeMillis

@MenuComponent("Favorites")
object FavoritesUI {

    @Config("core/ui/favorites.yml", autoReload = true)
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    fun initialize() {
        config = MenuConfiguration(source)
    }

    @Awake(LifeCycle.ENABLE)
    fun init() {
        source.onReload {
            measureTimeMillis { config = MenuConfiguration(source) }
                .let { console().sendLang("configuration-reload", source.file!!.name, it) }
        }
    }

    fun open(player: Player) {
        player.record(UIType.FAVORITE)
        player.openMenu<PageableChest<AiyatsbusEnchantment>>(config.title().component().buildColored().toLegacyText()) {
//            virtualize()

            val (shape, templates) = config
            rows(shape.rows)
            val slots = shape["Favorites:enchant"].toList()
            slots(slots)
            elements { player.favorites.mapNotNull { aiyatsbusEt(it) } }

            load(
                shape, templates, player,
                "Favorites:enchant", "Previous", "Next"
            )
            pages(shape, templates)

            val template = templates.require("Favorites:enchant")
            onGenerate(async = true) { _, element, index, slot ->
                template(slot, index) {
                    this["enchant"] = element
                    this["player"] = player
                }
            }
            onClick { event, element -> templates[event.rawSlot]?.handle(this, event, "element" to element) }
        }
    }

    @MenuComponent
    private val enchant = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val enchant = args["enchant"] as AiyatsbusEnchantment
            val player = args["player"] as Player
            val holders = enchant.displayer.holders(enchant.basicData.maxLevel, player, enchant.book())
            icon.variables { variable -> listOf(holders[variable] ?: "") }
                .modifyMeta<ItemMeta> {
                    lore = lore.toBuiltComponent().map(Source::toLegacyText)
                    if (enchant.rarity.isCustomModelUIEnabled && !hasCustomModelData()) {
                        setCustomModelData(enchant.rarity.customModelUI)
                    }
                }
                .also { if (it.type == Material.PLAYER_HEAD) it.skull(enchant.rarity.skull) }
        }
        onClick { (_, _, _, event, args) ->
            EnchantInfoUI.open(event.clicker, args["element"] as AiyatsbusEnchantment)
        }
    }
}