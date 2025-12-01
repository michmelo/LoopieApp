plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.loopieapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.loopieapp"
        minSdk = 33
        targetSdk = 36
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
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/io.netty.versions.properties"
        }
    }

    signingConfigs {

        release {
            storeFile file("loopie_release.jks")
            storePassword "mmelo1884"
            keyAlias "loopiekey"
            keyPassword "mmelo1884
    }

    buildTypes {

        release {
            minifyEnabled false
            signingConfig signingConfigs.release
        }
    }
}
val nav_version = "2.7.7"

dependencies {
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.compose.material:material-icons-extended-android:1.6.7") // Or the latest version
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0") // Or the latest version
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.firebase.appdistribution.gradle)
    implementation("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // --- DEPENDENCIAS PARA CONEXIÓN A LA API ---
    // Retrofit (el cliente HTTP)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson Converter (para convertir JSON a objetos Kotlin y viceversa)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp Logging Interceptor (muy útil para depurar, te permite ver las peticiones en el Logcat)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}