package cc.polarastrum.aiyatsbus.impl

import cc.polarastrum.aiyatsbus.core.AiyatsbusMinecraftAPI
import cc.polarastrum.aiyatsbus.core.MinecraftHelper
import cc.polarastrum.aiyatsbus.core.MinecraftItemOperator
import cc.polarastrum.aiyatsbus.core.MinecraftPacketHandler
import cc.polarastrum.aiyatsbus.core.MinecraftWorldOperator
import cc.polarastrum.aiyatsbus.impl.DefaultAiyatsbusAPI.Companion.proxy
import cc.polarastrum.aiyatsbus.impl.nmsj21.NMSJ21
import taboolib.module.nms.MinecraftVersion.versionId
import java.util.concurrent.CompletableFuture

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.DefaultAiyatsbusMinecraftAPI
 *
 * @author mical
 * @since 2024/2/18 00:21
 */
class DefaultAiyatsbusMinecraftAPI : AiyatsbusMinecraftAPI {

    /** 杂项 */
    val nmsHelper by lazy {
        proxy<MinecraftHelper>("$group.nmsj21.DefaultMinecraftHelper")
    }

    /** 物品操作接口 */
    val nmsItemOperator by lazy {
        proxy<MinecraftItemOperator>("$group.nms.DefaultMinecraftItemOperator")
    }

    /** 数据包控制接口 */
    val nmsPacketHandler by lazy {
        if (versionId >= 12105) {
            proxy<MinecraftPacketHandler>("$group.nmsj21.DefaultMinecraftPacketHandler")
        } else {
            proxy<MinecraftPacketHandler>("$group.nms.DefaultMinecraftPacketHandler")
        }
    }

    /** 世界操作接口 */
    val nmsWorldOperator by lazy {
        proxy<MinecraftWorldOperator>("$group.nms.DefaultMinecraftWorldOperator")
    }

    override fun getHelper(): MinecraftHelper {
        return nmsHelper
    }

    override fun getItemOperator(): MinecraftItemOperator {
        return nmsItemOperator
    }

    override fun getPacketHandler(): MinecraftPacketHandler {
        return nmsPacketHandler
    }

    override fun getWorldOperator(): MinecraftWorldOperator {
        return nmsWorldOperator
    }

    init {
        CompletableFuture.runAsync {
            if (versionId >= 12005) NMSJ21.instance
            nmsHelper
            nmsItemOperator
            nmsPacketHandler
            nmsWorldOperator
        }
    }

    companion object {

        val group = "${DefaultAiyatsbusMinecraftAPI::class.java.`package`.name}"
    }
}