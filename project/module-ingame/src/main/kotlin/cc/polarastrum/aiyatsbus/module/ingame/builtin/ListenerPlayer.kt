package cc.polarastrum.aiyatsbus.module.ingame.builtin

import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.Builtin
import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.EventType
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import org.bukkit.block.BlockFace
import org.bukkit.event.block.Action
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * Iweleth
 * cc.polarastrum.iweleth.listener.entries.ListenerPlayer
 *
 * @author mical
 * @since 2025/8/7 00:27
 */
object ListenerPlayer {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDamage(e: PlayerItemDamageEvent) {
        Builtin.execute(e.player, EventType.ITEM_DAMAGE, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onInteractEntity(e: PlayerInteractAtEntityEvent) {
        Builtin.execute(e.player, EventType.INTERACT_ENTITY, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onFish(e: PlayerFishEvent) {
        Builtin.execute(e.player, EventType.FISH, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onInteract(e: PlayerInteractEntityEvent) {
        // 之前 BlockFace 传的是 null，但是这是一个 NotNull 形参
        // 白熊到底咋写出来的这插件？
        Builtin.execute(e.player, EventType.INTERACT_RIGHT, PlayerInteractEvent(e.player, Action.RIGHT_CLICK_BLOCK, null, null,
            BlockFace.SELF), EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
    }

    // @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onJoin(e: PlayerJoinEvent) {
        // 之前是 bloodFixer 和 speedFixer 工作的地方
    }

    // @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onQuit(e: PlayerQuitEvent) {
//        // 清理PublicTasks中的缓存，防止内存泄漏
//        hamsteryds.nereusopus.enchants.PublicTasks.clearPlayerCache(player.getUniqueId());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onInteract(e: PlayerInteractEvent) {
        if (e.hand == EquipmentSlot.OFF_HAND) return
        when (e.action) {
            Action.LEFT_CLICK_BLOCK -> {
                Builtin.execute(e.player, EventType.INTERACT_LEFT_BLOCK, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
                Builtin.execute(e.player, EventType.INTERACT_LEFT, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
            }
            Action.RIGHT_CLICK_BLOCK -> {
                Builtin.execute(e.player, EventType.INTERACT_RIGHT_BLOCK, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
                Builtin.execute(e.player, EventType.INTERACT_RIGHT, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
            }
            Action.LEFT_CLICK_AIR -> {
                Builtin.execute(e.player, EventType.INTERACT_LEFT_AIR, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
                Builtin.execute(e.player, EventType.INTERACT_LEFT, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
            }
            Action.RIGHT_CLICK_AIR -> {
                Builtin.execute(e.player, EventType.INTERACT_RIGHT_AIR, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
                Builtin.execute(e.player, EventType.INTERACT_RIGHT, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
            }
            else -> {
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onJump(e: PlayerJumpEvent) {
        Builtin.execute(e.player, EventType.PLAYER_JUMP, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onElytraBoost(e: PlayerElytraBoostEvent) {
        Builtin.execute(e.player, EventType.ELYTRA_BOOST, e,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onHunger(e: FoodLevelChangeEvent) {
        // only hunger
        if (e.foodLevel > e.entity.foodLevel) return
        Builtin.execute(e.entity, EventType.HUNGER, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPickupExp(e: PlayerPickupExperienceEvent) {
        Builtin.execute(e.player, EventType.PICK_UP_EXPERIENCE, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onHeldItem(e: PlayerItemHeldEvent) {
//        //Dexterous Revert
//        Player player = event.getPlayer();
//        player.getAttribute(XAttribute.ATTACK_SPEED.get()).setBaseValue(4);
    }
}