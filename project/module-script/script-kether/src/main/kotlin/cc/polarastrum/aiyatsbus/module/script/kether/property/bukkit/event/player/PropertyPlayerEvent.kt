package cc.polarastrum.aiyatsbus.module.script.kether.property.bukkit.event.player

import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusGenericProperty
import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusProperty
import org.bukkit.event.player.PlayerEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerEvent
 *
 * @author mical
 * @since 2024/3/11 22:30
 */
@AiyatsbusProperty(
    id = "player-event",
    bind = PlayerEvent::class
)
class PropertyPlayerEvent : AiyatsbusGenericProperty<PlayerEvent>("player-event") {

    override fun readProperty(instance: PlayerEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "player" -> instance.player
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}