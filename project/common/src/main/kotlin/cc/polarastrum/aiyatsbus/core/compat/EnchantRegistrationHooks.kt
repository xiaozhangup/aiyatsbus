package cc.polarastrum.aiyatsbus.core.compat

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.platform.util.bukkitPlugin
import java.util.concurrent.ConcurrentHashMap

/**
 * 附魔注册钩子接口
 *
 * 定义与其他插件的附魔注册钩子接口，用于在插件启用时注册附魔，禁用时注销附魔。
 *
 * @author mical
 * @since 2024/4/30 21:22
 */
interface EnchantRegistrationHook {

    /**
     * 获取插件名称
     *
     * @return 目标插件名称
     */
    fun getPluginName(): String

    /**
     * 注册附魔
     *
     * 在目标插件启用时调用，用于注册附魔到目标插件中。
     */
    fun register() {
    }

    /**
     * 注销附魔
     *
     * 在目标插件禁用时调用，用于从目标插件中注销附魔。
     */
    fun unregister() {
    }
}

/**
 * 附魔注册钩子管理器
 *
 * 管理所有附魔注册钩子，负责在插件启用/禁用时自动注册/注销附魔。
 *
 * @author mical
 * @since 2024/4/30 21:22
 */
object EnchantRegistrationHooks {

    /** 已注册的钩子映射表 */
    val registered: ConcurrentHashMap<String, EnchantRegistrationHook> = ConcurrentHashMap()

    /**
     * 注册所有钩子
     *
     * 遍历所有已注册的钩子并尝试注册。
     */
    fun registerHooks() {
        registered.values.forEach { registerHook(it) }
    }

    /**
     * 注册单个钩子
     *
     * @param hook 要注册的钩子
     */
    fun registerHook(hook: EnchantRegistrationHook) {
        if (bukkitPlugin.server.pluginManager.getPlugin(hook.getPluginName()) != null) {
            kotlin.runCatching { hook.register() }
        }
    }

    /**
     * 注销单个钩子
     *
     * @param hook 要注销的钩子
     */
    fun unregisterHook(hook: EnchantRegistrationHook) {
        if (bukkitPlugin.server.pluginManager.getPlugin(hook.getPluginName()) != null) {
            kotlin.runCatching { hook.unregister() }
        }
    }

    /**
     * 注销所有钩子
     *
     * 在插件禁用时自动调用，注销所有已注册的钩子。
     */
    @Awake(LifeCycle.DISABLE)
    fun unregisterHooks() {
        registered.values.forEach { unregisterHook(it) }
    }
}