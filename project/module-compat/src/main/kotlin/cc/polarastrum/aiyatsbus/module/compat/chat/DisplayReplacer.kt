package cc.polarastrum.aiyatsbus.module.compat.chat

import cc.polarastrum.aiyatsbus.module.compat.chat.display.DisplayReplacerComponents
import cc.polarastrum.aiyatsbus.module.compat.chat.display.DisplayReplacerDataComponents
import cc.polarastrum.aiyatsbus.module.compat.chat.display.DisplayReplacerNBT
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.MinecraftVersion

/**
 * 用以替换 Component 中的 HoverEvent，使之支持更多附魔展示
 *
 * @author mical
 * @since 2024/8/18 16:36
 */
interface DisplayReplacer {

    /**
     * 替换 IChatBaseComponent 或 Component 中的 HoverEvent
     */
    fun apply(component: Component, player: Player): Component

    companion object {

        val inst by unsafeLazy {
            if (MinecraftVersion.versionId >= 12105) {
                DisplayReplacerComponents
            } else if (MinecraftVersion.versionId >= 12005) {
                DisplayReplacerDataComponents
            } else {
                DisplayReplacerNBT
            }
        }
    }
}