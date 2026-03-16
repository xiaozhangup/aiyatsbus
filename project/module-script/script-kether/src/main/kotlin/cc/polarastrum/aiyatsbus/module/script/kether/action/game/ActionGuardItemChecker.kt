package cc.polarastrum.aiyatsbus.module.script.kether.action.game

import cc.polarastrum.aiyatsbus.core.compat.GuardItemChecker
import org.bukkit.entity.Item
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.game.ActionGuardItemChecker
 *
 * @author mical
 * @since 2024/8/19 10:19
 */
object ActionGuardItemChecker {

    @KetherParser(["is-guard-item"], shared = true)
    fun guardItemParser() = combinationParser {
        it.group(type<Item>()).apply(it) { item -> now { GuardItemChecker.checkIsGuardItem(item, null) } }
    }
}