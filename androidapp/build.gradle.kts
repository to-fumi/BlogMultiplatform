import java.util.Properties

val envFile = rootProject.file("env.properties")
val env = Properties()
if (envFile.exists()) {
    env.load(envFile.inputStream())
} else {
    throw GradleException("Missing env.properties file. Please add it to the root directory.")
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization.plugin)
    alias(libs.plugins.androidx.hilt)
    alias(libs.plugins.kotlin.kapt)
}


android {
    namespace = "com.example.androidapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.androidapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "DITTO_APP_ID", "${env["DITTO_APP_ID"]}")
        buildConfigField("String", "DITTO_PLAYGROUND_TOKEN", "${env["DITTO_PLAYGROUND_TOKEN"]}")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/**"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.coroutine.core)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.serialization)
    implementation(libs.ditto.sync)
    implementation(libs.ditto.tools.viewer)
    implementation(libs.androidx.hilt)
    implementation(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.splashscreen)
    implementation(project(":shared"))
}

kapt {
    correctErrorTypes = true
}
