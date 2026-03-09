package cc.polarastrum.aiyatsbus.module.ingame.builtin

import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.Builtin
import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.EventType
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
        Builtin.execute(e.player, EventType.BLOCK_BREAK, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDamageBlock(e: BlockDamageEvent) {
        Builtin.execute(e.player, EventType.BLOCK_DAMAGE, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBlockDropItem(e: BlockDropItemEvent) {
        Builtin.execute(e.player, EventType.BLOCK_DROP_ITEM, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
    }
}