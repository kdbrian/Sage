import java.util.Properties

val secrets = Properties()
val secretsFile = rootProject.file("secrets.properties")

if (secretsFile.exists()) {
    secrets.load(secretsFile.inputStream())
}

fun getSecret(key: String): String? {
    return secrets[key] as? String ?: System.getenv(key)
}

plugins {

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    kotlin("plugin.serialization") version "2.2.0"
    alias(libs.plugins.google.gms.google.services)
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.kdbrian.sage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kdbrian.sage"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    flavorDimensions += "env"

    productFlavors {
        create("dev") {
            dimension = "env"
            val envValue = getSecret("ENV") ?: "dev"
            buildConfigField("String", "ENV", "\"$envValue\"")
        }
        create("staging") {
            dimension = "env"
            val envValue = getSecret("ENV") ?: "staging"
            buildConfigField("String", "ENV", "\"$envValue\"")
        }
        create("prod") {
            dimension = "env"
            val envValue = getSecret("ENV") ?: "prod"
            buildConfigField("String", "ENV", "\"$envValue\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //Timber
    implementation(libs.timber)

    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    //nav
    implementation(libs.androidx.navigation.compose)

    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //extended icons
    implementation(libs.androidx.material.icons.extended)

    //serialization
    implementation(libs.kotlinx.serialization.json)

    //datastore prefs
    implementation(libs.androidx.datastore.preferences)

    //accompanist permissions
    implementation(libs.accompanist.permissions)

    //room
    val room_version = "2.7.2"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:${room_version}")


}