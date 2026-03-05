package cc.polarastrum.aiyatsbus.core.event

import taboolib.platform.type.BukkitProxyEvent

/**
 * Aiyatsbus 重载事件
 *
 * 当 Aiyatsbus 插件执行重载操作时触发此事件。
 * 允许其他插件监听重载过程，并根据需要执行相应的处理逻辑。
 *
 * @author mical
 * @since 2025/6/27 00:06
 */
class AiyatsbusReloadEvent: BukkitProxyEvent() {

    /** 是否允许取消事件，重载事件不允许取消 */
    override val allowCancelled: Boolean = false
}