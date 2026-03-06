package cc.polarastrum.aiyatsbus.core.data

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.util.replace
import cc.polarastrum.aiyatsbus.core.util.replacePlaceholder
import cc.polarastrum.aiyatsbus.core.util.roman
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.chat.colored
import taboolib.module.configuration.Configuration

/**
 * 附魔显示类
 *
 * 负责管理附魔的显示格式和内容，包括名称、描述、等级等显示元素。
 * 支持变量替换、占位符处理和颜色格式化。
 *
 * @author mical
 * @since 2024/2/17 22:23
 */
data class Displayer(
    /** 配置根节点 */
    private val root: ConfigurationSection,
    /** 对应的附魔实例 */
    var enchant: AiyatsbusEnchantment?,
    /** 是否显示该附魔 */
    val display: Boolean = root.getBoolean("display", true),
    /** 附魔显示的前半部分，一般是名称和等级 */
    val previous: String = root.getString("format.previous", "{default_previous}")!!,
    /** 附魔显示的后半部分，一般是描述并且换行写 */
    val subsequent: String = root.getString("format.subsequent", "{default_subsequent}")!!,
    /** 描述 */
    val generalDescription: String = root.getString("description.general", "&7")!!.let {
        "&8${it.replace("&7", "&8").replace("&a", "&7")}" // 直接取代
    }, // 特殊显示, 直接内部格式化
    /** 一般有变量的描述会用这个替换变量，这个不写默认为普通描述 */
    val specificDescription: String = root.getString("description.specific")?.let {
        "&8${it.replace("&7", "&8").replace("&a", "&7")}" // 直接取代
    } ?: generalDescription
) {

    /** 显示管理器设置 */
    private val displayManagerSettings = Aiyatsbus.api().getDisplayManager().getSettings()

    /**
     * 是否是在 display.yml 中设置的默认配置格式
     *
     * @return true 表示使用默认格式
     */
    fun isDefaultDisplay() = previous == "{default_previous}" && subsequent == "{default_subsequent}"

    /**
     * 生成本附魔在当前状态下的显示，在非合并模式下
     *
     * @param level 附魔等级
     * @param player 玩家
     * @param item 物品
     * @return 格式化后的显示文本
     */
    fun display(level: Int, player: Player?, item: ItemStack?) = display(holders(level, player, item))

    /**
     * 生成本附魔在当前状态下的显示，在非合并模式下
     *
     * @param holders 变量映射
     * @return 格式化后的显示文本
     */
    fun display(holders: Map<String, String>): String {
        return (previous.replace("{default_previous}", displayManagerSettings.defaultPrevious)
                + subsequent.replace("{default_subsequent}", displayManagerSettings.defaultSubsequent)
                ).replace(holders).colored()
    }

    /**
     * 生成本附魔在当前状态下的显示，在合并模式下
     *
     * @param level 附魔等级
     * @param player 玩家
     * @param item 物品
     * @param index 索引
     * @return 显示映射
     */
    fun displays(
        level: Int,
        player: Player? = null,
        item: ItemStack? = null,
        index: Int? = null
    ): Map<String, String> {
        val suffix = index?.let { "_$it" } ?: ""
        val holders = holders(level, player, item)
        return mapOf(
            "previous$suffix" to previous.replace("{default_previous}", displayManagerSettings.defaultPrevious)
                .replace(holders).colored(),
            "subsequent$suffix" to subsequent.replace("{default_subsequent}", displayManagerSettings.defaultSubsequent)
                .replace(holders).colored()
        )
    }

    /**
     * 生成本附魔在当前状态下的变量替换 Map
     *
     * @param level 附魔等级
     * @param player 玩家
     * @param item 物品
     * @return 变量映射
     */
    fun holders(
        level: Int,
        player: Player? = null,
        item: ItemStack? = null
    ): Map<String, String> {
        val enchant = this.enchant!!
        val tmp = enchant.variables.variables(level, item, true)
            .mapValues { it.value.toString() }.toMutableMap() // 因为是显示，这里的变量可以直接转为字符串
        val lv = level
        tmp["id"] = enchant.basicData.id
        tmp["name"] = enchant.basicData.name
        tmp["level"] = "$lv"
        tmp["roman_level"] = lv.roman(enchant.basicData.maxLevel == 1)
        tmp["roman_level_with_a_blank"] = lv.roman(enchant.basicData.maxLevel == 1, true)
        tmp["max_level"] = "${enchant.basicData.maxLevel}"
        tmp["rarity"] = enchant.rarity.name
        tmp["rarity_display"] = enchant.rarity.displayName()
        tmp["enchant_display"] = enchant.displayName()
        tmp["enchant_display_roman"] = enchant.displayName(lv)
        tmp["enchant_display_number"] = enchant.displayName(lv, false)
        tmp["enchant_display_lore"] = display(tmp).replacePlaceholder(player)
        tmp["description"] = specificDescription.replace(tmp).colored().replacePlaceholder(player)
        return tmp
    }

    fun serialize(): ConfigurationSection {
        return Configuration.empty().apply {
            set("display", display)
            set("format.previous", previous)
            set("format.subsequent", subsequent)
            set("description.general", generalDescription)
            set("description.specific", specificDescription)
        }
    }

    class Builder {

        private var display: Boolean = true
        private var previous: String = "{default_previous}"
        private var subsequent: String = "{default_subsequent}"
        private var generalDescription: String = "&7"
        private var specificDescription: String? = null

        fun display(display: Boolean): Builder {
            this.display = display
            return this
        }

        fun previous(previous: String): Builder {
            this.previous = previous
            return this
        }

        fun subsequent(subsequent: String): Builder {
            this.subsequent = subsequent
            return this
        }

        fun generalDescription(generalDescription: String): Builder {
            this.generalDescription = generalDescription
            return this
        }

        fun specificDescription(specificDescription: String): Builder {
            this.specificDescription = specificDescription
            return this
        }

        fun build(): Displayer {
            return Displayer(
                Configuration.empty(),
                null, // 可放心设置为 null，因为注册到 Minecraft NMS Registry 时会被赋值
                display,
                previous,
                subsequent,
                generalDescription,
                specificDescription ?: generalDescription
            )
        }
    }

    companion object {

        @JvmStatic
        fun builder() = Builder()
    }
}
