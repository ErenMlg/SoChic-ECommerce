import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.apollo3.gradle.plugin)
    alias(libs.plugins.hilt.plugin)
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.softcross.ecommercecompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.softcross.ecommercecompose"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_ENDPOINT", "\"${properties.getProperty("API_ENDPOINT")}\"")
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

apollo {
    service("ecommerceService") {
        packageName.set("com.softcross.ecommerce")
        introspection {
            headers.put("culture", "tr-TR")
            headers.put("language", "tr")
            headers.put("Content-Type", "application/json")
            endpointUrl.set(properties.getProperty("API_ENDPOINT"))
            schemaFile.set(file("src/main/graphql/com/softcross/ecommercecompose/schema.graphqls"))
        }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.material)
    debugImplementation(libs.androidx.ui.test.manifest)
    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    //Lottie
    implementation(libs.com.airbnb.android.lottie)
    //Coil
    implementation(libs.coil.kt.compose)
    //Apollo for graphql
    implementation(libs.apollo3.apollo.runtime)
}