plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${libs.versions.jdkDesugar.get()}")
    implementation(project(":domain"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${libs.versions.coroutines.get()}")
    api("com.google.code.gson:gson:${libs.versions.gson.get()}")
    api("com.squareup.retrofit2:retrofit:${libs.versions.retrofit.get()}")
    api("com.squareup.retrofit2:converter-gson:${libs.versions.retrofit.get()}")
    api("com.squareup.okhttp3:okhttp:${libs.versions.okhttp.get()}")
    api("com.squareup.okhttp3:logging-interceptor:${libs.versions.okhttp.get()}")

    implementation("com.google.dagger:hilt-android:${libs.versions.dagger.get()}")
    kapt("com.google.dagger:hilt-compiler:${libs.versions.dagger.get()}")

    implementation("androidx.room:room-ktx:${libs.versions.room.get()}")
    implementation("androidx.room:room-runtime:${libs.versions.room.get()}")
    ksp("androidx.room:room-compiler:${libs.versions.room.get()}")
}