package cc.polarastrum.aiyatsbus.module.script.kether.property.bukkit.event.entity

import cc.polarastrum.aiyatsbus.core.util.coerceInt
import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusGenericProperty
import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusProperty
import org.bukkit.event.entity.FoodLevelChangeEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity.PropertyFoodLevelChangeEvent
 *
 * @author mical
 * @date 2024/8/19 21:48
 */
@AiyatsbusProperty(
    id = "food-level-change-event",
    bind = FoodLevelChangeEvent::class,
)
class PropertyFoodLevelChangeEvent : AiyatsbusGenericProperty<FoodLevelChangeEvent>("food-level-change") {

    override fun readProperty(instance: FoodLevelChangeEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "entity" -> instance.entity
            "item" -> instance.item
            "foodLevel", "food-level" -> instance.foodLevel
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: FoodLevelChangeEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "foodLevel", "food-level" -> instance.foodLevel = value?.coerceInt() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}