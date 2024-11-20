import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.serialization.plugin)
    // alias(libs.plugins.kobwebx.markdown)
}

group = "com.example.blogmultiplatform"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")
        }
        server {
            remoteDebugging {
                enabled.set(true)
                port.set(5005)
            }
        }
    }
}

kotlin {
    configAsKobwebApplication("blogmultiplatform", includeServer = true)

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
        }

        jsMain.dependencies {
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.silk.icons.fa)
            implementation(libs.kotlinx.serialization)
            // implementation(libs.kobwebx.markdown)
            implementation(project(":worker"))
        }
        jvmMain.dependencies {
            implementation(libs.kobweb.api) // Provided by Kobweb backend at runtime
            implementation(libs.mongodb.kotlin.driver)
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.coroutine.core)
        }
    }
}
