package cc.polarastrum.aiyatsbus.core.script

/**
 * 脚本类型枚举
 *
 * 定义了 Aiyatsbus 支持的脚本语言类型。
 * 每种类型都有对应的脚本处理器实现。
 * 支持多种脚本语言，满足不同的开发需求。
 *
 * @author mical
 * @since 2025/6/22 12:54
 */
enum class ScriptType {

    /** Kether 脚本语言，基于 TabooLib 的脚本系统 */
    KETHER,

    /** JavaScript 脚本语言，支持标准的 JavaScript 语法 */
    JAVASCRIPT,

    /** Fluxon 脚本语言，服了脚本 */
    FLUXON
}