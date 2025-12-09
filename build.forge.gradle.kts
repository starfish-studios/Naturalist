@file:Suppress("UnstableApiUsage")

plugins {
    id("dev.kikugie.stonecutter")
    id("net.neoforged.moddev.legacyforge") version "2.0.115"
    id("me.modmuss50.mod-publish-plugin")
}

fun prop(key: String) = project.property(key).toString()

tasks.named<ProcessResources>("processResources") {
    val props = mapOf(
        "version" to "${prop("mod.version")}+${prop("deps.minecraft")}",
        "minecraft" to prop("mod.mc_dep_forgelike"),
        "mod_version" to prop("mod.version"),
        "mod_description" to prop("mod.description")
    )

    inputs.properties(props)

    filesMatching("META-INF/mods.toml") {
        expand(props)
    }
}

version = "${prop("mod.version")}+${prop("deps.minecraft")}-forge"
base.archivesName = prop("mod.id")

repositories {
    mavenLocal()
    maven {
        name = "Parchment Mappings"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroupAndSubgroups("maven.modrinth")
        }
    }
    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        content {
            includeGroupAndSubgroups("software.bernie.geckolib")
        }
    }
    maven {
        name = "Minecraft Forge"
        url = uri("https://maven.minecraftforge.net/")
        content {
            includeGroup("net.minecraftforge")
        }
    }
}

legacyForge {
    version = prop("deps.forge")
    validateAccessTransformers = true

    if (hasProperty("deps.parchment")) parchment {
        val (mc, ver) = (prop("deps.parchment")).split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }

    runs {
        register("client") {
            gameDirectory = file("run/")
            client()
        }
        register("server") {
            gameDirectory = file("run/")
            server()
        }
    }

    mods {
        register(prop("mod.id")) {
            sourceSet(sourceSets["main"])
        }
    }
    sourceSets["main"].resources.srcDir("src/main/generated")
}

// Exclude Fabric-specific source files from Forge builds
sourceSets["main"].java {
    exclude("**/fabric/**")
    exclude("**/core/platform/fabric/**")
    exclude("**/core/registry/fabric/**")
    exclude("**/mixin/fabric/**")
    exclude("**/common/item/fabric/**")
}

dependencies {
    (findProperty("deps.geckolib") as String?)?.let {
        modImplementation("software.bernie.geckolib:geckolib-forge-${prop("deps.minecraft")}:$it")
    }

    (findProperty("deps.midnightlib") as String?)?.let {
        modImplementation("maven.modrinth:midnightlib:$it-forge")
    }

    compileOnly("org.jetbrains:annotations:24.1.0")
}

tasks {
    processResources {
        exclude("**/fabric.mod.json", "**/*.accesswidener", "**/neoforge.mods.toml")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${prop("mod.version")}"))
        dependsOn("build")
    }

    register("publishModrinth") {
        group = "publishing"
        doLast { logger.lifecycle("Modrinth publishing skipped.") }
    }
    register("publishCurseforge") {
        group = "publishing"
        doLast { logger.lifecycle("CurseForge publishing skipped.") }
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) {
        JavaVersion.VERSION_21
    } else {
        JavaVersion.VERSION_17
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

// publishMods {
//     val additionalVersions: List<String> = (findProperty("publish.additionalVersions") as String?)
//         ?.split(",")
//         ?.map { it.trim() }
//         ?.filter { it.isNotEmpty() }
//         ?: emptyList()
//
//     file = tasks.jar.map { it.archiveFile.get() }
//     additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })
//
//     type = BETA
//     displayName = "${prop("mod.name")} ${prop("mod.version")} for ${stonecutter.current.version} Forge"
//     version = "${prop("mod.version")}+${prop("deps.minecraft")}-forge"
//     changelog = provider { rootProject.file("CHANGELOG.md").readText() }
//     modLoaders.add("forge")
//
//     modrinth {
//         projectId = prop("publish.modrinth")
//         accessToken = env.MODRINTH_API_KEY.orNull()
//         minecraftVersions.add(stonecutter.current.version)
//         minecraftVersions.addAll(additionalVersions)
//         if (findProperty("deps.geckolib") != null) {
//             requires("geckolib")
//         }
//     }
//
//     curseforge {
//         projectId = prop("publish.curseforge")
//         accessToken = env.CURSEFORGE_API_KEY.orNull()
//         minecraftVersions.add(stonecutter.current.version)
//         minecraftVersions.addAll(additionalVersions)
//         if (findProperty("deps.geckolib") != null) {
//             requires("geckolib")
//         }
//     }
// }