package cc.polarastrum.aiyatsbus.impl.nms

import cc.polarastrum.aiyatsbus.core.MinecraftWorldOperator
import net.minecraft.core.BlockPosition
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.impl.nms.nms.DefaultMinecraftWorldOperator
 *
 * @author mical
 * @since 2025/8/16 08:52
 */
class DefaultMinecraftWorldOperator : MinecraftWorldOperator {

    override fun breakBlock(player: Player, block: Block): Boolean {
        return (player as CraftPlayer).handle.gameMode.destroyBlock(BlockPosition(block.x, block.y, block.z))
    }
}