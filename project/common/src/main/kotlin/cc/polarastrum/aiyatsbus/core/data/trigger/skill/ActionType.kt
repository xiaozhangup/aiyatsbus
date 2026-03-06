package cc.polarastrum.aiyatsbus.core.data.trigger.skill

/**
 * 技能触发动作类型
 *
 * 描述玩家触发技能时的基础动作：左右键交互与主副手交换。
 *
 * @author mical
 * @since 2026/2/8 21:18
 */
enum class ActionType {

    /** 右键触发 */
    RIGHT_CLICK,

    /** 左键触发 */
    LEFT_CLICK,

    /** 切换主副手触发 */
    SWAP
}
