package cc.polarastrum.aiyatsbus.module.ingame.mechanics

import cc.polarastrum.aiyatsbus.core.AiyatsbusSettings
import cc.polarastrum.aiyatsbus.core.sendLang
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.pluginVersion

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.ingame.mechanics.ThankMessage
 *
 * @author mical
 * @since 2024/5/1 23:46
 */
object ThankMessage {

    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        if (AiyatsbusSettings.sendThankMessages && (e.player.isOp || e.player.hasPermission("aiyatsbus.thanks"))) {
            e.player.sendLang("thanks", pluginVersion)
        }
    }
}