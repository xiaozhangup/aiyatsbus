package cc.polarastrum.aiyatsbus.core.data.trigger.ticker

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.data.trigger.Trigger
import cc.polarastrum.aiyatsbus.core.data.trigger.TriggerType
import cc.polarastrum.aiyatsbus.core.event.AiyatsbusEnchantmentExecuteEvent
import cc.polarastrum.aiyatsbus.core.script.ScriptType
import org.bukkit.entity.LivingEntity
import taboolib.module.configuration.Configuration

/**
 * 定时器触发器类
 *
 * 定义定时执行的脚本触发器，支持预处理、主处理和后处理脚本。
 * 可以按指定间隔执行脚本，用于实现持续性的附魔效果。
 *
 * @author mical
 * @since 2024/3/20 22:28
 */
data class BuiltinTicker @JvmOverloads constructor(
    /** 所属附魔 */
    val enchant: AiyatsbusEnchantment,
    /** 名称 */
    val name: String,
    /** 预处理脚本，在定时器启动前执行 */
    val preHandleFunc: ((LivingEntity, MutableMap<String, Any?>) -> Unit)? = null,
    /** 主处理脚本，定时执行的主要逻辑 */
    val handleFunc: ((LivingEntity, MutableMap<String, Any?>) -> Unit)? = null,
    /** 后处理脚本，在定时器停止后执行 */
    val postHandleFunc: ((LivingEntity, MutableMap<String, Any?>) -> Unit)? = null,
    /** 执行间隔（tick），默认为 20（1 秒） */
    val interval: Long = 20L
) : Trigger(Configuration.empty().createSection(name), enchant, ScriptType.FLUXON, "", -1, TriggerType.TICKER), Ticker {

    /** 注册定时任务 */
    override fun init() {
        Aiyatsbus.api().getTickHandler().getRoutine().put(enchant, id, interval)
    }

    override fun preheat() { }

    override fun executePreHandle(entity: LivingEntity, vars: MutableMap<String, Any?>) {
        if (!AiyatsbusEnchantmentExecuteEvent(entity, this, type, "${internalId}BuiltinPreHandle", vars).call()) return
        preHandleFunc?.invoke(entity, vars)
    }

    override fun executePostHandle(entity: LivingEntity, vars: MutableMap<String, Any?>) {
        if (!AiyatsbusEnchantmentExecuteEvent(entity, this, type, "${internalId}BuiltinPostHandle", vars).call()) return
        postHandleFunc?.invoke(entity, vars)
    }

    override fun executeTickerHandle(entity: LivingEntity, vars: MutableMap<String, Any?>) {
        if (!AiyatsbusEnchantmentExecuteEvent(entity, this, type, "${internalId}BuiltinHandle", vars).call()) return
        handleFunc?.invoke(entity, vars)
    }

    override fun executeHandle(entity: LivingEntity, vars: MutableMap<String, Any?>) {
        executeTickerHandle(entity, vars)
    }

    override fun close() {
        Aiyatsbus.api().getTickHandler().getRoutine().remove(enchant, id)
    }
}
