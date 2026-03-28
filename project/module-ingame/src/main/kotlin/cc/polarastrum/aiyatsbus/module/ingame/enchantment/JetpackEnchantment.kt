package cc.polarastrum.aiyatsbus.module.ingame.enchantment

import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.data.BasicData
import cc.polarastrum.aiyatsbus.core.data.Displayer
import cc.polarastrum.aiyatsbus.core.data.VariableType
import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.EventFunctions
import org.bukkit.Effect
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.util.Vector
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object JetpackEnchantment {
    private val thrustLevel = mapOf(
        1 to 0.3,
        2 to 0.4,
        3 to 0.5,
        4 to 0.6,
        5 to 0.7
    )

    private val expDrainCounter = ConcurrentHashMap<UUID, Int>()

    @Awake(LifeCycle.LOAD)
    fun register() {
        BuiltinAiyatsbusEnchantment.builder()
            .basicData(BasicData.builder().id("jetpack").name("喷气").maxLevel(5).build())
            .rarity("异宝")
            .targets("胸甲")
            .displayer(
                Displayer.builder()
                    .generalDescription("&7蹲下时消耗经验并获得&a{力度}&7的升力")
                    .specificDescription("&7每&a{等级}&7次消耗&a1&7经验并获得&a{力度}&7升力")
                    .build()
            )
            .addTicker(
                "jetpack",
                3,
                handle = { _, map ->
                    val player = map["player"] as? Player ?: return@addTicker
                    val level = map["level"] as? Int ?: 1

                    if (player.isSneaking) {
                        push(player, level)
                    }
                },
                postHandle = { _, map ->
                    val player = map["player"] as? Player ?: return@addTicker
                    expDrainCounter.remove(player.uniqueId)
                },
                preHandle = { _, map ->
                    val player = map["player"] as? Player ?: return@addTicker
                    expDrainCounter.remove(player.uniqueId)
                }
            )
            .eventExecutor(object : EventFunctions {
                override fun toggleSneak(level: Int, event: PlayerToggleSneakEvent) {
                    if (event.isSneaking) {
                        push(event.player, level)
                    }
                }
            })
            .addVariable(VariableType.LEVELED, "力度", "{level}*0.1+0.2")
            .addVariable(VariableType.LEVELED, "等级", "{level}")
            .register()
    }

    private fun push(player: Player, level: Int) {
        val interval = level.coerceIn(1, 5)
        val uuid = player.uniqueId
        val count = expDrainCounter.compute(uuid) { _, old -> (old ?: 0) + 1 } ?: 1

        if (count >= interval) {
            expDrainCounter[uuid] = 0
            if (player.level <= 0 && player.exp <= 0f) {
                return
            }
            player.giveExp(-1)
        }

        player.world.playEffect(player.location, Effect.SMOKE, 1, 1)
        player.fallDistance = 0f
        val vector = Vector(0, 1, 0)
        vector.multiply(thrustLevel[level] ?: 0.3)
        vector.add(player.eyeLocation.getDirection().multiply(0.2f))

        player.velocity = vector
    }
}