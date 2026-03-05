package cc.polarastrum.aiyatsbus.module.script.kether.action.game

import cc.polarastrum.aiyatsbus.module.script.kether.operation.Operations
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionOperation
 *
 * @author mical
 * @since 2024/3/10 15:33
 */
object ActionOperation {

    /**
     * operation xxx args [ &int &text ]
     */
    @KetherParser(["operation"])
    fun operationParser() = combinationParser {
        it.group(text(), command("args", then = anyAsList()).option()).apply(it) { operation, args ->
            now {
                Operations.registered[operation]?.apply(args)
            }
        }
    }
}