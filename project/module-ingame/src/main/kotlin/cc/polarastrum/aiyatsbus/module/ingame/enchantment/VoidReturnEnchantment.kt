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

object VoidReturnEnchantment {

    private lateinit var enchantment: BuiltinAiyatsbusEnchantment

    @Awake(LifeCycle.LOAD)
    fun register() {
        enchantment = BuiltinAiyatsbusEnchantment.builder()
            .basicData(BasicData.builder().id("void_return").name("闪回").maxLevel(1).build())
            .rarity("史诗")
            .targets("靴子")
            .displayer(
                Displayer.builder()
                    .generalDescription("&7跌入虚空时拉回世界出生点")
                    .specificDescription("&7跌入虚空时拉回世界出生点")
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
                    if (event.cause != EntityDamageEvent.DamageCause.VOID) return

                    event.isCancelled = true
                    val spawn = player.world.spawnLocation.clone()
                    player.fallDistance = 0f
                    player.teleport(spawn)
                    player.fallDistance = 0f
                }
            })
            .register()
    }
}
