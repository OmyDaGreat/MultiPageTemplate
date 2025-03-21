plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
}

tasks {
    register("startBoth") {
        group = "build"
        description = "Starts both the site and server"
        dependsOn(":site:kobwebStart", ":server:runServer")
    }
    register("stopBoth") {
        group = "build"
        description = "Stops both the site and server"
        dependsOn(":site:kobwebStop", ":server:stopServer")
    }
}
