package cc.polarastrum.aiyatsbus.impl

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import taboolib.common.util.unsafeLazy

/**
 * Aiyatsbus 启动器
 * 
 * 负责 Aiyatsbus 系统的启动和初始化。
 * 注册 API 实例，确保系统正常运行。
 *
 * @author mical
 * @since 2024/2/17 16:19
 */
object DefaultAiyatsbusBooster {

    /** API 实例，延迟初始化 */
    val api by unsafeLazy { DefaultAiyatsbusAPI() }

    /**
     * 启动 Aiyatsbus 系统
     * 
     * 注册 API 实例到全局 Aiyatsbus 对象中
     */
    fun startup() {
        Aiyatsbus.register(api)
    }
}