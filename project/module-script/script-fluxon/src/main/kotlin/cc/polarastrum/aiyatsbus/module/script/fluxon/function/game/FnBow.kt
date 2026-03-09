package cc.polarastrum.aiyatsbus.module.script.fluxon.function.game

import cc.polarastrum.aiyatsbus.core.event.AiyatsbusBowChargeEvent
import cc.polarastrum.aiyatsbus.module.script.fluxon.relocate.FluxonRelocate
import org.tabooproject.fluxon.platform.bukkit.function.bukkit.entity.FnPlayer
import org.tabooproject.fluxon.platform.bukkit.function.bukkit.event.FnEvent
import org.tabooproject.fluxon.platform.bukkit.function.bukkit.inventory.FnEquipmentSlot
import org.tabooproject.fluxon.platform.bukkit.function.bukkit.inventory.FnItemStack
import org.tabooproject.fluxon.runtime.FluxonRuntime
import org.tabooproject.fluxon.runtime.FunctionSignature.returns
import org.tabooproject.fluxon.runtime.FunctionSignature.returnsVoid
import org.tabooproject.fluxon.runtime.Type
import taboolib.common.LifeCycle
import taboolib.common.Requires
import taboolib.common.platform.Awake

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.module.script.fluxon.function.game.FnBow
 *
 * @author mical
 * @since 2026/2/12 23:59
 */
@Requires(missingClasses = ["!org.tabooproject.fluxon.ParseScript"])
@FluxonRelocate
object FnBow {

    val TYPE_PREPARE = Type.fromClass(AiyatsbusBowChargeEvent.Prepare::class.java)!!
    val TYPE_CHARGE_INFO = Type.fromClass(AiyatsbusBowChargeEvent.ChargeInfo::class.java)!!
    val TYPE_BREAK_REASON = Type.fromClass(AiyatsbusBowChargeEvent.Break.Reason::class.java)!!

    @Awake(LifeCycle.LOAD)
    fun init() {
        with(FluxonRuntime.getInstance()) {
            registerExtension(AiyatsbusBowChargeEvent.Prepare::class.java)
                .function("player", returns(FnPlayer.TYPE).noParams()) { it.setReturnRef(it.target?.player) }
                .function("itemStack", returns(FnItemStack.TYPE).noParams()) { it.setReturnRef(it.target?.itemStack) }
                .function("hand", returns(FnEquipmentSlot.TYPE).noParams()) { it.setReturnRef(it.target?.hand) }
                .function("isAllowed", returns(Type.Z).noParams()) { it.setReturnBool(it.target?.isAllowed!!) }
                .function("setAllowed", returnsVoid().params(Type.Z)) { it.apply { target?.isAllowed = it.getBool(0) } }
                .function("fire", returns(TYPE_PREPARE).noParams()) { it.setReturnRef(it.target?.fire()) }
                .function("release", returnsVoid().noParams()) { it.target?.release() }
                .function("interrupt", returnsVoid().noParams()) { it.target?.interrupt() }
            registerExtension(AiyatsbusBowChargeEvent.Released::class.java)
                .function("player", returns(FnPlayer.TYPE).noParams()) { it.setReturnRef(it.target?.player) }
                .function("itemStack", returns(FnItemStack.TYPE).noParams()) { it.setReturnRef(it.target?.itemStack) }
                .function("hand", returns(FnEquipmentSlot.TYPE).noParams()) { it.setReturnRef(it.target?.hand) }
                .function("chargeInfo", returns(TYPE_CHARGE_INFO).noParams()) { it.setReturnRef(it.target?.chargeInfo) }
                .function("startTime", returns(Type.J).noParams()) { it.setReturnLong(it.target?.startTime!!) }
                .function("chargeTime", returns(Type.J).noParams()) { it.setReturnLong(it.target?.chargeTime!!) }
            registerExtension(AiyatsbusBowChargeEvent.Break::class.java)
                .function("player", returns(FnPlayer.TYPE).noParams()) { it.setReturnRef(it.target?.player) }
                .function("chargeInfo", returns(TYPE_CHARGE_INFO).noParams()) { it.setReturnRef(it.target?.chargeInfo) }
                .function("reason", returns(TYPE_BREAK_REASON).noParams()) { it.setReturnRef(it.target?.reason!!) }
                .function("source", returns(FnEvent.TYPE).noParams()) { it.setReturnRef(it.target?.source) }
                .function("startTime", returns(Type.J).noParams()) { it.setReturnLong(it.target?.startTime!!) }
                .function("chargeTime", returns(Type.J).noParams()) { it.setReturnLong(it.target?.chargeTime!!) }
            registerExtension(AiyatsbusBowChargeEvent.ChargeInfo::class.java)
                .function("player", returns(FnPlayer.TYPE).noParams()) { it.setReturnRef(it.target?.player) }
                .function("itemStack", returns(FnItemStack.TYPE).noParams()) { it.setReturnRef(it.target?.itemStack) }
                .function("hand", returns(FnEquipmentSlot.TYPE).noParams()) { it.setReturnRef(it.target?.hand) }
                .function("startTime", returns(Type.J).noParams()) { it.setReturnLong(it.target?.startTime!!) }
                .function("chargeTime", returns(Type.J).noParams()) { it.setReturnLong(it.target?.chargeTime!!) }
                .function("stopTime", returns(Type.J).noParams()) { it.setReturnLong(it.target?.stopTime!!) }
                .function("setStopTime", returnsVoid().params(Type.J)) { it.apply { target?.stopTime = it.getLong(0) } }
        }
    }
}