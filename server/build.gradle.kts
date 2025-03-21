import java.util.Locale

plugins {
    kotlin("jvm")
    application
}

group = "xyz.malefic.multipage"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.http4k:http4k-bom:6.2.0.0"))
    implementation(libs.bundles.http4k)
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks {
    test {
        useJUnitPlatform()
    }

    val runServer by registering {
        group = "application"
        description = "Run the server"
        doLast {
            val pidFile = File("server.pid")
            if (pidFile.exists()) {
                val pid = pidFile.readText().trim().toInt()
                println("Stopping previous server with PID: $pid")
                if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win")) {
                    ProcessBuilder("taskkill", "/F", "/PID", "$pid").start()
                } else {
                    ProcessBuilder("kill", "$pid").start()
                }
                pidFile.delete()
            } else {
                println("No prior server is running.")
            }

            val process =
                ProcessBuilder("java", "-cp", sourceSets["main"].runtimeClasspath.asPath, "xyz.malefic.multipage.MainKt")
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .start()
            val pid = process.pid()
            pidFile.writeText(pid.toString())
            println("Server started with PID: $pid")
        }
    }

    val stopServer by registering {
        group = "application"
        description = "Stop the server"
        doLast {
            val pidFile = File("server.pid")
            if (pidFile.exists()) {
                val pid = pidFile.readText().trim().toInt()
                println("Stopping server with PID: $pid")
                if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win")) {
                    ProcessBuilder("taskkill", "/F", "/PID", "$pid").start()
                } else {
                    ProcessBuilder("kill", "$pid").start()
                }
                pidFile.delete()
            } else {
                println("No prior server is running.")
            }
        }
    }
}

application {
    mainClass.set("xyz.malefic.multipage.MainKt")
}
