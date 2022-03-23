plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version "1.6.10-1.0.4"
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.mattrobertson.handlebar.sample"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    kotlin.sourceSets.main {
        kotlin.srcDirs(
            file("$buildDir/generated/ksp/"),
        )
    }


}

dependencies {

    implementation(project(":handlebar"))
    implementation(project(path = ":handlebar-annotation", configuration = "default"))
    ksp(project(path = ":handlebar-ksp", configuration = "default"))

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

    testImplementation("junit:junit:4.+")
}