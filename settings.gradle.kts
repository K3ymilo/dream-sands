pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "dream-sands"

include(":plugin-core")
include(":plugin-core:nms:api")
