package cc.polarastrum.aiyatsbus.core.data.trigger.ticker

import org.bukkit.entity.LivingEntity

/**
 * 定时器触发器类
 *
 * 定义定时执行的脚本触发器，支持预处理、主处理和后处理脚本。
 * 可以按指定间隔执行脚本，用于实现持续性的附魔效果。
 *
 * @author mical
 * @since 2024/3/20 22:28
 */
interface Ticker {

    /** 执行预处理脚本 */
    fun executePreHandle(entity: LivingEntity, vars: MutableMap<String, Any?>)

    /** 执行后处理脚本 */
    fun executePostHandle(entity: LivingEntity, vars: MutableMap<String, Any?>)

    /** 执行脚本 */
    fun executeTickerHandle(entity: LivingEntity, vars: MutableMap<String, Any?>)
}
