plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.openaiproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.openaiproject"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true // ✅ enable custom BuildConfig fields
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "OPENAI_API_KEY", "\"${project.findProperty("openai.api.key") ?: ""}\"")
        }
        getByName("release") {
            buildConfigField("String", "OPENAI_API_KEY", "\"${project.findProperty("openai.api.key") ?: ""}\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }



}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}