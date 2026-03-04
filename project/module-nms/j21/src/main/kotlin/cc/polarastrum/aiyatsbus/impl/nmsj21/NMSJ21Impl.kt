/*
 *  Copyright (C) 2022-2024 PolarAstrumLab
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package cc.polarastrum.aiyatsbus.impl.nmsj21

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.aiyatsbusEt
import cc.polarastrum.aiyatsbus.core.toDisplayMode
import cc.polarastrum.aiyatsbus.core.util.isNull
import com.google.common.collect.Maps
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.trading.MerchantOffers
import org.bukkit.craftbukkit.enchantments.CraftEnchantment
import org.bukkit.craftbukkit.entity.CraftLivingEntity
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.MinecraftVersion.versionId
import taboolib.module.nms.remap.DynamicOpcode
import taboolib.module.nms.remap.dynamic
import java.util.*
import kotlin.jvm.optionals.getOrNull

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms.NMS12005Impl
 *
 * @author mical
 * @since 2024/5/5 20:26
 */
class NMSJ21Impl : NMSJ21() {

    override fun getRepairCost(item: ItemStack): Int {
        return (item as CraftItemStack).handle[DataComponents.REPAIR_COST] ?: 0
    }

    override fun setRepairCost(item: ItemStack, cost: Int) : ItemStack {
        return item.apply {
            (this as CraftItemStack).handle[DataComponents.REPAIR_COST] = cost
        }
    }

    override fun adaptMerchantRecipe(merchantRecipeList: Any, player: Player) {

        fun adapt(item: Any, player: Player): Any {
            val bkItem = CraftItemStack.asCraftMirror(item as NMSItemStack)
            if (bkItem.isNull) return item
            return (bkItem.toDisplayMode(player) as CraftItemStack).handle
        }

        val previous = merchantRecipeList as MerchantOffers
        for (i in 0 until previous.size) {
            with(previous[i]!!) {
                baseCostA.setProperty("itemStack", adapt(baseCostA.itemStack, player))
                setProperty("costB", Optional.ofNullable(costB.getOrNull()?.also { it.setProperty("itemStack", adapt(it.itemStack, player)) }))
                setProperty("result", adapt(result, player) as NMSItemStack)
            }
        }
    }

    override fun hurtAndBreak(nmsItem: Any, amount: Int, entity: LivingEntity) {
        return (nmsItem as NMSItemStack).hurtAndBreak(amount, (entity as CraftLivingEntity).handle, null)
    }

    private fun resourceLocationGetPath(resourceLocation: Any): String {
        if (versionId > 12110) {
            return dynamic(
                DynamicOpcode.INVOKEVIRTUAL,
                "net.minecraft.resources.Identifier#getPath()java.lang.String;",
                resourceLocation
            ) as String
        }
        return dynamic(
            DynamicOpcode.INVOKEVIRTUAL,
            "net.minecraft.resources.ResourceLocation#getPath()java.lang.String;",
            resourceLocation
        ) as String
    }

    private fun nmsEnchNamespacedKey(resourceKey: ResourceKey<*>): Any {
        if (versionId > 12110) {
            return dynamic(
                DynamicOpcode.INVOKEVIRTUAL,
                "net.minecraft.resources.ResourceKey#identifier()net.minecraft.resources.Identifier;",
                resourceKey
            ) as Any
        }
        return dynamic(
            DynamicOpcode.INVOKEVIRTUAL,
            "net.minecraft.resources.ResourceKey#location()net.minecraft.resources.ResourceLocation;",
            resourceKey
        ) as Any
    }

    override fun getEnchants(item: ItemStack): Map<AiyatsbusEnchantment, Int> {
        val handle: NMSItemStack = if (item is CraftItemStack) item.handle else CraftItemStack.asNMSCopy(item)
        val stored = handle.get(DataComponents.STORED_ENCHANTMENTS) ?: handle.get(DataComponents.ENCHANTMENTS) ?: return emptyMap()
        val entries = stored.entrySet()
        if (entries.isEmpty()) {
            return emptyMap()
        }
        val map = Maps.newHashMapWithExpectedSize<AiyatsbusEnchantment, Int>(entries.size)
        for (entry in entries) {
            map[aiyatsbusEt(
                resourceLocationGetPath(nmsEnchNamespacedKey(entry.key.unwrapKey().get()))
            )!!] = entry.value
        }
        return map
    }

    override fun getFastEnchants(item: ItemStack): Array<Array<Any>> {
        val handle: NMSItemStack = if (item is CraftItemStack) item.handle else CraftItemStack.asNMSCopy(item)
        val stored = handle.get(DataComponents.STORED_ENCHANTMENTS) ?: handle.get(DataComponents.ENCHANTMENTS) ?: return emptyArray()
        val entries = stored.entrySet()
        if (entries.isEmpty()) {
            return emptyArray()
        }
        val array = Array<Array<Any>>(entries.size) { arrayOf() }
        entries.forEachIndexed { i, entry ->
            array[i] = arrayOf(aiyatsbusEt(
                resourceLocationGetPath(nmsEnchNamespacedKey(entry.key.unwrapKey().get()))
            )!!, entry.value)
        }
        return array
    }

    override fun getEnchantLevel(item: ItemStack, enchant: AiyatsbusEnchantment): Int? {
        val handle: NMSItemStack = if (item is CraftItemStack) item.handle else CraftItemStack.asNMSCopy(item)
        val stored = handle.get(DataComponents.STORED_ENCHANTMENTS) ?: handle.get(DataComponents.ENCHANTMENTS) ?: return null
        return if (MinecraftVersion.isHigherOrEqual(MinecraftVersion.V1_21)) {
            stored.getLevel(CraftEnchantment.bukkitToMinecraftHolder(enchant.enchantment))
        } else {
            dynamic(
                DynamicOpcode.INVOKEVIRTUAL,
                "net.minecraft.world.item.enchantment#getLevel(net.minecraft.world.item.enchantment.Enchantment;)I",
                stored,
                enchant.enchantment
            ) as Int
        }
    }

    override fun isUnbreakable(item: ItemStack): Boolean {
        val handle: NMSItemStack = if (item is CraftItemStack) item.handle else CraftItemStack.asNMSCopy(item)

        /**
         * java.lang.IncompatibleClassChangeError: Found interface net.minecraft.core.component.DataComponentHolder, but class was expected
         * 所以这里为了避免这个问题, 不能用父类/接口函数 net.minecraft.core.component.DataComponentHolder#get
         * 要用 net.minecraft.world.item.ItemStack#get
         *
         * - [INVOKEVIRTUAL] — 调用实例方法（含 abstract / interface 方法）。
         *   JVM 会在运行时根据对象实际类型进行虚分派，因此无论目标方法声明在
         *   class、abstract class 还是 interface 上，一律使用此操作码即可。
         *   不需要区分 INVOKEINTERFACE，transformer 会根据目标类型自动处理
         */
        if (versionId > 12104) {
            return dynamic(
                DynamicOpcode.INVOKEVIRTUAL,
                "net.minecraft.world.item.ItemStack#get(net.minecraft.core.component.DataComponentType;)java.lang.Object;",
                handle,
                DataComponents.UNBREAKABLE
            ) != null
        }
        val unbreakable = dynamic(
            DynamicOpcode.INVOKEVIRTUAL,
            "net.minecraft.world.item.ItemStack#get(net.minecraft.core.component.DataComponentType;)java.lang.Object;",
            handle,
            DataComponents.UNBREAKABLE
        ) ?: return false
        return dynamic(
            DynamicOpcode.INVOKEVIRTUAL,
            "net.minecraft.world.item.component.Unbreakable#showInTooltip()Z",
            unbreakable
        ) as Boolean
    }
}

typealias NMSItemStack = net.minecraft.world.item.ItemStack