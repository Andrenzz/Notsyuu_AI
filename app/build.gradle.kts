plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
    namespace = "id.notsyuu.assistant"
    compileSdk = 35
    defaultConfig {
        applicationId = "id.notsyuu.assistant"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0-preview"
        ndk { abiFilters += "arm64-v8a" }
    }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }
dependencies {
    implementation("androidx.core:core:1.15.0")
    implementation("com.google.ai.edge.litertlm:litertlm-android:0.18.1")
    testImplementation("junit:junit:4.13.2")
}
