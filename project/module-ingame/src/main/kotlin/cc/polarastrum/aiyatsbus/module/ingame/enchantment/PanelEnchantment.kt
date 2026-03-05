package cc.polarastrum.aiyatsbus.module.ingame.enchantment

import cc.polarastrum.aiyatsbus.core.aiyatsbusEt
import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker
import cc.polarastrum.aiyatsbus.core.etLevel
import cc.polarastrum.aiyatsbus.core.util.mark
import cc.polarastrum.aiyatsbus.core.util.unmark
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * 平展附魔（平面）
 *
 * 为镐子提供 N x N 平面范围挖掘能力（在与玩家朝向垂直的平面内）。
 * 支持按附魔等级扩展范围：等级1 -> 半径1(3x3)，等级2 -> 半径2(5x5)，以此类推，半径最大为5。
 * 在破坏额外方块前会通过 AntiGriefChecker 进行权限验证，避免越权问题。
 */
object PanelEnchantment {

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
        val enchant = aiyatsbusEt("panel") ?: return
        val level = tool.etLevel(enchant)
        if (level < 1) return

        val block = e.block
        val toolCopy = tool.clone()

        // 根据附魔等级选择半径，等级1->1(3x3)，等级2->2(5x5)，以此类推，最大半径为5
        val radius = minOf(level, 5)
        val face = player.facing

        // 根据检测到的 face 确定 N x N 平面，并遍历（不包含中心方块）
        for (target in getPlaneBlocks(block, face, radius)) {
            if (target.type == Material.AIR || target.type in unbreakable) continue
            if (!AntiGriefChecker.canBreak(player, target.location)) continue
            breakBlockSafely(target, toolCopy)
        }
    }

    /**
     * 根据玩家朝向获取以指定方块为中心的 N x N 平面内的方块（不含中心方块）
     *
     * @param center 中心方块
     * @param facing 玩家朝向
     * @param radius 半径（例如 radius=1 表示 3x3，radius=2 表示 5x5）
     * @return 平面内除中心外的方块列表
     */
    private fun getPlaneBlocks(center: Block, facing: BlockFace, radius: Int): List<Block> {
        val result = mutableListOf<Block>()
        for (i in -radius..radius) {
            for (j in -radius..radius) {
                if (i == 0 && j == 0) continue
                val target = when (facing) {
                    // 玩家朝南/北：平面为 X-Y（东西 × 垂直）
                    BlockFace.NORTH, BlockFace.SOUTH -> center.getRelative(i, j, 0)
                    // 玩家朝东/西：平面为 Z-Y（南北 × 垂直）
                    BlockFace.EAST, BlockFace.WEST -> center.getRelative(0, j, i)
                    // 玩家朝上/下：平面为 X-Z（东西 × 南北）
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