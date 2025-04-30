plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.keak.kanjininja"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.keak.kanjininja"
        minSdk = 24
        targetSdk = 34
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
            buildConfigField (type = "String", name= "BaseUrl", value = "\"https://kanjialive-api.p.rapidapi.com/api/public/\"")
            buildConfigField (type = "String", name= "RapidApiKey", value = "\"3784f1e67bmshd30a30c7bd2fc8cp1d43a6jsnd2cfaabdc6b6\"")
        }
        debug {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField (type = "String", name= "BaseUrl", value = "\"https://kanjialive-api.p.rapidapi.com/api/public/\"")
            buildConfigField (type = "String", name= "RapidApiKey", value = "\"3784f1e67bmshd30a30c7bd2fc8cp1d43a6jsnd2cfaabdc6b6\"")
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
    implementation(libs.androidx.material)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //composeTypeSafeNavigation
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //Timber
    implementation(libs.timber)

    //retrofit
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.okhttp3.logging.interceptor)
    implementation (libs.retrofit2.kotlinx.serialization.converter)


    //compose
    implementation(libs.compose.coil)
    implementation(libs.material.icon)
    implementation(libs.androidx.core.splashscreen)
    implementation (libs.androidx.animation)

    //room
    ksp(libs.compose.room.compiler)
    implementation(libs.compose.room.paging)
    implementation(libs.compose.room.runtime)
    implementation(libs.compose.room.ktx)

    //lottie
    implementation(libs.lottie.compose)

    //datastore
    implementation (libs.androidx.datastore.preferences)
    implementation (libs.androidx.datastore.preferences.core)
}