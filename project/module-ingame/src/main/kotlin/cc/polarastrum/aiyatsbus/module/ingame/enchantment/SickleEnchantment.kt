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
import org.bukkit.block.data.Ageable
import org.bukkit.event.block.BlockBreakEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * 镰刀附魔
 *
 * 为锄头提供 3x3 范围作物收割能力，支持两个等级：
 * - 一级：收割 3x3 范围内的所有成熟作物
 * - 二级：收割 3x3 范围内的所有成熟作物，同时自动补种
 *
 * 在收割/补种前会通过 AntiGriefChecker 进行权限验证，避免越权问题。
 *
 * @author copilot
 */
object SickleEnchantment {

    /** 支持收割的作物材质及其对应的种子材质 */
    private val cropSeeds = mapOf(
        Material.WHEAT to Material.WHEAT_SEEDS,
        Material.CARROTS to Material.CARROT,
        Material.POTATOES to Material.POTATO,
        Material.BEETROOTS to Material.BEETROOT_SEEDS,
        Material.NETHER_WART to Material.NETHER_WART
    )

    @SubscribeEvent(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onBlockBreak(e: BlockBreakEvent) {
        // 防止递归触发：已被标记为正在处理的方块跳过
        if (e.block.hasMetadata("block-ignored")) return

        val player = e.player
        // 只在生存/冒险模式下生效
        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) return

        val tool = player.inventory.itemInMainHand
        val enchant = aiyatsbusEt("sickle") ?: return
        val level = tool.etLevel(enchant)
        if (level < 1) return

        val block = e.block
        // 仅当被破坏的方块本身是成熟作物时才触发
        if (!isFullyGrownCrop(block)) return

        val toolCopy = tool.clone()

        // 根据附魔等级选择范围：等级1/2 为半径1(3x3)，等级3 及以上为半径2(5x5)
        val radius = if (level >= 3) 2 else 1
        for (target in getSurrounding(block, radius)) {
            if (!isFullyGrownCrop(target)) continue
            val cropType = target.type

            // 检查是否有破坏权限
            if (!AntiGriefChecker.canBreak(player, target.location)) continue

            // 标记方块防止递归，并执行自然破坏（保留工具附魔效果）
            target.mark("block-ignored")
            try {
                target.breakNaturally(toolCopy)

                // 二级附魔：自动补种
                if (level >= 2 && AntiGriefChecker.canPlace(player, target.location)) {
                    replantCrop(target, cropType)
                }
            } finally {
                target.unmark("block-ignored")
            }
        }
    }

    /**
     * 检查指定方块是否为成熟作物
     *
     * @param block 要检查的方块
     * @return 如果是成熟作物则返回 true
     */
    private fun isFullyGrownCrop(block: Block): Boolean {
        if (block.type !in cropSeeds) return false
        val data = block.blockData as? Ageable ?: return false
        return data.age == data.maximumAge
    }

    /**
     * 获取以指定方块为中心的 N x N 水平范围内的其他方块（不含中心方块）
     *
     * @param center 中心方块
     * @param radius 半径（1 表示 3x3，2 表示 5x5）
     * @return 周围方块列表
     */
    private fun getSurrounding(center: Block, radius: Int): List<Block> {
        val result = mutableListOf<Block>()
        for (dx in -radius..radius) {
            for (dz in -radius..radius) {
                if (dx == 0 && dz == 0) continue
                result.add(center.getRelative(dx, 0, dz))
            }
        }
        return result
    }

    /**
     * 在指定位置补种对应的作物
     *
     * 仅当底部方块满足作物生长条件（耕地或灵魂沙）时才会补种。
     *
     * @param block 要补种的方块位置
     * @param cropType 原作物的材质类型
     */
    private fun replantCrop(block: Block, cropType: Material) {
        val blockBelow = block.getRelative(BlockFace.DOWN)
        val canReplant = when (cropType) {
            Material.NETHER_WART -> blockBelow.type == Material.SOUL_SAND
            else -> blockBelow.type == Material.FARMLAND
        }
        if (!canReplant) return
        block.type = cropType
        val ageableData = block.blockData as? Ageable ?: return
        ageableData.age = 0
        block.blockData = ageableData
    }
}