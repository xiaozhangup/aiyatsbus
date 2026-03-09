package cc.polarastrum.aiyatsbus.module.script.fluxon

import taboolib.common.LifeCycle
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.inject.ClassVisitorHandler
import taboolib.common.io.runningClassMap
import taboolib.common.platform.Awake

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.module.script.fluxon.FluxonTestLoader
 *
 * @author mical
 * @since 2026/1/5 23:56
 */
@RuntimeDependencies(
    RuntimeDependency(
        repository = "https://repo.tabooproject.org/repository/releases",
        value = "!org.tabooproject.fluxon:core:1.6.14-2",
        test = "org.tabooproject.fluxon.Fluxon",
        relocate = ["!org.tabooproject.fluxon", "!cc.polarastrum.aiyatsbus.module.script.fluxon.core"],
        transitive = false,
        external = false
    ),
    RuntimeDependency(
        repository = "https://repo.tabooproject.org/repository/releases",
        value = "!org.tabooproject.fluxon:inst-core:1.6.14-2",
        test = "org.tabooproject.fluxon.inst.InjectionSpec",
        relocate = ["!org.tabooproject.fluxon", "!cc.polarastrum.aiyatsbus.module.script.fluxon.core"],
        transitive = false,
        external = false
    ),
    RuntimeDependency(
        repository = "https://repo.tabooproject.org/repository/releases",
        value = "!org.tabooproject.fluxon.plugin:core:1.1.3-2",
        test = "org.tabooproject.fluxon.ParseScript",
        relocate = ["!org.tabooproject.fluxon", "!cc.polarastrum.aiyatsbus.module.script.fluxon.core", "!kotlin.", "!kotlin1822.", "!taboolib.", "!cc.polarastrum.aiyatsbus.taboolib."],
        transitive = false,
        external = false
    ),
    RuntimeDependency(
        repository = "https://repo.tabooproject.org/repository/releases",
        value = "!org.tabooproject.fluxon.plugin:common:1.1.3-2",
        test = "org.tabooproject.fluxon.FluxonPlugin",
        relocate = ["!org.tabooproject.fluxon", "!cc.polarastrum.aiyatsbus.module.script.fluxon.core", "!kotlin.", "!kotlin1822.", "!taboolib.", "!cc.polarastrum.aiyatsbus.taboolib."],
        transitive = false,
        external = false
    ),
    RuntimeDependency(
        repository = "https://repo.tabooproject.org/repository/releases",
        value = "!org.tabooproject.fluxon.plugin:platform-bukkit:1.1.3-2",
        test = "org.tabooproject.fluxon.platform.bukkit.function.FnEnumGetter",
        relocate = ["!org.tabooproject.fluxon", "!cc.polarastrum.aiyatsbus.module.script.fluxon.core", "!kotlin.", "!kotlin1822.", "!taboolib.", "!cc.polarastrum.aiyatsbus.taboolib."],
        transitive = false,
        external = false
    )
)
object FluxonTestLoader {

    @Awake(LifeCycle.INIT)
    fun init() {
        runningClassMap.filter { it.key.startsWith("cc.polarastrum.aiyatsbus.module.script.fluxon.core") }
            .forEach { (_, clazz) ->
                if (ClassVisitorHandler.checkPlatform(clazz) && ClassVisitorHandler.checkRequires(clazz)) {
                    ClassVisitorHandler.injectAll(clazz)
                }
            }
    }
}