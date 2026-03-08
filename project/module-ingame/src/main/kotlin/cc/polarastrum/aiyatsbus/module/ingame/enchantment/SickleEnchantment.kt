package cc.polarastrum.aiyatsbus.module.ingame.enchantment

import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker
import cc.polarastrum.aiyatsbus.core.data.BasicData
import cc.polarastrum.aiyatsbus.core.data.Displayer
import cc.polarastrum.aiyatsbus.core.data.VariableType
import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.EventFunctions
import cc.polarastrum.aiyatsbus.core.util.mark
import cc.polarastrum.aiyatsbus.core.util.unmark
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Ageable
import org.bukkit.event.block.BlockBreakEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submit

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

    @Awake(LifeCycle.LOAD)
    fun register() {
        BuiltinAiyatsbusEnchantment.builder()
            .basicData(BasicData.builder().id("sickle").name("镰刀").maxLevel(3).build())
            .rarity("稀有")
            .targets("锄")
            .displayer(
                Displayer.builder()
                    .generalDescription("&7收割范围内的成熟作物, 并在大于2级时自动补种")
                    .specificDescription("&7收割&a{范围}x{范围}&7范围内的成熟作物")
                    .build()
            )
            .addVariable(VariableType.LEVELED, "范围", "max({level}-1,1)*2+1")
            .eventExecutor(object : EventFunctions {
                override fun blockBreak(level: Int, event: BlockBreakEvent) {
                    if (event.block.hasMetadata("block-ignored")) return

                    val player = event.player
                    val tool = player.inventory.itemInMainHand
                    if (level < 1) return

                    val block = event.block
                    if (!isFullyGrownCrop(block)) return

                    val toolCopy = tool.clone()
                    val radius = if (level >= 3) 2 else 1
                    val centerCropType = block.type

                    for (target in getSurrounding(block, radius)) {
                        if (!isFullyGrownCrop(target)) continue
                        val cropType = target.type

                        if (!AntiGriefChecker.canBreak(player, target.location)) continue

                        target.mark("block-ignored")
                        try {
                            target.breakNaturally(toolCopy)

                            if (level >= 2 && AntiGriefChecker.canPlace(player, target.location)) {
                                replantCrop(target, cropType)
                            }
                        } finally {
                            target.unmark("block-ignored")
                        }
                    }

                    if (level >= 2 && AntiGriefChecker.canPlace(player, block.location)) {
                        submit(delay = 1L) {
                            val center = block.world.getBlockAt(block.x, block.y, block.z)
                            if (center.type.isAir) {
                                center.mark("block-ignored")
                                try {
                                    replantCrop(center, centerCropType)
                                } finally {
                                    center.unmark("block-ignored")
                                }
                            }
                        }
                    }
                }
            })
            .register()
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