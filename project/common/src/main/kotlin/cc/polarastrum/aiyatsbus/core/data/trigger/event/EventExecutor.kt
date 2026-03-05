package cc.polarastrum.aiyatsbus.core.data.trigger.event

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.data.trigger.Trigger
import cc.polarastrum.aiyatsbus.core.data.trigger.TriggerType
import cc.polarastrum.aiyatsbus.core.script.ScriptType
import taboolib.library.configuration.ConfigurationSection

/**
 * 事件执行器类
 *
 * 定义事件触发时的脚本执行逻辑，支持不同脚本类型和优先级设置。
 * 负责在特定事件发生时执行相应的脚本代码。
 *
 * @author mical
 * @since 2024/3/9 18:35
 */
data class EventExecutor @JvmOverloads constructor(
    /** 配置根节点 */
    private val root: ConfigurationSection,
    /** 所属附魔 */
    private val enchant: AiyatsbusEnchantment,
    /** 脚本类型，默认为 KETHER */
    val scriptType: ScriptType = ScriptType.valueOf(root.getString("type")?.uppercase() ?: "KETHER"),
    /** 监听的事件类型 */
    val listen: String = root.getString("listen")!!,
    /** 事件处理脚本 */
    override val handle: String = root.getString("handle") ?: "",
    /** 执行优先级，默认为 0 */
    override val priority: Int = root.getInt("priority", 0)
) : Trigger(root, enchant, scriptType, handle, priority, TriggerType.LISTENER)