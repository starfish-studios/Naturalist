@file:Suppress("UnstableApiUsage")

plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom")
    id("me.modmuss50.mod-publish-plugin")
}

fun prop(key: String) = project.property(key).toString()

tasks.named<ProcessResources>("processResources") {
    val props = mapOf(
        "version" to "${prop("mod.version")}+${prop("deps.minecraft")}",
        "minecraft" to prop("mod.mc_dep_fabric"),
        "mod_version" to prop("mod.version"),
        "mod_description" to prop("mod.description")
    )

    inputs.properties(props)

    filesMatching(listOf("fabric.mod.json", "META-INF/mods.toml")) {
        expand(props)
    }
}

version = "${prop("mod.version")}+${prop("deps.minecraft")}-fabric"
base.archivesName = prop("mod.id")

loom {
    val modId = prop("mod.id")
    val accessWidener = file("src/main/resources/$modId.accesswidener")
    if (accessWidener.exists()) {
        accessWidenerPath = accessWidener
    }
}

repositories {
    mavenLocal()
    maven {
        name = "Terraformers (Mod Menu)"
        url = uri("https://maven.terraformersmc.com/releases/")
        content {
            includeGroupAndSubgroups("com.terraformersmc")
        }
    }
    maven {
        name = "shedaniel (Cloth Config)"
        url = uri("https://maven.shedaniel.me/")
        content {
            includeGroupAndSubgroups("me.shedaniel")
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
        name = "Parchment Mappings"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        content {
            includeGroupByRegex("software\\.bernie.*")
            includeGroup("com.eliotlash.mclib")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
    mappings(loom.layered {
        officialMojangMappings()
        (findProperty("deps.parchment") as String?)?.let {
            parchment("org.parchmentmc.data:parchment-$it@zip")
        }
    })
    modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")

    (findProperty("deps.modmenu") as String?)?.let {
        modApi("com.terraformersmc:modmenu:$it") {
            exclude(group = "net.fabricmc")
        }
    }

    (findProperty("deps.geckolib") as String?)?.let {
        modImplementation("software.bernie.geckolib:geckolib-fabric-${prop("deps.minecraft")}:$it")
    }

    (findProperty("deps.cloth_config") as String?)?.let {
        modApi("me.shedaniel.cloth:cloth-config-fabric:$it") {
            exclude(group = "net.fabricmc.fabric-api")
            exclude(group = "net.fabricmc", module = "fabric-loader")
        }
    }

    (findProperty("deps.midnightlib") as String?)?.let {
        modImplementation("maven.modrinth:midnightlib:$it-fabric") {
            exclude(group = "net.fabricmc", module = "fabric-loader")
        }
    }

    compileOnly("org.jetbrains:annotations:24.1.0")
}

// Exclude Forge-specific source files from Fabric builds
sourceSets["main"].java {
    exclude("**/forge/**")
    exclude("**/core/platform/forge/**")
    exclude("**/core/registry/forge/**")
    exclude("**/mixin/forge/**")
    exclude("**/world/forge/**")
    exclude("**/util/forge/**")
    exclude("**/item/forge/**")
}

tasks {
    processResources {
        exclude("**/neoforge.mods.toml", "**/mods.toml")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile })
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
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=1.21")) {
        JavaVersion.VERSION_21
    } else {
        JavaVersion.VERSION_21
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
//     file = tasks.remapJar.map { it.archiveFile.get() }
//     additionalFiles.from(tasks.remapSourcesJar.map { it.archiveFile.get() })
//
//     type = STABLE
//     displayName = "${prop("mod.name")} ${prop("mod.version")} for ${stonecutter.current.version} Fabric"
//     version = "${prop("mod.version")}+${prop("deps.minecraft")}-fabric"
//     changelog = provider { rootProject.file("CHANGELOG.md").readText() }
//     modLoaders.add("fabric")
//
//     modrinth {
//         projectId = prop("publish.modrinth")
//         accessToken = env.MODRINTH_API_KEY.orNull()
//         minecraftVersions.add(stonecutter.current.version)
//         minecraftVersions.addAll(additionalVersions)
//         requires("fabric-api")
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
//         requires("fabric-api")
//         if (findProperty("deps.geckolib") != null) {
//             requires("geckolib")
//         }
//     }
// }