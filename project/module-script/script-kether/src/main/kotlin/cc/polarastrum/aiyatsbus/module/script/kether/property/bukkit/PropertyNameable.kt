package cc.polarastrum.aiyatsbus.module.script.kether.property.bukkit

import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusGenericProperty
import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusProperty
import org.bukkit.Nameable
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.PropertyNameable
 *
 * @author yanshiqwq
 * @since 2024/4/1 01:21
 */
@AiyatsbusProperty(
    id = "nameable",
    bind = Nameable::class
)
class PropertyNameable : AiyatsbusGenericProperty<Nameable>("nameable") {

    override fun readProperty(instance: Nameable, key: String): OpenResult {
        val property: Any? = when (key) {
            "customName", "custom-name" -> instance.customName
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }
    override fun writeProperty(instance: Nameable, key: String, value: Any?): OpenResult {
        when (key) {
            "customName", "custom-name" -> instance.customName = value.toString()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}