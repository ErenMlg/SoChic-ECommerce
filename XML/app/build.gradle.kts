import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.apollo3.gradle.plugin)
    alias(libs.plugins.dagger.hilt.android.gradle.plugin)
    alias(libs.plugins.navigationSafeArgs)
    alias(libs.plugins.kotlinKsp)
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    signingConfigs{
        create("release") {
            storeFile = file("C:\\Users\\erenm\\Documents\\mykeystore\\sochic.jks")
            storePassword = properties.getProperty("STORE_PASSWORD")
            keyAlias = properties.getProperty("KEY_ALIAS")
            keyPassword = properties.getProperty("KEY_PASSWORD")
        }
    }
    namespace = "com.softcross.ecommerce"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.softcross.ecommerce"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_ENDPOINT", "\"${properties.getProperty("API_ENDPOINT")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures{
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
            schemaFile.set(file("src/main/graphql/com/softcross/ecommerce/schema.json"))
        }
    }
}


dependencies {
    debugImplementation(libs.leakcanary.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Apollo for graphql
    implementation(libs.apollo3.apollo.runtime)
    //Dagger Hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.compiler)
    //Animation and Image
    implementation(libs.lottie)
    implementation(libs.glide)
    ksp(libs.compiler)
    //Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

}