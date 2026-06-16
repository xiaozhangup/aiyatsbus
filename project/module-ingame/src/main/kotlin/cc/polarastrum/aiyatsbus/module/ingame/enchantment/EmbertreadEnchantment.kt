package cc.polarastrum.aiyatsbus.module.ingame.enchantment

import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.fastEtLevel
import cc.polarastrum.aiyatsbus.core.data.AlternativeData
import cc.polarastrum.aiyatsbus.core.data.BasicData
import cc.polarastrum.aiyatsbus.core.data.Displayer
import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.EventFunctions
import org.bukkit.Tag
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object EmbertreadEnchantment {

    private lateinit var enchantment: BuiltinAiyatsbusEnchantment

    @Awake(LifeCycle.LOAD)
    fun register() {
        enchantment = BuiltinAiyatsbusEnchantment.builder()
            .basicData(BasicData.builder().id("embertread").name("炙足").maxLevel(1).build())
            .rarity("珍奇")
            .targets("靴子")
            .displayer(
                Displayer.builder()
                    .generalDescription("&7免疫岩浆块与火焰伤害")
                    .specificDescription("&7免疫岩浆块与火焰伤害")
                    .build()
            )
            .alternativeData(
                AlternativeData.builder()
                    .weight(2)
                    .build()
            )
            .eventExecutor(object : EventFunctions {
                override fun damaged(level: Int, event: EntityDamageEvent) {
                    val player = event.entity as? Player ?: return
                    val boots = player.inventory.boots ?: return

                    if (!Tag.ITEMS_FOOT_ARMOR.isTagged(boots.type)) return
                    if (boots.fastEtLevel(enchantment) <= 0) return

                    when (event.cause) {
                        EntityDamageEvent.DamageCause.HOT_FLOOR,
                        EntityDamageEvent.DamageCause.FIRE -> event.isCancelled = true
                        else -> return
                    }
                }
            })
            .register()
    }
}
