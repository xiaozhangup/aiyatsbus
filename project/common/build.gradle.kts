import org.jetbrains.dokka.gradle.DokkaTask

repositories {
    maven("https://jitpack.io")
}

apply(plugin = "org.jetbrains.dokka")

dependencies {
    // 如果不需要跨平台，可以在此处引入 Bukkit 核心
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    // compileOnly("ink.ptms.core:v11903:11903:mapped")
    // compileOnly("ink.ptms.core:v11903:11903:universal")
    compileOnly("com.github.Redempt:Crunch:1.0.7")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.2-beta-r3-b")
}

tasks.named<DokkaTask>("dokkaJavadoc") {
    moduleName.set("Aiyatsbus Common")
    outputDirectory.set(buildDir.resolve("docs/javadoc"))
    dokkaSourceSets.configureEach {
        jdkVersion.set(17)
        skipDeprecated.set(false)
        reportUndocumented.set(false)
    }
}

// 子模块
taboolib { subproject = true }
