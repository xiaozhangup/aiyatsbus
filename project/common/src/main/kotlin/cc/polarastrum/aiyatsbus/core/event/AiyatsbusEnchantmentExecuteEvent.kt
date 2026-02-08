package cc.polarastrum.aiyatsbus.core.event

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.data.trigger.Trigger
import cc.polarastrum.aiyatsbus.core.data.trigger.TriggerType
import cc.polarastrum.aiyatsbus.core.data.trigger.ticker.Ticker
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.core.event.AiyatsbusEnchantmentExecuteEvent
 *
 * @author mical
 * @since 2026/2/8 22:07
 */
data class AiyatsbusEnchantmentExecuteEvent(
    val executor: LivingEntity,
    val trigger: Trigger,
    val type: TriggerType,
    val script: String,
    val variableMap: MutableMap<String, Any?>
) : BukkitProxyEvent() {

    override val allowCancelled: Boolean = true

    var level: Int
        get() = variableMap["level"] as Int
        set(value) {
            variableMap.put("level", value)
        }

    var item: ItemStack?
        get() = variableMap["item"] as ItemStack?
        set(value) {
            variableMap.put("item", value)
        }

    var enchant: AiyatsbusEnchantment
        get() = variableMap["enchant"] as AiyatsbusEnchantment
        set(value) {
            variableMap.put("enchant", value)
        }

    /**
     * 必须先判断触发器类型再获取
     */
    var event: Event?
        get() = variableMap["event"] as Event?
        set(value) {
            variableMap.put("event", value)
        }

    var cooldown: Double?
        get() = variableMap["cooldown"] as Double?
        set(value) {
            variableMap.put("cooldown", value)
        }

    var slot: EquipmentSlot?
        get() = variableMap["slot"] as EquipmentSlot?
        set(value) {
            variableMap.put("slot", value)
        }

    /**
     * 只适用于 Ticker
     */
    fun isPreHandle(): Boolean {
        return trigger is Ticker && script == trigger.preHandle
    }

    /**
     * 一般是用于 Ticker
     */
    fun isHandle(): Boolean {
        return script == trigger.handle
    }

    /**
     * 只适用于 Ticker
     */
    fun isPostHandle(): Boolean {
        return trigger is Ticker && script == trigger.postHandle
    }
}