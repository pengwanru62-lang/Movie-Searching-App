plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")   // ✅ kapt 的正确写法
}

android {
    namespace = "com.example.movieappcompose"
    compileSdk = 34   // ⚠️ 当前 AGP 8.x 稳定支持 34，不建议使用 35/36（尚未正式发布）

    defaultConfig {
        applicationId = "com.example.movieappcompose"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // Compose Compiler 1.5.10 requires Kotlin 1.9.22 (compatibility map); keep this pair aligned.
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))

    // Compose core
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.graphics)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material.icons.extended)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Networking & JSON
    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.converter.moshi)
    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.moshi)
    kapt(libs.squareup.moshi.kotlin.codegen)

    // Room (database)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // Coil
    implementation(libs.coil.compose)

    // Ensure Kotlin runtime stdlib matches the Kotlin plugin version (1.9.22)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")

    // Removed explicit kotlin stdlib dependency; Kotlin plugin and version catalog manage stdlib version.
}

kapt {
    correctErrorTypes = true
}


// Removed explicit kotlin { jvmToolchain(17) } to prevent Gradle toolchain provisioning failure.
// Android Studio already supplies a compatible JDK 17; compileOptions + kotlinOptions ensure targeting 17.
// If you prefer an explicit toolchain, install JDK 17 locally and set JAVA_HOME, or enable Foojay access.
