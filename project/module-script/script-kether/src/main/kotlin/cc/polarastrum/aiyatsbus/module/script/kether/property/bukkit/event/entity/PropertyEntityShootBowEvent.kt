package cc.polarastrum.aiyatsbus.module.script.kether.property.bukkit.event.entity

import cc.polarastrum.aiyatsbus.core.util.coerceBoolean
import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusGenericProperty
import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusProperty
import cc.polarastrum.aiyatsbus.module.script.kether.LiveData.Companion.liveEntity
import org.bukkit.event.entity.EntityShootBowEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity.PropertyEntityShootBowEvent
 *
 * @author mical
 * @since 2024/5/19 12:20
 */
@AiyatsbusProperty(
    id = "entity-shoot-bow-event",
    bind = EntityShootBowEvent::class
)
class PropertyEntityShootBowEvent : AiyatsbusGenericProperty<EntityShootBowEvent>("entity-shoot-bow-event") {

    override fun readProperty(instance: EntityShootBowEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "bow" -> instance.bow
            "hand" -> instance.hand
            "force" -> instance.force
            "consumable" -> instance.consumable
            "shouldConsumeItem", "should-consume-item" -> instance.shouldConsumeItem()
            "projectile" -> instance.projectile
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: EntityShootBowEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "shouldConsumeItem", "should-consume-item" -> instance.setConsumeItem(value?.coerceBoolean() ?: return OpenResult.successful())
            "projectile" -> instance.projectile = value?.liveEntity ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}