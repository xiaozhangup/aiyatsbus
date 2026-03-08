package cc.polarastrum.aiyatsbus.core.enchant

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.InternalEnchantment
import cc.polarastrum.aiyatsbus.core.data.CheckType
import cc.polarastrum.aiyatsbus.core.data.trigger.Mechanism
import cc.polarastrum.aiyatsbus.core.fastFixedEnchants
import cc.polarastrum.aiyatsbus.core.util.camelToSnake
import cc.polarastrum.aiyatsbus.core.util.isNull
import cc.polarastrum.aiyatsbus.core.util.random
import org.bukkit.Material
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common5.Baffle
import taboolib.module.configuration.Configuration
import java.io.File
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * 此附魔作为内部附魔注册，
 * 其他插件无需自行实现附魔文件读取加载，
 * 只需要作为附魔包释放到 Aiyatsbus 的附魔目录即可。
 *
 * @author mical
 * @since 2026/3/5 19:33
 */
open class FileDefinedHardcodedEnchantment(
    id: String,
    file: File,
    config: Configuration
) : AiyatsbusEnchantmentBase(id, file, config), EventFunctions, InternalEnchantment {

    override val mechanism: Mechanism? = null

    /**
     * 应该是给 Ticker 用的，现在还没有用到。
     */
    open fun call(level: Int, type: EventType, entity: LivingEntity, event: Event) {
        executors[type]?.invoke(this, level, event)
        executors[EventType.TRIGGER]?.invoke(this, level, type, event, entity)
    }

    companion object {

        private val baffle = Baffle.of(150, TimeUnit.MILLISECONDS)

        val executors = ConcurrentHashMap<EventType, Method>()

        @Awake(LifeCycle.ENABLE)
        fun trigger() {
            executors.putAll(EventFunctions::class.java.declaredMethods.associateBy { method ->
                EventType.valueOf(method.name.camelToSnake()) }
            )
        }

//        fun execute(entity: Entity, type: EventType, event: Event, slot: TriggerSlots) {
//            execute(entity, type, event, *slot.slots.toTypedArray())
//        }

        fun execute(entity: Entity, type: EventType, event: Event, vararg slots: EquipmentSlot) {
            if (entity !is LivingEntity) return
            for (slot in slots) {
                val item = entity.equipment?.getItem(slot)
                if (item.isNull) continue
                execute(entity, item!!, type, event, slot)
            }
        }

        /**
         * 适用于三叉戟这种触发时物品不在手上的附魔
         */
//        fun execute(entity: Entity, type: EventType, event: Event, item: ItemStack, slot: TriggerSlots) {
//            execute(entity, type, event, item, *slot.slots.toTypedArray())
//        }

        /**
         * 适用于三叉戟这种触发时物品不在手上的附魔
         */
        fun execute(entity: Entity, type: EventType, event: Event, item: ItemStack, vararg slots: EquipmentSlot) {
            if (entity !is LivingEntity) return
            for (slot in slots) {
                execute(entity, item, type, event, slot)
            }
        }

        fun execute(entity: Entity, item: ItemStack, type: EventType, event: Event, slot: EquipmentSlot) {
            entity as LivingEntity
            if (item.fastFixedEnchants.isEmpty()) return
//            if (checkEvents.contains(event)) return
            if (entity is Player) {
                if ((type == EventType.BLOCK_BREAK || type == EventType.DAMAGED_BY_ENTITY) && "HOE" !in item.type.name) {
                    if (!baffle.hasNext(entity.uniqueId.toString())) return
                    baffle.next(entity.uniqueId.toString())
                }
            }
            if ((item.type == Material.BOW || item.type == Material.CROSSBOW) && event is EntityDamageByEntityEvent) {
                if (event.damager !is AbstractArrow) return
            }
            for ((enchant, level) in item.fastFixedEnchants) {
                val et = enchant as? FileDefinedHardcodedEnchantment ?: continue
                et as AiyatsbusEnchantment
                level as Int
                val chance = when {
                    et.variables.leveled.contains("chance") -> et.variables.leveled("chance", level, false) as Double
                    et.variables.leveled.contains("概率") -> et.variables.leveled("概率", level, false) as Double
                    else -> 100.0
                }
                if (et.basicData.enable &&
                    random(chance) &&
                    et.limitations.checkAvailable(CheckType.USE, item, entity, slot).isSuccess
                ) {
                    et.call(level, type, entity, event)
                }
            }
        }
    }
}