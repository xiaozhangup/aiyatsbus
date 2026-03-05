package cc.polarastrum.aiyatsbus.module.ingame.trigger

import cc.polarastrum.aiyatsbus.core.enchant.EventType
import cc.polarastrum.aiyatsbus.core.enchant.FileDefinedHardcodedEnchantment
import cc.polarastrum.aiyatsbus.core.util.isNull
import com.destroystokyo.paper.event.entity.EntityJumpEvent
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.FishHook
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.entity.Trident
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import java.util.UUID

/**
 * Iweleth
 * cc.polarastrum.iweleth.listener.entries.ListenerEntity
 *
 * @author mical
 * @since 2025/8/6 16:54
 */
object ListenerEntity {

    private val projectiles = HashMap<UUID, ItemStack?>()

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDamage(e: EntityDamageEvent) {
        FileDefinedHardcodedEnchantment.execute(e.entity, EventType.DAMAGED, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onTarget(e: EntityTargetLivingEntityEvent) {
        FileDefinedHardcodedEnchantment.execute(e.entity, EventType.BE_TARGETED, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    // @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onSpawnItem(e: ItemSpawnEvent) {
        /*
        Item item = event.getEntity();
        if (item.getThrower() != null) {
            NBTUtils.write("thrown", item.getPersistentDataContainer(), PersistentDataType.INTEGER, 1);
        }
         */
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDamageByBlock(e: EntityDamageByBlockEvent) {
        FileDefinedHardcodedEnchantment.execute(e.entity, EventType.DAMAGED_BY_BLOCK, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDamageByEntity(e: EntityDamageByEntityEvent) {
        // 2025/12/28 小张：为什么要忽视被横扫和风暴的伤害事件？
        // if (e.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return
        // if (e.cause == EntityDamageEvent.DamageCause.THORNS) return

        var damager = e.damager
        val damagee = e.entity
        if (damager is Projectile) {
            val projectile = damager
            if (projectile.shooter is LivingEntity) {
                damager = damager.shooter as LivingEntity
            }
            if (projectile is AbstractArrow || projectile is FishHook) {
                val item = projectiles[projectile.uniqueId]
                if (!item.isNull) {
                    FileDefinedHardcodedEnchantment.execute(damager, item!!, EventType.ATTACK_ENTITY, e, EquipmentSlot.HAND)
                }
            }
            if (projectile is Trident) {
                FileDefinedHardcodedEnchantment.execute(damager, projectile.item, EventType.ATTACK_ENTITY, e,
                    EquipmentSlot.HAND)
            }
        } else {
            FileDefinedHardcodedEnchantment.execute(damager, EventType.ATTACK_ENTITY, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
        }
        FileDefinedHardcodedEnchantment.execute(damagee as? LivingEntity ?: return, EventType.DAMAGED_BY_ENTITY, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onLaunchProjectile(e: EntityShootBowEvent) {
        FileDefinedHardcodedEnchantment.execute(e.entity, EventType.SHOOT_BOW, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
        projectiles += e.projectile.uniqueId to e.bow
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDeath(e: EntityDeathEvent) {
        FileDefinedHardcodedEnchantment.execute(e.entity, EventType.DEATH, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onJump(e: EntityJumpEvent) {
        FileDefinedHardcodedEnchantment.execute(e.entity, EventType.ENTITY_JUMP, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onProjectileLaunch(e: ProjectileLaunchEvent) {
        FileDefinedHardcodedEnchantment.execute(e.entity.shooter as? LivingEntity ?: return, EventType.PROJECTILE_LAUNCH, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onRegainHealth(e: EntityRegainHealthEvent) {
        FileDefinedHardcodedEnchantment.execute(e.entity, EventType.REGAIN_HEALTH, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onProjectileHit(e: ProjectileHitEvent) {
        if (e.entity.shooter is LivingEntity) {
            if (e.entity is Trident) {
                val trident = e.entity as Trident
                if (e.hitBlock != null) {
                    FileDefinedHardcodedEnchantment.execute(e.entity.shooter as LivingEntity, EventType.PROJECTILE_HIT_BLOCK, e, trident.item,
                        EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
                }
                if (e.hitEntity != null) {
                    FileDefinedHardcodedEnchantment.execute(e.entity.shooter as LivingEntity, EventType.PROJECTILE_HIT_ENTITY, e, trident.item,
                        EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
                }
            } else {
                if (e.hitBlock != null) {
                    FileDefinedHardcodedEnchantment.execute(e.entity.shooter as LivingEntity, EventType.PROJECTILE_HIT_BLOCK, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
                }
                if (e.hitEntity != null) {
                    FileDefinedHardcodedEnchantment.execute(e.entity.shooter as LivingEntity, EventType.PROJECTILE_HIT_ENTITY, e, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onKill(e: EntityDeathEvent) {
        val event = e.entity.lastDamageCause
        if (event is EntityDamageByEntityEvent) {
            if (event.damager is LivingEntity) {
                FileDefinedHardcodedEnchantment.execute(event.damager, EventType.KILL, event, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
            }
            if (event.damager is Projectile) {
                FileDefinedHardcodedEnchantment.execute((event.damager as Projectile).shooter as? LivingEntity ?: return, EventType.KILL, event, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)
            }
        }
    }
}