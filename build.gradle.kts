plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kobweb.library) apply false
    alias(libs.plugins.serialization.plugin) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://us-central1-maven.pkg.dev/varabyte-repos/public")
    }
}
