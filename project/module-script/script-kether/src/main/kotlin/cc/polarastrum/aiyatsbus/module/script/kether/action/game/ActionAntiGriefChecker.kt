package cc.polarastrum.aiyatsbus.module.script.kether.action.game

import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker
import cc.polarastrum.aiyatsbus.module.script.kether.AiyatsbusParser
import cc.polarastrum.aiyatsbus.module.script.kether.aiyatsbus
import taboolib.platform.util.toBukkitLocation

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionAntiGriefChecker
 *
 * @author mical
 * @since 2024/3/23 18:08
 */
object ActionAntiGriefChecker {

    @AiyatsbusParser(["anti-grief-checker", "anti-grief", "agc"])
    fun antiGriefChecker() = aiyatsbus {
        when (nextToken()) {
            "can-break" -> {
                combine(player(), location()) { p, l ->
                    AntiGriefChecker.canBreak(p, l.toBukkitLocation())
                }
            }
            "can-damage" -> {
                combine(player(), entity()) { p, e ->
                    AntiGriefChecker.canDamage(p, e)
                }
            }
            else -> error("unknown operation")
        }
    }
}