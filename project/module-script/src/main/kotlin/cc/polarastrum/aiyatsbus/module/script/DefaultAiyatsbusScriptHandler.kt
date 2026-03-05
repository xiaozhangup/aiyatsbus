package cc.polarastrum.aiyatsbus.module.script

import cc.polarastrum.aiyatsbus.core.script.AiyatsbusScriptHandler
import cc.polarastrum.aiyatsbus.core.script.ScriptHandler
import cc.polarastrum.aiyatsbus.core.script.ScriptType
import cc.polarastrum.aiyatsbus.module.script.fluxon.FluxonScriptHandler
import cc.polarastrum.aiyatsbus.module.script.kether.KetherScriptHandler
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.impl.script.DefaultAiyatsbusScriptHandler
 *
 * @author mical
 * @since 2025/6/22 13:18
 */
class DefaultAiyatsbusScriptHandler : AiyatsbusScriptHandler {

    val ketherScriptHandler = KetherScriptHandler()
    val fluxonScriptHandler = FluxonScriptHandler()

    override fun getScriptHandler(type: ScriptType): ScriptHandler {
        return when (type) {
            ScriptType.KETHER -> ketherScriptHandler
            ScriptType.FLUXON -> fluxonScriptHandler
            else -> error("Unsupported script type: ${type.name}")
        }
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusScriptHandler>(DefaultAiyatsbusScriptHandler())
        }
    }
}