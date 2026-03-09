package cc.polarastrum.aiyatsbus.core.registration

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantmentBase
import org.bukkit.enchantments.Enchantment

/**
 * Aiyatsbus 附魔注册器接口
 *
 * 负责附魔的注册和注销操作。
 * 将自定义附魔注册到 Bukkit 的附魔系统中，使其能够正常使用。
 * 提供统一的附魔管理接口，支持不同版本的 Minecraft。
 *
 * @author mical
 * @since 2024/2/17 14:59
 */
interface AiyatsbusEnchantmentRegisterer {

    /**
     * 注册附魔
     *
     * 将自定义附魔注册到 Bukkit 的附魔系统中。
     * 注册成功后，该附魔可以在游戏中正常使用。
     *
     * @param enchant 要注册的附魔实例
     * @return 注册后的 Bukkit 附魔实例
     */
    fun register(enchant: AiyatsbusEnchantmentBase) : Enchantment

    /**
     * 注销附魔
     *
     * 从 Bukkit 的附魔系统中移除指定的附魔。
     * 注销后，该附魔将无法在游戏中使用。
     *
     * @param enchant 要注销的附魔实例
     */
    fun unregister(enchant: AiyatsbusEnchantment)
}