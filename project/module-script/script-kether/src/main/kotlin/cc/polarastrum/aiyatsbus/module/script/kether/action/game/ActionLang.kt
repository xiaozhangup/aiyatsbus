package cc.polarastrum.aiyatsbus.module.script.kether.action.game

import cc.polarastrum.aiyatsbus.core.sendLang
import org.bukkit.entity.Entity
import taboolib.module.kether.KetherParser
import taboolib.module.kether.ParserHolder.option
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionLang
 *
 * @author mical
 * @since 2024/7/14 12:41
 */
object ActionLang {

    /**
     * send-lang enchant-impact-damaged to &event[entity] with array [ entity-name &event[attacker] ]
     */
    @Suppress("UNCHECKED_CAST")
    @KetherParser(["send-lang"])
    fun sendLangParser() = combinationParser {
        it.group(text(), command("to", then = type<Entity>()), command("with", then = anyAsList()).option()).apply(it) { node, to, args ->
            now {
                to.sendLang(node, args = args?.map { it.toString() }?.toTypedArray() ?: emptyArray())
            }
        }
    }
}