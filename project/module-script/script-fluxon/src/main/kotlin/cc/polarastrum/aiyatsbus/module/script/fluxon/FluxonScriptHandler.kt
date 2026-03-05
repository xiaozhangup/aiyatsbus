package cc.polarastrum.aiyatsbus.module.script.fluxon

import cc.polarastrum.aiyatsbus.core.script.ScriptHandler
import cc.polarastrum.aiyatsbus.module.script.fluxon.handler.FluxonHandler
import org.bukkit.command.CommandSender

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.module.script.fluxon.FluxonScriptHandler
 *
 * @author mical
 * @since 2025/6/22 13:24
 */
class FluxonScriptHandler : ScriptHandler {

    override fun invoke(
        source: String,
        id: String,
        sender: CommandSender?,
        variables: Map<String, Any?>
    ): Any? {
        return fluxonHandler.invoke(source, id, sender, variables)
    }

    override fun preheat(source: String, id: String) {
        return fluxonHandler.preheat(source, id)
    }

    companion object {

        val DEFAULT_PACKAGE_AUTO_IMPORT = mutableSetOf<String>()

        lateinit var fluxonHandler: FluxonHandler
    }
}