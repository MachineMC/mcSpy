rootProject.name = "mcSpy"

include("plugin")

pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    versionCatalogs {

        create("libs") {
            val jetbrainsAnnotations: String by settings
            library("jetbrains-annotations", "org.jetbrains:annotations:$jetbrainsAnnotations")

            val junit: String by settings
            library("junit-api", "org.junit.jupiter:junit-jupiter-api:$junit")
            library("junit-engine", "org.junit.jupiter:junit-jupiter-engine:$junit")
            library("junit-params", "org.junit.jupiter:junit-jupiter-params:$junit")

            val lombok: String by settings
            library("lombok", "org.projectlombok:lombok:$lombok")

            val fabricMappingIO: String by settings
            library("fabric-mappingio", "net.fabricmc:mapping-io:$fabricMappingIO")

            val paperApi: String by settings
            library("paper-api", "io.papermc.paper:paper-api:$paperApi")

            val shadow: String by settings
            plugin("shadow", "io.github.goooler.shadow").version(shadow)

            val paperweightUserDev: String by settings
            plugin("paperweight.userdev", "io.papermc.paperweight.userdev").version(paperweightUserDev)
        }

    }
}
