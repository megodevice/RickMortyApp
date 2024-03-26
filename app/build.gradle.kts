plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltAndroidPlugin)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.ilia_zusik.rickmortyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ilia_zusik.rickmortyapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


        buildConfigField("String", "BASEURL", "\"https://rickandmortyapi.com/api/\"")
        buildConfigField("String", "CHARACTER", "\"character\"")
        buildConfigField("String", "PAGE", "\"page\"")
        buildConfigField("String", "ALIVE", "\"Alive\"")
        buildConfigField("String", "DEATH", "\"Dead\"")
        buildConfigField("String", "EPISODE", "\"episode/\"")


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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // retrofit2
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //okhttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // coil
    implementation(libs.coil)

    // hilt
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)
    kapt(libs.hilt.compiler)

    // fragmentX activityX
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)

    // lottie animation
    implementation(libs.lottie)

    // Navigation Component
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
}

kapt {
    correctErrorTypes = true
}