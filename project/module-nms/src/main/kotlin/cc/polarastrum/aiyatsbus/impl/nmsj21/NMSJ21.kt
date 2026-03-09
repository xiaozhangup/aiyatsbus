package cc.polarastrum.aiyatsbus.impl.nmsj21

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.nmsProxy

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.nms12005.NMS12005
 *
 * @author mical
 * @since 2024/5/5 20:20
 */
abstract class NMSJ21 {

    abstract fun getRepairCost(item: ItemStack): Int

    abstract fun setRepairCost(item: ItemStack, cost: Int) : ItemStack

    abstract fun adaptMerchantRecipe(merchantRecipeList: Any, player: Player)

    abstract fun hurtAndBreak(nmsItem: Any, amount: Int, entity: LivingEntity)

    abstract fun getEnchants(item: ItemStack): Map<AiyatsbusEnchantment, Int>

    abstract fun getFastEnchants(item: ItemStack): Array<Array<Any>>

    abstract fun getEnchantLevel(item: ItemStack, enchant: AiyatsbusEnchantment): Int?

    abstract fun isUnbreakable(item: ItemStack): Boolean

    companion object {

        val instance by unsafeLazy { nmsProxy<NMSJ21>() }
    }
}