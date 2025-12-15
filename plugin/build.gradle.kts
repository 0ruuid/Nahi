@file:Suppress("PropertyName", "SpellCheckingInspection")

plugins {
    id("com.github.johnrengelman.shadow")
    id("com.hiusers.klos") version "0.0.2"
}

tasks {
    jar {
        // 构件名
        archiveBaseName.set(rootProject.name)
        // 避免与 shadowJar 产物冲突
        archiveClassifier.set("original")
    }

    // 注册源码包任务
    register<Jar>("sourcesJar") {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("sources")
        
        // 打包子项目源代码
        rootProject.subprojects.forEach { subproject ->
            val sourceSets = subproject.extensions.findByType<SourceSetContainer>()
            sourceSets?.findByName("main")?.let { mainSourceSet ->
                from(mainSourceSet.allSource)
            }
        }
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("") // 移除 "-all" 后缀

        // 保持激进的排除策略，只保留 TabooLib 和 Klos 处理的库
        dependencies {
            exclude(dependency(".*:.*"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}
