package cc.polarastrum.aiyatsbus.module.script.kether

import cc.polarastrum.aiyatsbus.core.script.ScriptHandler
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.parseKetherScript
import taboolib.module.kether.runKether

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.module.script.kether.KetherScriptHandler
 *
 * @author mical
 * @since 2025/6/22 13:23
 */
class KetherScriptHandler : ScriptHandler {

    override fun invoke(
        source: String,
        id: String,
        sender: CommandSender?,
        variables: Map<String, Any?>
    ): Any? {
        val player = sender as? Player
        return runKether(detailError = true) {
            KetherShell.eval(source,
                ScriptOptions.builder().namespace(namespace = listOf("aiyatsbus"))
                    .sender(sender = if (player != null) adaptPlayer(player) else if (sender != null) adaptCommandSender(sender) else console())
                    .vars(variables)
                    .build())
        }
    }

    override fun preheat(source: String, id: String) {
        val s = if (source.startsWith("def ")) source else "def main = { $source }"
        KetherShell.mainCache.scriptMap[s] = s.parseKetherScript(listOf("aiyatsbus"))
    }
}