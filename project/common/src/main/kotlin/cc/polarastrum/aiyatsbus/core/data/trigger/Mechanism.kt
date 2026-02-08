package cc.polarastrum.aiyatsbus.core.data.trigger

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.data.trigger.event.EventExecutor
import cc.polarastrum.aiyatsbus.core.data.trigger.skill.Skill
import cc.polarastrum.aiyatsbus.core.data.trigger.ticker.Ticker
import cc.polarastrum.aiyatsbus.core.util.safeguard
import taboolib.library.configuration.ConfigurationSection
import java.io.Closeable
import java.util.*

/**
 * 机制类类
 *
 * 负责注册和注销各种触发器。
 * 支持事件监听器、定时器、技能附魔三种触发方式。
 *
 * @author mical
 * @since 2026/2/8 18:55
 */
data class Mechanism(
    /** 配置节点 */
    private val section: ConfigurationSection?,
    /** 所属附魔 */
    var enchant: AiyatsbusEnchantment
) : Closeable {

    private val triggers = HashMap<Trigger, TriggerType>()
    private val priorities = EnumMap<TriggerType, Int>(TriggerType::class.java)

    fun init() = safeguard("附魔机制", "enchantment mechanisms") {
        // 初始化事件监听器
        section?.getConfigurationSection("listeners")?.let { listenersSection ->
            for (listener in listenersSection.getKeys(false)) {
                triggers[EventExecutor(listenersSection.getConfigurationSection(listener)!!, enchant)] = TriggerType.LISTENER
            }
        }
        // 初始化定时器
        section?.getConfigurationSection("tickers")?.let { tickersSection ->
            for (ticker in tickersSection.getKeys(false)) {
                triggers[Ticker(tickersSection.getConfigurationSection(ticker)!!, enchant)] = TriggerType.TICKER
            }
        }
        // 初始化技能
        section?.getConfigurationSection("skills")?.let { skillSection ->
            for (skill in skillSection.getKeys(false)) {
                triggers[Skill(skillSection.getConfigurationSection(skill)!!, enchant)] = TriggerType.SKILL
            }
        }
    }

    fun priority(type: TriggerType): Int {
        return priorities.getOrPut(type) { section?.getInt("priority-${type.name.lowercase()}", 0) ?: 0 }
    }

    fun triggers(type: TriggerType): Collection<Trigger> {
        return triggers.filter { it.value == type }.keys
    }

    fun hasTrigger(type: TriggerType): Boolean {
        return triggers.containsValue(type)
    }

    /**
     * 禁用触发器
     *
     * 清理所有监听器和定时器，释放相关资源。
     *
     * @example
     * ```kotlin
     * trigger.onDisable()
     * ```
     */
    override fun close() {
        triggers.forEach { it.key.close() }
        triggers.clear()
    }
}