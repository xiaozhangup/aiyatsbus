package cc.polarastrum.aiyatsbus.core

import com.google.common.collect.Table

/**
 * Aiyatsbus 附魔调度器接口
 *
 * 负责管理附魔的定时任务和调度。
 * 提供附魔定时器的管理、重置和启动功能。
 *
 * @author mical
 * @since 2024/3/20 21:14
 */
interface AiyatsbusTickHandler {

    /**
     * 获取所有附魔调度器
     *
     * AiyatsbusEnchantment -> 附魔对象
     * String -> 调度器 ID
     * Long -> 循环时间间隔
     *
     * @return 附魔调度器表格
     */
    fun getRoutine(): Table<AiyatsbusEnchantment, String, Long>

    /**
     * 重置所有附魔调度器
     *
     * 清空所有定时任务
     */
    fun reset()

    /**
     * 开始运行
     *
     * 启动所有定时任务
     */
    fun start()
}