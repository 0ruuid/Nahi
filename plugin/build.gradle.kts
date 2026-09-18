@file:Suppress("PropertyName", "SpellCheckingInspection")

plugins {
    id("com.github.johnrengelman.shadow")
    id("com.hiusers.klos") version "0.0.2"
}

val packagedProjects = rootProject.subprojects.filter { it.path.startsWith(":project:") }

// TabooLib 2.0.38 在打包时会解析 `taboo` 中的 ProjectDependency。
// 先完成这些子项目的配置，避免在 plugin 的 afterEvaluate 阶段延迟配置它们。
packagedProjects.forEach { evaluationDependsOn(it.path) }

taboolib {
    description {
        name(rootProject.name)
        prefix(rootProject.name)
        contributors {
            name("HiUsers")
        }
        links {
            name("homepage").url("https://iplugin.hiusers.com/")
        }
        dependencies {
            // name("Adyeshach").optional(true)
        }
    }

    classifier = null
}

dependencies {
    packagedProjects.forEach {
        taboo(project(it.path)) { isTransitive = false }
    }

    configurations.implementation.get().dependencies.forEach {
        configurations.taboo.get().dependencies.add(it)
    }
}

tasks {
    jar {
        // 构件名
        archiveBaseName.set(rootProject.name)
        // 添加 classifier 避免与 shadowJar 文件名冲突
        archiveClassifier.set("original")
        // 打包子项目输出
        packagedProjects.forEach {
            from(it.sourceSets["main"].output)
        }
    }

    // 注册源码包任务
    register<Jar>("sourcesJar") {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("sources")
        
        // 打包子项目源代码
        packagedProjects.forEach { subproject ->
            val sourceSets = subproject.extensions.findByType<SourceSetContainer>()
            sourceSets?.findByName("main")?.let { mainSourceSet ->
                from(mainSourceSet.allSource)
            }
        }
    }

    shadowJar {
        dependsOn(jar)
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("") // 移除 "-all" 后缀

        // 保持激进的排除策略，只保留 TabooLib 和 Klos 处理的库
        dependencies {
            exclude(dependency(".*:.*"))
        }
        from(jar.get().archiveFile)
    }

    build {
        dependsOn(shadowJar)
    }
}
