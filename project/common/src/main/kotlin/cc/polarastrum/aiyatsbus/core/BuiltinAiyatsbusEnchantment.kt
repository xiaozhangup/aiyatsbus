package cc.polarastrum.aiyatsbus.core

import cc.polarastrum.aiyatsbus.core.data.*
import cc.polarastrum.aiyatsbus.core.data.trigger.Mechanism
import cc.polarastrum.aiyatsbus.core.data.trigger.TriggerType
import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.Builtin
import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.EventFunctions
import cc.polarastrum.aiyatsbus.core.data.trigger.ticker.BuiltinTicker
import org.bukkit.entity.LivingEntity
import taboolib.module.configuration.Configuration

/**
 * 硬编码附魔，
 * 不会被当作 Aiyatsbus 内部附魔，而是外部附魔
 *
 * @author mical
 * @since 2026/3/5 22:04
 */
class BuiltinAiyatsbusEnchantment(
    id: String,
    config: Configuration,
    eventExecutor: EventFunctions
) : AiyatsbusEnchantmentBase(id, null, config), EventFunctions by eventExecutor {

    override val mechanism: Mechanism = Mechanism(null, this)

    class Builder {

        private var basicData: BasicData? = null
        private var alternativeData: AlternativeData? = null
        private var displayer: Displayer? = null
        private var rarity: String = AiyatsbusSettings.defaultRarity
        private var limitations: MutableList<String> = mutableListOf()
        private var targets: MutableList<String> = mutableListOf()
        private var variables: MutableMap<VariableType, MutableMap<String, String>> = mutableMapOf()

        private var eventExecutor: EventFunctions = object : EventFunctions {
        }
        
        private class TickerData(
            val id: String,
            val interval: Long,
            val preHandle: ((LivingEntity, MutableMap<String, Any?>) -> Unit)?,
            val handle: ((LivingEntity, MutableMap<String, Any?>) -> Unit)?,
            val postHandle: ((LivingEntity, MutableMap<String, Any?>) -> Unit)?
        )

        private var customTickers: MutableList<TickerData> = mutableListOf()

        fun basicData(basicData: BasicData): Builder {
            this.basicData = basicData
            return this
        }

        fun alternativeData(alternativeData: AlternativeData): Builder {
            this.alternativeData = alternativeData
            return this
        }

        fun displayer(displayer: Displayer): Builder {
            this.displayer = displayer
            return this
        }

        fun rarity(rarity: String): Builder {
            this.rarity = rarity
            return this
        }

        fun addTarget(target: String): Builder {
            this.targets += target
            return this
        }

        fun removeTarget(target: String): Builder {
            this.targets -= target
            return this
        }

        fun targets(vararg targets: String): Builder {
            this.targets = targets.toMutableList()
            return this
        }

        fun targets(targets: MutableList<String>): Builder {
            this.targets = targets
            return this
        }

        fun eventExecutor(event: EventFunctions): Builder {
            this.eventExecutor = event
            return this
        }

        fun addTicker(
            id: String,
            interval: Long,
            preHandle: ((LivingEntity, MutableMap<String, Any?>) -> Unit)? = null,
            handle: ((LivingEntity, MutableMap<String, Any?>) -> Unit)? = null,
            postHandle: ((LivingEntity, MutableMap<String, Any?>) -> Unit)? = null
        ): Builder {
            this.customTickers.add(TickerData(id, interval, preHandle, handle, postHandle))
            return this
        }

        fun addLimitation(type: LimitType, value: String): Builder {
            this.limitations += "${type.name}:$value"
            return this
        }

        fun removeLimitation(type: LimitType, value: String): Builder {
            this.limitations -= "${type.name}:$value"
            return this
        }

        fun addVariable (type: VariableType, name: String, value: String, unit: String = ""): Builder {
            val map = this.variables.getOrPut(type) { mutableMapOf() }
            map[name] = "$unit:$value"
            return this
        }

        fun removeVariable(type: VariableType, vararg variables: String): Builder {
            val map = this.variables[type] ?: return this
            variables.forEach { map.remove(it) }
            return this
        }

        fun limitations(vararg value: String): Builder {
            this.limitations = value.toMutableList()
            return this
        }

        fun limitations(values: MutableList<String>): Builder {
            this.limitations = values
            return this
        }

        fun build(): BuiltinAiyatsbusEnchantment {
            val config = Configuration.Companion.empty()
            config["basic"] = basicData!!.serialize()
            if (alternativeData != null) {
                config["alternative"] = alternativeData!!.serialize()
            }
            config["display"] = displayer!!.serialize()
            config["rarity"] = rarity
            config["targets"] = targets
            config["limitations"] = limitations
            for ((type, variable) in variables) {
                for ((name, value) in variable) {
                    config["variables.${type.name.lowercase()}.${name}"] = value
                }
            }
            return BuiltinAiyatsbusEnchantment(basicData!!.id, config, eventExecutor).apply {
                mechanism.addTrigger(TriggerType.BUILTIN, object : Builtin(), EventFunctions by eventExecutor { })
                customTickers.forEach { tData ->
                    val ticker = BuiltinTicker(this, tData.id, tData.preHandle, tData.handle, tData.postHandle, tData.interval)
                    ticker.init()
                    mechanism.addTrigger(TriggerType.TICKER, ticker)
                }
            }
        }

        /**
         * 生成并注册
         */
        fun register(): BuiltinAiyatsbusEnchantment {
            return build().also { Aiyatsbus.api().getEnchantmentManager().register(it) }
        }
    }


    companion object {

        @JvmStatic
        fun builder() = Builder()
    }
}