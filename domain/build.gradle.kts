plugins {
    id("java-library")
    alias (libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.coroutines.get()}")
    api("com.google.code.gson:gson:${libs.versions.gson.get()}")
    implementation("javax.inject:javax.inject:1")
}