package cc.polarastrum.aiyatsbus.module.script.kether.property.bukkit.event.block

import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusGenericProperty
import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusProperty
import cc.polarastrum.aiyatsbus.module.script.kether.LiveData.Companion.liveItemStack
import org.bukkit.event.block.BlockCookEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockCookEvent
 *
 * @author yanshiqwq
 * @since 2024/4/1 20:50
 */
@AiyatsbusProperty(
    id = "block-cook-event",
    bind = BlockCookEvent::class
)
class PropertyBlockCookEvent : AiyatsbusGenericProperty<BlockCookEvent>("block-cook-event") {

    override fun readProperty(instance: BlockCookEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "source" -> instance.source
            "result" -> instance.result
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockCookEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "result" -> instance.result = value?.liveItemStack ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}