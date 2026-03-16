package cc.polarastrum.aiyatsbus.module.script.fluxon.function.game

import cc.polarastrum.aiyatsbus.module.script.fluxon.FluxonScriptHandler
import cc.polarastrum.aiyatsbus.module.script.fluxon.relocate.FluxonRelocate
import org.bukkit.entity.Player
import org.tabooproject.fluxon.runtime.FluxonRuntime
import org.tabooproject.fluxon.runtime.Function
import org.tabooproject.fluxon.runtime.FunctionSignature.returns
import org.tabooproject.fluxon.runtime.Type
import org.tabooproject.fluxon.util.invokeInline
import taboolib.common.LifeCycle
import taboolib.common.Requires
import taboolib.common.platform.Awake
import taboolib.platform.util.hasItem
import taboolib.platform.util.takeItem

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.module.script.fluxon.function.game.FnEntity
 *
 * @author mical
 * @since 2026/1/2 00:09
 */
@Requires(missingClasses = ["!org.tabooproject.fluxon.ParseScript"])
@FluxonRelocate
object FnInventory {

    val TYPE = Type.fromClass(FnInventory::class.java)!!

    @Awake(LifeCycle.LOAD)
    fun init() {
        FluxonScriptHandler.DEFAULT_PACKAGE_AUTO_IMPORT += "aiy:inventory"
        with(FluxonRuntime.getInstance()) {
            registerFunction("aiy:inventory", "inventory", returns(TYPE).noParams()) { it.setReturnRef(FnInventory) }
//            exportRegistry.registerClass(FnInventory::class.java, "aiy:inventory")
            registerExtensionFunction(FnInventory::class.java, "aiy:inventory", "hasItem", returns(Type.Z).params(org.tabooproject.fluxon.platform.bukkit.function.bukkit.entity.FnPlayer.TYPE,
                Type.I, Function.TYPE), { context ->
                    context.setReturnBool((context.getRef(0) as Player).inventory.hasItem(context.getAsInt(1)) {
                        (context.getRef(2) as Function).invokeInline(context.environment, 1, it, null, null, null, this@FnInventory) == true
                    })
            }, false, false)
            registerExtensionFunction(FnInventory::class.java, "aiy:inventory", "takeItem", returns(Type.Z).params(org.tabooproject.fluxon.platform.bukkit.function.bukkit.entity.FnPlayer.TYPE,
                Type.I, Function.TYPE), { context ->
                context.setReturnBool((context.getRef(0) as Player).inventory.takeItem(context.getAsInt(1)) {
                    (context.getRef(2) as Function).invokeInline(context.environment, 1, it, null, null, null, this@FnInventory) == true
                })
            }, false, false)
        }
    }
}