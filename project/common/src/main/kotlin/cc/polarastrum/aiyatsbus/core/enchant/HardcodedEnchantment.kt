package cc.polarastrum.aiyatsbus.core.enchant

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.AiyatsbusSettings
import cc.polarastrum.aiyatsbus.core.data.AlternativeData
import cc.polarastrum.aiyatsbus.core.data.BasicData
import cc.polarastrum.aiyatsbus.core.data.Displayer
import cc.polarastrum.aiyatsbus.core.data.LimitType
import cc.polarastrum.aiyatsbus.core.data.trigger.Mechanism
import taboolib.module.configuration.Configuration

/**
 * 非内部硬编码附魔
 *
 * @author mical
 * @since 2026/3/5 22:04
 */
class HardcodedEnchantment(
    id: String,
    config: Configuration,
    eventExecutor: EventFunctions
) : AiyatsbusEnchantmentBase(id, null, config), EventFunctions by eventExecutor {

    override val mechanism: Mechanism? = null

    class Builder {

        private var basicData: BasicData? = null
        private var alternativeData: AlternativeData? = null
        private var displayer: Displayer? = null
        private var rarity: String = AiyatsbusSettings.defaultRarity
        private var limitations: MutableList<String> = mutableListOf()
        private var targets: MutableList<String> = mutableListOf()

        private var eventExecutor: EventFunctions = object : EventFunctions {
        }

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

        fun addLimitation(type: LimitType, value: String): Builder {
            this.limitations += "${type.name}:$value"
            return this
        }

        fun removeLimitation(type: LimitType, value: String): Builder {
            this.limitations -= "${type.name}:$value"
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

        fun build(): HardcodedEnchantment {
            val config = Configuration.empty()
            config["basic"] = basicData!!.serialize()
            if (alternativeData != null) {
                config["alternative"] = alternativeData!!.serialize()
            }
            config["displayer"] = displayer!!.serialize()
            config["rarity"] = rarity
            config["targets"] = targets
            config["limitations"] = limitations
            return HardcodedEnchantment(basicData!!.id, config, eventExecutor)
        }

        /**
         * 生成并注册
         */
        fun register(): HardcodedEnchantment {
            return build().also { Aiyatsbus.api().getEnchantmentManager().register(it) }
        }
    }


    companion object {

        @JvmStatic
        fun builder() = Builder()
    }
}
