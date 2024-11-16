plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.secrets.plugin)
    alias(libs.plugins.google.service)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "com.romoreno.compraplus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.romoreno.compraplus"
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
        // Habilitar ViewBinding
        viewBinding = true
        buildConfig = true
    }
}

secrets {
    // Para setear la clave de Google Maps en el proyecto
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}


dependencies {

    // Android y Material
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // DaggerHilt (Dependencia de DaggerHilt para Android y del compilador. Necesita el plugin de kapt y el de dagger (desactivando su ejecucion inmediata)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Retrofit  (Retrofit, Gson y Logger HTTP)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.gson)
    implementation(libs.okhttp.logging)

    // NavComponent (Con esto se añade la navegacion, el delegado de los viewModel para descargarse el control de su ciclo de vida, corrutinas, etc)
    // Tambien plugin de safeargs para pasar datos entre vistas
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Google Maps
    implementation(libs.google.maps)
    implementation(libs.google.location)
    implementation(libs.google.places)

    // Glide
    implementation(libs.bumptech.glide)

    // Splash Screen Api
    implementation(libs.androidx.core.splashscreen)

    // Swipe Refresh Layout
    implementation(libs.androidx.swiperefreshlayout)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.realtime)
    implementation ("com.google.android.gms:play-services-auth:20.5.0")
    implementation ("com.google.android.gms:play-services-auth-api-phone")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Unit Tests
    testImplementation(libs.junit)
    testImplementation(libs.runner.junit)
    testImplementation(libs.io.mockk)
    testImplementation(libs.coroutines.test)
    testImplementation("androidx.arch.core:core-testing:2.1.0")

    // Android Tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}