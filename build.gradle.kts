allprojects {
    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://maven.fabricmc.net/") {
            name = "FabricMC"
            content {
                includeGroupAndSubgroups("net.fabricmc")
            }
        }
        maven("https://maven.minecraftforge.net/") {
            name = "Minecraft Forge"
            content {
                includeGroup("net.minecraftforge")
            }
        }
        maven("https://maven.parchmentmc.org") {
            name = "ParchmentMC"
            content {
                includeGroupAndSubgroups("org.parchmentmc")
            }
        }
        maven("https://maven.shedaniel.me/") {
            name = "shedaniel (Cloth Config)"
            content {
                includeGroupAndSubgroups("me.shedaniel")
            }
        }
        maven("https://maven.terraformersmc.com/") {
            name = "Terraformers (Mod Menu)"
            content {
                includeGroupAndSubgroups("com.terraformersmc")
            }
        }
        maven("https://api.modrinth.com/maven") {
            name = "Modrinth"
            content {
                includeGroupAndSubgroups("maven.modrinth")
            }
        }
        maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") {
            name = "GeckoLib"
            content {
                includeGroupByRegex("software\\.bernie.*")
                includeGroup("com.eliotlash.mclib")
            }
        }
        maven("https://maven.isxander.dev/releases/") {
            name = "Xander Maven"
            content {
                includeGroupAndSubgroups("dev.isxander")
                includeGroupAndSubgroups("org.quiltmc.parsers")
            }
        }
        maven("https://cursemaven.com") {
            name = "CurseMaven"
            content {
                includeGroup("curse.maven")
            }
        }
    }
}