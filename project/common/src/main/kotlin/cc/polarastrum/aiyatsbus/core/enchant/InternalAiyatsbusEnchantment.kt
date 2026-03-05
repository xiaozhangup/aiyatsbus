package cc.polarastrum.aiyatsbus.core.enchant

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.InternalEnchantment
import cc.polarastrum.aiyatsbus.core.data.trigger.Mechanism
import taboolib.module.configuration.Configuration
import java.io.File

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.enchant.SplendidEnchant
 *
 * @author mical
 * @date 2024/8/21 17:43
 */
class InternalAiyatsbusEnchantment(
    id: String,
    file: File,
    config: Configuration
) : AiyatsbusEnchantmentBase(id, file, config), InternalEnchantment {

    /**
     * 附魔机制
     */
    override val mechanism: Mechanism = Mechanism(config.getConfigurationSection("mechanisms"), this)
}