plugins {
    kotlin("jvm") version "1.9.10"
    id("org.jetbrains.compose") version "1.6.0"
    kotlin("plugin.serialization") version "1.9.10"
}

group = "com.skyfiles"
version = "1.0.1"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.jetbrains.compose.ui:ui-test-junit4:1.6.0")
}

kotlin {
    jvmToolchain(17)
}

compose.desktop {
    application {
        mainClass = "skyfiles.MainKt" // Corrected main class
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe)
            packageName = "SkyFiles"
                    packageVersion = "1.0.1"
            description = "A modern file explorer for Windows."
            vendor = "SkyFiles"

            windows {
                menuGroup = "SkyFiles"
                iconFile.set(project.file("src/main/resources/icons/icon.ico"))
                dirChooser = true
                upgradeUuid = "a2a78f2d-2b5a-4259-9a21-95a7113d32a0" // Unique ID for updates
            }
        }
    }
}
