package cc.polarastrum.aiyatsbus.module.compat.chat

import cc.polarastrum.aiyatsbus.core.Aiyatsbus.packetEventManager
import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMessagePreSendEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.compat.vanilla.PacketSystemChat
 *
 * @author mical
 * @since 2024/2/18 00:40
 */
object PacketSystemChat {

    @SubscribeEvent
    fun e(e: PlayerMessagePreSendEvent) {
        e.component = DisplayReplacer.inst.apply(e.component, e.player)
    }

//    @Awake(LifeCycle.ACTIVE)
//    fun registerListener() {
//        packetEventManager.registerListener(PacketChatListener())
//    }
//
//    class PacketChatListener : PacketListenerAbstract() {
//        override fun onPacketSend(e: PacketSendEvent) {
//            val player = e.getPlayer<Player>()
//            when(e.packetType) {
//                PacketType.Play.Server.CHAT_MESSAGE -> {
//                    val wrapper = WrapperPlayServerChatMessage(e)
//                    val message = wrapper.message
//                    val content = message.chatContent
//                    message.chatContent = DisplayReplacer.inst.apply(content, player)
//                    wrapper.message = message
//                }
//                PacketType.Play.Server.SYSTEM_CHAT_MESSAGE -> {
//                    val wrapper = WrapperPlayServerSystemChatMessage(e)
//                    val message = wrapper.message
//                    wrapper.message = DisplayReplacer.inst.apply(message, player)
//                }
//            }
//        }
//    }
}