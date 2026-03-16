package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.compat.GuardItemChecker
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.QuickShopGruardCheckerImpl
 *
 * @author mical
 * @since 2024/8/19 09:50
 */
object QuickShopGuardCheckerHikariImpl : GuardItemChecker {

    override fun checkIsGuardItem(item: Item): Boolean {
        return AbstractDisplayItemHikari.checkIsGuardItemStack(item.itemStack)
    }

    @Awake(LifeCycle.ACTIVE)
    fun init() {
        if (kotlin.runCatching {
                Class.forName("com.ghostchu.quickshop.shop.display.AbstractDisplayItem")
            }.isSuccess) {
            GuardItemChecker.registeredIntegrations += this
        }
    }
}

object QuickShopGuardCheckerReremakeImpl : GuardItemChecker {

    override fun checkIsGuardItem(item: Item): Boolean {
        return AbstractDisplayItemReremake.checkIsGuardItemStack(item.itemStack)
    }

    @Awake(LifeCycle.ACTIVE)
    fun init() {
        if (kotlin.runCatching {
                Class.forName("org.maxgamer.quickshop.api.shop.AbstractDisplayItem")
            }.isSuccess) {
            GuardItemChecker.registeredIntegrations += this
        }
    }
}

typealias AbstractDisplayItemHikari = com.ghostchu.quickshop.shop.display.AbstractDisplayItem
typealias AbstractDisplayItemReremake = org.maxgamer.quickshop.api.shop.AbstractDisplayItem