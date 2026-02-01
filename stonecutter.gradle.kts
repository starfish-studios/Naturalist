plugins {
    id("dev.kikugie.stonecutter")
    id("co.uzzu.dotenv.gradle") version "4.0.0"
    id("fabric-loom") version "1.14-SNAPSHOT" apply false

    id("me.modmuss50.mod-publish-plugin") version "0.8.+" apply false
}

stonecutter active "1.21.10-fabric"

stonecutter parameters {
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

stonecutter tasks {
    order("publishModrinth")

}

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
    group = "publishing"
    dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}
