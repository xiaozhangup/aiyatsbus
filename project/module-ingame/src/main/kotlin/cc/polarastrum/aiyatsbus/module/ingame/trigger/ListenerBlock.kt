package cc.polarastrum.aiyatsbus.module.ingame.trigger

import cc.polarastrum.aiyatsbus.core.enchant.EventType
import cc.polarastrum.aiyatsbus.core.enchant.FileDefinedHardcodedEnchantment
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * Iweleth
 * cc.polarastrum.iweleth.listener.entries.ListenerBlock
 *
 * @author mical
 * @since 2025/8/6 18:02
 */
object ListenerBlock {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBreakBlock(e: BlockBreakEvent) {
        FileDefinedHardcodedEnchantment.execute(e.player, EventType.BLOCK_BREAK, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDamageBlock(e: BlockDamageEvent) {
        FileDefinedHardcodedEnchantment.execute(e.player, EventType.BLOCK_DAMAGE, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBlockDropItem(e: BlockDropItemEvent) {
        FileDefinedHardcodedEnchantment.execute(e.player, EventType.BLOCK_DROP_ITEM, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
    }
}