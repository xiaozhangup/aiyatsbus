package cc.polarastrum.aiyatsbus.core.data.trigger.builtin

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.*
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.enchantment.PrepareItemEnchantEvent
import org.bukkit.event.entity.*
import org.bukkit.event.player.*
import org.bukkit.event.weather.LightningStrikeEvent
import org.bukkit.inventory.EquipmentSlot

/**
 * 附魔事件回调接口
 *
 * 由硬编码/文件定义的内置附魔实现，用于覆盖各类事件回调。
 * 默认实现为空，实现类按需重写具体事件方法。
 *
 * @author mical
 * @since 2025/8/6 15:11
 */
@Suppress("unused")
interface EventFunctions {

    fun trigger(level: Int, type: EventType, event: Event, who: LivingEntity) {
    }

    fun attackEntity(level: Int, event: EntityDamageByEntityEvent) {
    }

    fun damagedByEntity(level: Int, event: EntityDamageByEntityEvent) {
    }

    fun damagedByBlock(level: Int, event: EntityDamageByBlockEvent) {
    }

    fun damaged(level: Int, event: EntityDamageEvent) {
    }

    fun advancementDone(level: Int, event: PlayerAdvancementDoneEvent) {
    }

    fun armorstandManipulate(level: Int, event: PlayerArmorStandManipulateEvent) {
    }

    fun bedEnter(level: Int, event: PlayerBedEnterEvent) {
    }

    fun bedLeave(level: Int, event: PlayerBedLeaveEvent) {
    }

    fun bucketEmpty(level: Int, event: PlayerBucketEmptyEvent) {
    }

    fun bucketFill(level: Int, event: PlayerBucketFillEvent) {
    }

    fun bucketEntity(level: Int, event: PlayerBucketEntityEvent) {
    }

    fun dropItem(level: Int, event: PlayerDropItemEvent) {
    }

    fun expChange(level: Int, event: PlayerExpChangeEvent) {
    }

    fun fish(level: Int, event: PlayerFishEvent) {
    }

    fun harvestBlock(level: Int, event: PlayerHarvestBlockEvent) {
    }

    fun interactEntity(level: Int, event: PlayerInteractAtEntityEvent) {
    }

    fun interactLeftBlock(level: Int, event: PlayerInteractEvent) {
    }

    fun interactLeftAir(level: Int, event: PlayerInteractEvent) {
    }

    fun interactLeft(level: Int, event: PlayerInteractEvent) {
    }

    fun interactRightBlock(level: Int, event: PlayerInteractEvent) {
    }

    fun interactRightAir(level: Int, event: PlayerInteractEvent) {
    }

    fun interactRight(level: Int, event: PlayerInteractEvent) {
    }

    fun itemBreak(level: Int, event: PlayerItemBreakEvent) {
    }

    fun itemConsume(level: Int, event: PlayerItemConsumeEvent) {
    }

    fun itemDamage(level: Int, event: PlayerItemDamageEvent) {
    }

    fun itemHeld(level: Int, event: PlayerItemHeldEvent) {
    }

    fun itemMend(level: Int, event: PlayerItemMendEvent) {
    }

    fun levelChange(level: Int, event: PlayerLevelChangeEvent) {
    }

    fun move(level: Int, event: PlayerMoveEvent) {
    }

    fun pickUpArrow(level: Int, event: PlayerPickupArrowEvent) {
    }

    fun portal(level: Int, event: PlayerPortalEvent) {
    }

    fun recipeDiscover(level: Int, event: PlayerRecipeDiscoverEvent) {
    }

    fun respawn(level: Int, event: PlayerRespawnEvent) {
    }

    fun riptide(level: Int, event: PlayerRiptideEvent) {
    }

    fun shearEntity(level: Int, event: PlayerShearEntityEvent) {
    }

    fun swapHandItems(level: Int, event: PlayerSwapHandItemsEvent) {
    }

    fun takeLecternBook(level: Int, event: PlayerTakeLecternBookEvent) {
    }

    fun teleport(level: Int, event: PlayerTeleportEvent) {
    }

    fun toggleSneak(level: Int, event: PlayerToggleSneakEvent) {
    }

    fun toggleSprint(level: Int, event: PlayerToggleSprintEvent) {
    }

    fun toggleFlight(level: Int, event: PlayerToggleFlightEvent) {
    }

    fun unleashEntity(level: Int, event: PlayerUnleashEntityEvent) {
    }

    fun blockBreak(level: Int, event: BlockBreakEvent) {
    }

    fun blockDamageAbort(level: Int, event: BlockDamageEvent) {
    }

    fun blockDamage(level: Int, event: BlockDamageEvent) {
    }

    fun blockDispenseArmor(level: Int, event: BlockDispenseArmorEvent) {
    }

    fun blockDispense(level: Int, event: BlockDispenseEvent) {
    }

    fun blockDropItem(level: Int, event: BlockDropItemEvent) {
    }

    fun blockFertilize(level: Int, event: BlockFertilizeEvent) {
    }

    fun blockMultiPlace(level: Int, event: BlockMultiPlaceEvent) {
    }

    fun blockPlace(level: Int, event: BlockPlaceEvent) {
    }

    fun signChange(level: Int, event: SignChangeEvent) {
    }

    fun notePlay(level: Int, event: NotePlayEvent) {
    }

    fun enchantItem(level: Int, event: EnchantItemEvent) {
    }

    fun prepareItemEnchant(level: Int, event: PrepareItemEnchantEvent) {
    }

    fun lightningStrike(level: Int, event: LightningStrikeEvent) {
    }

    fun tickTask(level: Int, slot: EquipmentSlot, player: Player, stamp: Int) {
    }

    fun shootBow(level: Int, event: EntityShootBowEvent) {
    }

    fun death(level: Int, event: EntityDeathEvent) {
    }

    fun projectileLaunch(level: Int, event: ProjectileLaunchEvent) {
    }

    fun projectileHitBlock(level: Int, event: ProjectileHitEvent) {
    }

    fun projectileHitEntity(level: Int, event: ProjectileHitEvent) {
    }

    fun kill(level: Int, event: EntityDeathEvent) {
    }

    fun hunger(level: Int, event: FoodLevelChangeEvent) {
    }

    fun regainHealth(level: Int, event: EntityRegainHealthEvent) {
    }

    fun beTargeted(level: Int, event: EntityTargetLivingEntityEvent) {
    }
}
