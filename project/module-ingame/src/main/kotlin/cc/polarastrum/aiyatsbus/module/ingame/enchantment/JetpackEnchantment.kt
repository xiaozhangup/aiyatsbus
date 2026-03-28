package cc.polarastrum.aiyatsbus.module.ingame.enchantment

import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.data.BasicData
import cc.polarastrum.aiyatsbus.core.data.Displayer
import org.bukkit.Effect
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info

object JetpackEnchantment {
    private val thrustLevel = mapOf(
        1 to 0.35,
        2 to 0.45,
        3 to 0.5,
        4 to 0.60,
        5 to 0.7
    )

    @Awake(LifeCycle.LOAD)
    fun register() {
        BuiltinAiyatsbusEnchantment.builder()
            .basicData(BasicData.builder().id("jetpack").name("喷气").maxLevel(5).build())
            .rarity("异宝")
            .targets("胸甲")
            .displayer(
                Displayer.builder()
//                    .generalDescription("&7挖掘时以破坏的方块为中心，同时破坏&a{范围}x{范围}x{范围}&7范围内的方块")
//                    .specificDescription("&7同时挖掘&a{范围}x{范围}x{范围}&7的方块")
                    .generalDescription("1")
                    .specificDescription("2")
                    .build()
            )
            .addTicker(
                "jetpack",
                3,
                handle = { _, map ->
                    info("AAAAA")
                    val p = map["player"] as? Player ?: return@addTicker
                    val item = map["item"] as? ItemStack ?: return@addTicker
                    val level = map["level"] as? Int ?: 1

                    if (p.isSneaking) {
                        p.world.playEffect(p.location, Effect.SMOKE, 1, 1)
                        p.fallDistance = 0f
                        val vector = Vector(0, 1, 0)
                        vector.multiply(thrustLevel[level] ?: 0.3)
                        vector.add(p.eyeLocation.getDirection().multiply(0.2f))

                        p.velocity = vector
                    }
                }
            )
//            .addVariable(VariableType.LEVELED, "范围", "{level}*2+1")
            .register()
    }
}