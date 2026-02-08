package cc.polarastrum.aiyatsbus.core.data.trigger

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.event.AiyatsbusEnchantmentExecuteEvent
import cc.polarastrum.aiyatsbus.core.script.ScriptType
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.warning
import taboolib.library.configuration.ConfigurationSection
import java.io.Closeable

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.trigger.Trigger
 *
 * @author mical
 * @since 2024/3/9 18:36
 */
abstract class Trigger(
    /** 配置根节点 */
    root: ConfigurationSection,
    /** 所属附魔 */
    private val enchant: AiyatsbusEnchantment,
    private val scriptType: ScriptType,
    open val handle: String,
    /** 执行优先级，默认为 0 */
    open val priority: Int,
    val type: TriggerType
) : Closeable {

    val id: String = root.name
    val internalId: String =
        "Enchantment_" + enchant.basicData.id + "_" + type.name.lowercase().replaceFirstChar { it.uppercase() } + "_" + id.replace("-", "_") + "_"

    open fun init() {
        try {
            preheat()
        } catch (ex: Throwable) {
            warning("Unable to preheat the ${type.name.lowercase()} $id of enchantment ${enchant.id}")
            ex.printStackTrace()
        }
    }

    open fun preheat() {
        with(Aiyatsbus.api().getScriptHandler().getScriptHandler(scriptType)) {
            preheat(handle, "${internalId}Handle")
        }
    }

    /**
     * 执行事件脚本
     *
     * 使用指定的脚本处理器执行事件处理脚本。
     *
     * @param sender 命令发送者
     * @param vars 变量映射表
     *
     * @example
     * ```kotlin
     * executor.execute(player, mapOf("level" to 5, "damage" to 10.0))
     * ```
     */
    open fun executeHandle(entity: LivingEntity, vars: MutableMap<String, Any?>) {
        if (!AiyatsbusEnchantmentExecuteEvent(entity, this, type, handle, vars).call()) return
        Aiyatsbus.api().getScriptHandler().getScriptHandler(scriptType).invoke(handle, "${internalId}Handle", entity, vars)
    }

    override fun close() {
    }
}