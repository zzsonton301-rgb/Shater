plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.shater.invoices"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.shater.invoices"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        create("release") {
            // استقبال المسار الصحيح من متغير البيئة أو ضبطه على مجلد التطبيق app/ مباشرة
            val envStoreFile = System.getenv("ANDROID_KEYSTORE_FILE")
            if (!envStoreFile.isNullOrBlank()) {
                storeFile = file(envStoreFile)
            } else {
                storeFile = file("release-key.jks") // سيتم تنفيذه داخل نطاق مجلد app
            }

            storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD") ?: "shater_pass_2026"
            keyAlias = System.getenv("ANDROID_KEY_ALIAS") ?: "shater_alias"
            keyPassword = System.getenv("ANDROID_KEY_PASSWORD") ?: "shater_pass_2026"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }

    configurations.all {
        resolutionStrategy {
            force("androidx.core:core-ktx:1.13.1")
            force("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
            force("androidx.core:core-splashscreen:1.0.1")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.09.03")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    
    implementation("com.google.mlkit:barcode-scanning:17.3.0")
    
    implementation("androidx.camera:camera-camera2:1.4.1")
    implementation("androidx.camera:camera-lifecycle:1.4.1")
    implementation("androidx.camera:camera-view:1.4.1")
    implementation("androidx.camera:camera-video:1.4.1")
    
    implementation("androidx.core:core-splashscreen:1.0.1")
    
    debugImplementation("androidx.compose.ui:ui-tooling")
}
