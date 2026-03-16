package cc.polarastrum.aiyatsbus.core

import cc.polarastrum.aiyatsbus.core.data.trigger.skill.ActionType
import org.bukkit.event.player.PlayerEvent
import taboolib.module.configuration.Configuration

/**
 * Aiyatsbus 技能处理器接口
 *
 * 负责技能型附魔的全链路处理：事件注册、冷却判定以及技能触发分发。
 * 不同的触发动作（左键、右键、切换主副手等）均通过此处理器统一管理。
 *
 * @author mical
 * @since 2026/1/27 17:19
 */
interface AiyatsbusSkillHandler {

    /**
     * 获取技能配置
     *
     * @return 技能处理相关的配置对象
     */
    fun getSettings(): Settings

    /**
     * 注册技能相关事件
     *
     * 按当前配置挂载所需的 Bukkit 事件监听器。
     */
    fun registerEvents()

    /**
     * 注销技能相关事件
     *
     * 在停用或重载时移除监听，避免重复注册。
     */
    fun unregisterEvents()

    /**
     * 处理玩家技能事件
     *
     * 根据触发动作类型分发到对应的技能脚本或逻辑。
     *
     * @param e 玩家事件实例
     * @param type 触发动作类型
     */
    fun handleEvent(e: PlayerEvent, type: ActionType)

    /**
     * 技能配置接口
     *
     * 定义技能触发、冷却等相关的可配置项。
     */
    interface Settings {

        /** 配置文件 */
        var conf: Configuration

        /** 是否开启冷却 */
        var enableCooldown: Boolean

        /** 冷却变量名 */
        var cooldownVar: String

        /** 默认触发形式 */
        val action: ActionType

        /** 是否需要下蹲才能触发技能 */
        var shiftNeeded: Boolean

        /** 下蹲时是否不触发技能 */
        var shiftIgnored: Boolean

        /** 冷却减免映射，键为标识，值为冷却表达式 */
        val privilege: Map<String, String>
    }
}
