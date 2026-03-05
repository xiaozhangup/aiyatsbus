@file:Suppress("LeakingThis")

package cc.polarastrum.aiyatsbus.core

import cc.polarastrum.aiyatsbus.core.data.*
import cc.polarastrum.aiyatsbus.core.data.registry.Rarity
import cc.polarastrum.aiyatsbus.core.data.registry.Target
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import taboolib.module.configuration.Configuration
import java.io.File

/**
 * Aiyatsbus 附魔基础抽象类
 *
 * 2024/8/21 此类变为抽象类，使第三方通过代码自定义附魔更容易。
 * 需要实现这个类，此类不再提供自带的触发器，方便其他插件如果有需要调用自己提供的触发器。
 *
 * @author mical
 * @since 2024/2/17 14:39
 */
abstract class AiyatsbusEnchantmentBase(
    final override val id: String,
    final override val file: File?,
    final override val config: Configuration
) : AiyatsbusEnchantment {

    override val enchantmentKey: NamespacedKey = NamespacedKey.minecraft(id)

    override val basicData: BasicData = BasicData(config.getConfigurationSection("basic")!!)

    override val alternativeData: AlternativeData = AlternativeData(config.getConfigurationSection("alternative"))

    override val dependencies: Dependencies = Dependencies(config.getConfigurationSection("dependencies"))

    override lateinit var enchantment: Enchantment

    /** 手动缓存, 节约性能 */
    private var _rarity: Rarity? = null
    override val rarity: Rarity
        get() = _rarity ?: (aiyatsbusRarity(config["rarity"].toString())
            ?: aiyatsbusRarity(AiyatsbusSettings.defaultRarity) ?: error("Enchantment $id has an unknown rarity")).also { _rarity = it }

    override val variables: Variables = Variables(config.getConfigurationSection("variables"))

    /** 手动缓存, 节约性能 */
    private var _targets: List<Target>? = null
    override val targets: List<Target>
        get() = _targets ?: config.getStringList("targets").mapNotNull(::aiyatsbusTarget).also { _targets = it }

    override val displayer: Displayer = Displayer(config.getConfigurationSection("display")!!, this)

    override val limitations: Limitations = Limitations(this, config.getStringList("limitations"))

    override fun updateEnchantment() {
        _rarity = null
        _targets = null
    }
}
