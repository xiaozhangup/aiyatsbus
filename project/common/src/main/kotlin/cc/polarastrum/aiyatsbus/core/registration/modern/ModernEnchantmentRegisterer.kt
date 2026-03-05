package cc.polarastrum.aiyatsbus.core.registration.modern

import cc.polarastrum.aiyatsbus.core.registration.AiyatsbusEnchantmentRegisterer

/**
 * 1.20.3+ 附魔注册器接口
 *
 * 继承自 AiyatsbusEnchantmentRegisterer，提供 1.20.3+ 版本的附魔注册功能。
 * 支持替换注册表等高级功能，适用于 1.20.3+ 的 Minecraft 版本。
 * 通过注册表的冻结和解冻机制，确保附魔注册的安全性。
 *
 * @author mical
 * @since 2024/2/17 15:22
 */
interface ModernEnchantmentRegisterer : AiyatsbusEnchantmentRegisterer {

    /**
     * 解冻注册表
     *
     * 在注册新附魔前解冻注册表，允许修改。
     */
    fun unfreezeRegistry()

    /**
     * 替换注册表
     *
     * 替换 Bukkit 的附魔注册表，确保自定义附魔能够正确注册和使用。
     * 这是 1.20.3+ 版本特有的功能。
     */
    fun replaceRegistry()

    /**
     * 冻结注册表
     *
     * 注册完成后冻结注册表，防止意外修改。
     */
    fun freezeRegistry()
}