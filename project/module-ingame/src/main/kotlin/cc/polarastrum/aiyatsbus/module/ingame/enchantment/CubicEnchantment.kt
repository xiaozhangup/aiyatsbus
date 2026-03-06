package cc.polarastrum.aiyatsbus.module.ingame.enchantment

import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker
import cc.polarastrum.aiyatsbus.core.data.BasicData
import cc.polarastrum.aiyatsbus.core.data.Displayer
import cc.polarastrum.aiyatsbus.core.data.VariableType
import cc.polarastrum.aiyatsbus.core.enchant.EventFunctions
import cc.polarastrum.aiyatsbus.core.enchant.HardcodedEnchantment
import cc.polarastrum.aiyatsbus.core.util.mark
import cc.polarastrum.aiyatsbus.core.util.unmark
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * 立方附魔
 *
 * 为镐子提供 N x N x N 立方体范围挖掘能力。
 * 支持按附魔等级扩展范围：等级1 -> 半径1(3x3x3)，等级2 -> 半径2(5x5x5)，以此类推，半径最大为5。
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

    @Awake(LifeCycle.LOAD)
    fun register() {
        HardcodedEnchantment.builder()
            .basicData(BasicData.builder().id("cubic").name("立方").maxLevel(1).build())
            .rarity("异宝")
            .targets("镐")
            .displayer(
                Displayer.builder()
                    .generalDescription("&7挖掘时以破坏的方块为中心，同时破坏&a{范围}x{范围}x{范围}&7范围内的方块")
                    .specificDescription("&7同时挖掘&a{范围}x{范围}x{范围}&7的方块")
                    .build()
            )
            .addVariables(VariableType.LEVELED, "范围", "{level}*2+1")
            .eventExecutor(object : EventFunctions {
                override fun blockBreak(level: Int, event: BlockBreakEvent) {
                    if (event.block.hasMetadata("block-ignored")) return

                    val player = event.player
                    val tool = player.inventory.itemInMainHand
                    if (level < 1) return

                    val block = event.block
                    val toolCopy = tool.clone()
                    val radius = minOf(level, 5)

                    for (target in getCubeBlocks(block, radius)) {
                        if (target.type == Material.AIR || target.type in unbreakable) continue
                        if (!AntiGriefChecker.canBreak(player, target.location)) continue
                        breakBlockSafely(target, toolCopy)
                    }
                }
            })
            .register()
    }

    /**
     * 获取以指定方块为中心的 (2*radius+1)^3 立方体内的方块（不包含中心方块）
     *
     * @param center 中心方块
     * @param radius 半径（例如 radius=1 表示 3x3x3，radius=2 表示 5x5x5）
     */
    private fun getCubeBlocks(center: Block, radius: Int): List<Block> {
        val result = mutableListOf<Block>()
        for (dx in -radius..radius) {
            for (dy in -radius..radius) {
                for (dz in -radius..radius) {
                    if (dx == 0 && dy == 0 && dz == 0) continue
                    result.add(center.getRelative(dx, dy, dz))
                }
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