package cc.polarastrum.aiyatsbus.core.script

/**
 * Aiyatsbus 脚本处理器接口
 *
 * 负责管理不同类型的脚本处理器，如 Kether、JavaScript、Fluxon 等。
 * 提供统一的脚本处理接口，支持多种脚本语言的执行。
 * 根据脚本类型返回对应的处理器实例。
 *
 * @author mical
 * @since 2025/6/22 00:13
 */
interface AiyatsbusScriptHandler {

    /**
     * 获取指定类型的脚本处理器
     *
     * 根据脚本类型返回对应的处理器实例。
     * 支持 Kether、JavaScript、Fluxon 等多种脚本类型。
     *
     * @param type 脚本类型
     * @return 对应的脚本处理器实例
     */
    fun getScriptHandler(type: ScriptType): ScriptHandler
}