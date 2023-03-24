plugins {
    alias (libs.plugins.android.library)
    alias (libs.plugins.kotlin.android)
    alias (libs.plugins.kotlin.kapt)
    alias (libs.plugins.kotlin.parcelize)
    alias (libs.plugins.hilt)
}

android {
    namespace = "com.example.presentation"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":domain"))
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${libs.versions.jdkDesugar.get()}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${libs.versions.coroutines.get()}")
    implementation("com.google.dagger:hilt-android:${libs.versions.dagger.get()}")
    kapt("com.google.dagger:hilt-compiler:${libs.versions.dagger.get()}")

    val composeBom = platform("androidx.compose:compose-bom:${libs.versions.composeBom.get()}")
    implementation(composeBom)
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-core")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.navigation:navigation-compose:${libs.versions.composeNav.get()}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${libs.versions.composeViewModel.get()}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${libs.versions.composeViewModel.get()}")
    implementation("androidx.hilt:hilt-navigation-compose:${libs.versions.composeHilt.get()}")
    implementation("io.coil-kt:coil-compose:${libs.versions.composeCoil.get()}")

    implementation("androidx.core:core-ktx:${libs.versions.core.get()}")

    testImplementation("io.mockk:mockk:${libs.versions.mockk.get()}")
    testImplementation("org.junit.jupiter:junit-jupiter:${libs.versions.junit.get()}")
    testImplementation("org.hamcrest:hamcrest:${libs.versions.hamcrest.get()}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${libs.versions.coroutines.get()}")
}