plugins {
    id("dev.kikugie.stonecutter")
    id("co.uzzu.dotenv.gradle") version "4.0.0"
    id("fabric-loom") version "1.11-SNAPSHOT" apply false
    id("net.neoforged.moddev.legacyforge") version "2.0.115" apply false
    id("me.modmuss50.mod-publish-plugin") version "0.8.+" apply false
}

stonecutter active "1.20.1-forge"

stonecutter parameters {
    val loader = node.metadata.project.substringAfterLast('-')

    constants.match(loader, "fabric", "forge")

    if (loader == "forge") {
        filters.exclude(
            "**/fabric/**",
            "**/core/platform/fabric/**",
            "**/core/registry/fabric/**",
            "**/mixin/fabric/**",
            "**/common/item/fabric/**"
        )
    }

    if (loader == "fabric") {
        filters.exclude(
            "**/forge/**",
            "**/core/platform/forge/**",
            "**/core/registry/forge/**",
            "**/mixin/forge/**",
            "**/world/forge/**",
            "**/util/forge/**",
            "**/item/forge/**"
        )
    }
}

stonecutter tasks {
    order("publishModrinth")
    order("publishCurseforge")
}

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
    group = "publishing"
    dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}
