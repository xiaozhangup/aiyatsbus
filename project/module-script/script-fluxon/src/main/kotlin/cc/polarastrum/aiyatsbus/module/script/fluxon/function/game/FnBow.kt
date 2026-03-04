package cc.polarastrum.aiyatsbus.module.script.fluxon.function.game

import cc.polarastrum.aiyatsbus.core.event.AiyatsbusBowChargeEvent
import cc.polarastrum.aiyatsbus.module.script.fluxon.relocate.FluxonRelocate
import org.tabooproject.fluxon.runtime.FluxonRuntime
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

    @Awake(LifeCycle.LOAD)
    fun init() {
        with(FluxonRuntime.getInstance()) {
            registerExtension(AiyatsbusBowChargeEvent.Prepare::class.java)
                .function("player", 0) { it.target?.player }
                .function("itemStack", 0) { it.target?.itemStack }
                .function("hand", 0) { it.target?.hand }
                .function("isAllowed", 0) { it.target?.isAllowed }
                .function("setAllowed", 1) { it.apply { target?.isAllowed = it.getBoolean(0) } }
                .function("fire", 0) { it.target?.fire() }
                .function("release", 0) { it.target?.release() }
                .function("interrupt", 0) { it.target?.interrupt() }
            registerExtension(AiyatsbusBowChargeEvent.Released::class.java)
                .function("player", 0) { it.target?.player }
                .function("itemStack", 0) { it.target?.itemStack }
                .function("hand", 0) { it.target?.hand }
                .function("chargeInfo", 0) { it.target?.chargeInfo }
                .function("startTime", 0) { it.target?.startTime }
                .function("chargeTime", 0) { it.target?.chargeTime }
            registerExtension(AiyatsbusBowChargeEvent.Break::class.java)
                .function("player", 0) { it.target?.player }
                .function("chargeInfo", 0) { it.target?.chargeInfo }
                .function("reason", 0) { it.target?.reason }
                .function("source", 0) { it.target?.source }
                .function("startTime", 0) { it.target?.startTime }
                .function("chargeTime", 0) { it.target?.chargeTime }
            registerExtension(AiyatsbusBowChargeEvent.ChargeInfo::class.java)
                .function("player", 0) { it.target?.player }
                .function("itemStack", 0) { it.target?.itemStack }
                .function("hand", 0) { it.target?.hand }
                .function("startTime", 0) { it.target?.startTime }
                .function("stopTime", 0) { it.target?.stopTime }
                .function("setStopTime", 1) { it.apply { target?.stopTime = it.getNumber(0).toLong() } }
                .function("chargeTime", 0) { it.target?.chargeTime }
        }
    }
}