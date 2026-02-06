/*
 * This file is part of Ratziel, licensed under the GPL-3.0 License.
 *
 *  Copyright (C) 2025 TheFloodDragon
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package cc.polarastrum.aiyatsbus.impl.nms

import cc.polarastrum.aiyatsbus.core.MinecraftPacketHandler
import cc.polarastrum.aiyatsbus.core.toRevertMode
import cc.polarastrum.aiyatsbus.core.util.isNull
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R3.CraftServer
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.nms.MinecraftVersion.isUniversal
import taboolib.module.nms.MinecraftVersion.versionId
import taboolib.module.nms.PacketReceiveEvent

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.impl.nms.nms.DefaultMinecraftPacketHandler
 *
 * @author mical
 * @since 2025/8/16 09:34
 */
class DefaultMinecraftPacketHandler : MinecraftPacketHandler {

    override fun synchronizeRegistries(player: Player) {
//        if (versionId < 12005) return // 小于这个版本的没有 SynchronizeRegistriesTask，调用无意义
//        val uuid = player.uniqueId
//        (player as CraftPlayer).handle.connection.switchToConfig()
//
//        submit(delay = 10L) {
//            ((Bukkit.getServer() as CraftServer).server.connection.connections
//                .firstOrNull {
//                    it.packetListener is ServerConfigurationPacketListenerImpl &&
//                            (it.packetListener as ServerConfigurationPacketListenerImpl).owner.id == uuid
//                }
//                ?.packetListener as? ServerConfigurationPacketListenerImpl)
//                ?.startConfiguration()
//        }
    }

    /**
     * isUniversal -> carriedItem
     * 1.16- -> item
     * 这里不做判断
     */
    private val carriedItemFieldInContainerClick = "carriedItem"

    /** since 1.17 */
    private val changedSlotsField = "changedSlots"

    override fun handleContainerClick(event: PacketReceiveEvent) {
        val items = HashMap<Int, Any>()
        // 光标上的物品
        items[-10086] = event.packet.read<Any>(carriedItemFieldInContainerClick) ?: error("$carriedItemFieldInContainerClick does not exist.")

        handleItem(event.packet.read<Any>(carriedItemFieldInContainerClick), event.player)?.let { carried -> event.packet.write(carriedItemFieldInContainerClick, carried) }

        // 1.17 加入 changedSlots
        // 1.21.4-
        if (isUniversal) {
            items.putAll((event.packet.read<Map<Int, Any>>(changedSlotsField) ?: emptyMap()).mapValues { (_, nmsItem) ->
                handleItem(nmsItem, event.player) ?: nmsItem
            })
        }
    }

    private fun handleItem(nmsItem: Any?, player: Player): Any? {
        val bkItem = CraftItemStack.asCraftMirror(nmsItem as? NMSItemStack ?: return null)
        if (bkItem.isNull) return null
        return (bkItem.toRevertMode(player) as CraftItemStack).getProperty("handle")
    }
}