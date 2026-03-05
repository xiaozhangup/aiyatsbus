package cc.polarastrum.aiyatsbus.core

import org.bukkit.inventory.ItemStack

/**
 * Minecraft 杂项工具接口
 *
 * 提供 Minecraft 底层的通用杂项操作工具，
 * 主要包括组件转换等跨版本兼容功能。
 *
 * @author mical
 * @since 2025/8/16 08:42
 */
interface MinecraftHelper {

    /**
     * 将 Json 转成 IChatBaseComponent
     *
     * @param json JSON 字符串
     * @return IChatBaseComponent 对象
     */
    fun componentFromJson(json: String): Any

    /**
     * 将 IChatBaseComponent 转成 Json
     *
     * @param iChatBaseComponent IChatBaseComponent 对象
     * @return JSON 字符串
     */
    fun componentToJson(iChatBaseComponent: Any): String

    /**
     * 将 NMSItemStack 转成 CraftItemStack
     *
     * @param nmsItemStack NMS 的 ItemStack 对象
     * @return CraftItemStack 对象
     */
    fun asCraftMirror(nmsItemStack: Any): ItemStack

    /**
     * 通过 CraftItemStack 直接获取 NMSItemStack, 避免转换
     *
     * @param item CraftItemStack 对象
     * @return NMS 的 ItemStack 对象
     */
    fun getCraftItemStackHandle(item: ItemStack): Any
}