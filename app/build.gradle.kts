plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
    namespace = "id.notsyuu.assistant"
    compileSdk = 35
    defaultConfig {
        applicationId = "id.notsyuu.assistant"
        minSdk = 31
        targetSdk = 35
        versionCode = 2
        versionName = "0.2.0-preview"
        ndk { abiFilters += "arm64-v8a" }
    }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    androidResources { noCompress += "litertlm" }
}
kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }
dependencies {
    implementation("androidx.core:core:1.15.0")
    implementation("com.google.ai.edge.litertlm:litertlm-android:0.18.1")
    testImplementation("junit:junit:4.13.2")
}
val verifyBundledModel by tasks.registering {
    val model=layout.projectDirectory.file("src/main/assets/models/gemma3-1b-it-int4.litertlm")
    inputs.file(model)
    doLast {
        check(model.asFile.exists() && model.asFile.length()==584417280L) { "Bundled Gemma model missing or incomplete. Read README.md and run tools/prepare-model.ps1 before building." }
    }
}
tasks.named("preBuild") { dependsOn(verifyBundledModel) }
