@file:Suppress("DuplicatedCode")

package cc.polarastrum.aiyatsbus.impl

import cc.polarastrum.aiyatsbus.core.*
import cc.polarastrum.aiyatsbus.core.data.trigger.ticker.Ticker
import cc.polarastrum.aiyatsbus.core.event.AiyatsbusEnchantmentExecuteEvent
import cc.polarastrum.aiyatsbus.core.util.isNull
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.getItemTag
import taboolib.platform.util.submit

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.impl.LevelFixer
 *
 * @author mical
 * @since 2026/2/7 01:45
 */
object LevelFixer {

    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        e.player.submit(delay = 20L) {
            e.player.inventory.contents.filterNot { it.isNull }.forEach { fix(it!!, it.fastFixedEnchants) }
        }
    }

    @SubscribeEvent
    fun e(e: EntityPickupItemEvent) {
        fix(e.item.itemStack, e.item.itemStack.fastFixedEnchants)
    }

    @SubscribeEvent
    fun e(e: AiyatsbusEnchantmentExecuteEvent) {
        if (e.trigger is Ticker) {
            if (!e.isPreHandle()) return
        }
        fix(e.item ?: return, e.item.fastFixedEnchants)
        e.level = e.item?.fastEtLevel(e.enchant)!!
    }

    /**
     * 检测物品超过最大等级的附魔并进行修正
     * 会直接修改传入的物品
     *
     * @param item 原物品, 会直接修改
     * @param enchants 读取到的附魔列表
     * @return 是否修正了
     */
    fun fix(item: ItemStack, enchants: Map<AiyatsbusEnchantment, Int>): Boolean {
        var fixed = false
        with(AiyatsbusSettings) {
            if (!enableLevelfixers) return false
            for ((enchant, level) in enchants) {
                // 如果等级没超过最大等级, 压根不用管
                if (enchant.basicData.maxLevel >= level) continue
                // 如果不检测原版而此附魔又是原版附魔则直接跳过, 减少不必要的判断
                if (!fixVanillaLevel && enchant.alternativeData.isVanilla) continue
                // 如果不检测更多附魔而此附魔又是更多附魔则直接跳过, 减少不必要的判断
                if (!fixCustomLevel && !enchant.alternativeData.isVanilla) continue
                // 如果附魔在白名单中则跳过
                if (levelFixersWhitelist.contains(enchant.basicData.id) || levelFixersWhitelist.contains(enchant.basicData.originName)) continue
                // 此时检测 NBT, 如果存在直接结束循环
                // 在这里才检测, 而不是遍历前检测的原因是, 尽可能减少 ItemMeta 开销
                if (levelFixersBypassNBT.any { item.getItemTag().getDeep(it) != null }) break
                item.addEt(enchant, enchant.basicData.maxLevel)
                fixed = true
            }
        }
        return fixed
    }

    fun fix(item: ItemStack, enchants: Array<Array<Any>>): Boolean {
        var fixed = false
        with(AiyatsbusSettings) {
            if (!enableLevelfixers) return false
            for ((enchant, level) in enchants) {
                enchant as AiyatsbusEnchantment
                level as Int
                // 如果等级没超过最大等级, 压根不用管
                if (enchant.basicData.maxLevel >= level) continue
                // 如果不检测原版而此附魔又是原版附魔则直接跳过, 减少不必要的判断
                if (!fixVanillaLevel && enchant.alternativeData.isVanilla) continue
                // 如果不检测更多附魔而此附魔又是更多附魔则直接跳过, 减少不必要的判断
                if (!fixCustomLevel && !enchant.alternativeData.isVanilla) continue
                // 如果附魔在白名单中则跳过
                if (levelFixersWhitelist.contains(enchant.basicData.id) || levelFixersWhitelist.contains(enchant.basicData.originName)) continue
                // 此时检测 NBT, 如果存在直接结束循环
                // 在这里才检测, 而不是遍历前检测的原因是, 尽可能减少 ItemMeta 开销
                if (levelFixersBypassNBT.any { item.getItemTag().getDeep(it) != null }) break
                item.addEt(enchant, enchant.basicData.maxLevel)
                fixed = true
            }
        }
        return fixed
    }
}