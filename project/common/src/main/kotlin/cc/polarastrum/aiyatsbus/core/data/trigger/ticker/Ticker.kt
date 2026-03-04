package cc.polarastrum.aiyatsbus.core.data.trigger.ticker

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.data.trigger.Trigger
import cc.polarastrum.aiyatsbus.core.data.trigger.TriggerType
import cc.polarastrum.aiyatsbus.core.event.AiyatsbusEnchantmentExecuteEvent
import cc.polarastrum.aiyatsbus.core.script.ScriptType
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import taboolib.library.configuration.ConfigurationSection

/**
 * 定时器触发器类
 *
 * 定义定时执行的脚本触发器，支持预处理、主处理和后处理脚本。
 * 可以按指定间隔执行脚本，用于实现持续性的附魔效果。
 *
 * @author mical
 * @since 2024/3/20 22:28
 */
data class Ticker @JvmOverloads constructor(
    /** 配置根节点 */
    private val root: ConfigurationSection,
    /** 所属附魔 */
    private val enchant: AiyatsbusEnchantment,
    /** 脚本类型，默认为 KETHER */
    private val scriptType: ScriptType = ScriptType.valueOf(root.getString("type")?.uppercase() ?: "KETHER"),
    /** 预处理脚本，在定时器启动前执行 */
    val preHandle: String = root.getString("pre-handle") ?: "",
    /** 主处理脚本，定时执行的主要逻辑 */
    override val handle: String = root.getString("handle") ?: "",
    /** 后处理脚本，在定时器停止后执行 */
    val postHandle: String = root.getString("post-handle") ?: "",
    /** 执行间隔（tick），默认为 20（1 秒） */
    val interval: Long = root.getLong("interval", 20L)
) : Trigger(root, enchant, scriptType, handle, -1, TriggerType.TICKER) {

    override fun init() {
        super.init()
        Aiyatsbus.api().getTickHandler().getRoutine().put(enchant, id, interval)
    }

    override fun preheat() {
        with(Aiyatsbus.api().getScriptHandler().getScriptHandler(scriptType)) {
            preheat(preHandle, "${internalId}PreHandle")
            preheat(handle, "${internalId}Handle")
            preheat(postHandle, "${internalId}PostHandle")
        }
    }

    /**
     * 执行脚本
     *
     * 使用指定的脚本处理器执行脚本。
     *
     * @param source 脚本内容
     * @param sender 命令发送者
     * @param vars 变量映射表
     *
     * @example
     * ```kotlin
     * ticker.execute("say Hello", player, mapOf("level" to 5))
     * ```
     */
    private fun execute(source: String, id: String, sender: CommandSender, vars: Map<String, Any?>) {
        Aiyatsbus.api().getScriptHandler().getScriptHandler(scriptType)
            .invoke(source, id, sender, vars)
    }

    fun executePreHandle(entity: LivingEntity, vars: MutableMap<String, Any?>) {
        if (!AiyatsbusEnchantmentExecuteEvent(entity, this, type, preHandle, vars).call()) return
        execute(preHandle, "${internalId}PreHandle", entity, vars)
    }

    fun executePostHandle(entity: LivingEntity, vars: MutableMap<String, Any?>) {
        if (!AiyatsbusEnchantmentExecuteEvent(entity, this, type, postHandle, vars).call()) return
        execute(postHandle, "${internalId}PostHandle", entity, vars)
    }

    override fun close() {
        Aiyatsbus.api().getTickHandler().getRoutine().remove(enchant, id)
    }
}