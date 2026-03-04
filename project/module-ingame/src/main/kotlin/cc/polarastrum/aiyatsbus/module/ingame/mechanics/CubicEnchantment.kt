/*
 *  Copyright (C) 2022-2024 PolarAstrumLab
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
package cc.polarastrum.aiyatsbus.module.ingame.mechanics

import cc.polarastrum.aiyatsbus.core.aiyatsbusEt
import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker
import cc.polarastrum.aiyatsbus.core.etLevel
import cc.polarastrum.aiyatsbus.core.util.mark
import cc.polarastrum.aiyatsbus.core.util.unmark
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.inventory.ItemStack
import org.bukkit.event.block.BlockBreakEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * 立方附魔
 *
 * 为镐子提供 3x3 范围挖掘能力。
 * 触发时以破坏的方块为中心，在垂直于玩家朝向的平面上同时破坏 3x3 范围内的方块。
 * 在破坏额外方块前会通过 AntiGriefChecker 进行权限验证，避免越权问题。
 *
 * @author copilot
 */
object CubicEnchantment {

    /** 不应被破坏的方块材质集合 */
    private val unbreakable = setOf(
        Material.BEDROCK,
        Material.BARRIER,
        Material.END_PORTAL_FRAME,
        Material.END_PORTAL,
        Material.NETHER_PORTAL,
        Material.COMMAND_BLOCK,
        Material.CHAIN_COMMAND_BLOCK,
        Material.REPEATING_COMMAND_BLOCK,
        Material.STRUCTURE_BLOCK,
        Material.JIGSAW
    )

    @SubscribeEvent(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onBlockBreak(e: BlockBreakEvent) {
        // 防止递归触发：已被标记为正在处理的方块跳过
        if (e.block.hasMetadata("block-ignored")) return

        val player = e.player
        // 只在生存/冒险模式下生效
        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) return

        val tool = player.inventory.itemInMainHand
        val enchant = aiyatsbusEt("cubic") ?: return
        if (tool.etLevel(enchant) < 1) return

        val block = e.block
        val toolCopy = tool.clone()

        // 根据玩家朝向确定 3x3 挖掘平面
        for (target in get3x3Blocks(block, player.facing)) {
            if (target.type == Material.AIR || target.type in unbreakable) continue
            if (!AntiGriefChecker.canBreak(player, target.location)) continue
            breakBlockSafely(target, toolCopy)
        }
    }

    /**
     * 根据玩家朝向获取以指定方块为中心的 3x3 范围内的方块（不含中心方块）
     *
     * @param center 中心方块
     * @param facing 玩家朝向
     * @return 3x3 范围内除中心外的 8 个方块列表
     */
    private fun get3x3Blocks(center: Block, facing: BlockFace): List<Block> {
        val result = mutableListOf<Block>()
        for (i in -1..1) {
            for (j in -1..1) {
                if (i == 0 && j == 0) continue
                val target = when (facing) {
                    // 玩家朝南/北：3x3 在 X-Y 平面（东西方向 × 垂直方向）
                    BlockFace.NORTH, BlockFace.SOUTH -> center.getRelative(i, j, 0)
                    // 玩家朝东/西：3x3 在 Z-Y 平面（南北方向 × 垂直方向）
                    BlockFace.EAST, BlockFace.WEST -> center.getRelative(0, j, i)
                    // 玩家朝上/下：3x3 在 X-Z 平面（东西方向 × 南北方向）
                    else -> center.getRelative(i, 0, j)
                }
                result.add(target)
            }
        }
        return result
    }

    /**
     * 安全地破坏方块，使用 "block-ignored" 标记防止递归触发附魔
     *
     * @param block 要破坏的方块
     * @param tool 使用的工具（用于计算掉落物及附魔效果）
     */
    private fun breakBlockSafely(block: Block, tool: ItemStack) {
        block.mark("block-ignored")
        try {
            block.breakNaturally(tool)
        } finally {
            block.unmark("block-ignored")
        }
    }
}
