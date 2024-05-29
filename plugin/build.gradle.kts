plugins {
    id("java-library-convention")
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    implementation(libs.fabric.mappingio)

    compileOnly(libs.paper.api)
    paperweight.paperDevBundle(libs.paper.api.get().version)
}

tasks {

    build {
        dependsOn(shadowJar)
    }

    jar {
        archiveBaseName = "mcSpy"
    }

    shadowJar {
        archiveBaseName = "mcSpy"
        relocate("net.fabricmc", "org.machinemc.mcspy.fabricmc")
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

}
