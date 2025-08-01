plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.customerconnect.customerconnect_mobile"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.customerconnect.customerconnect_mobile"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    buildTypes {
        release {
            // TODO: Add your own signing config for the release build.
            // Signing with the debug keys for now, so `flutter run --release` works.
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

flutter {
    source = "../.."
}

dependencies {
    // ... other existing dependencies ...

    // For networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // For JSON parsing with Gson
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3") // For logging requests (use a recent version)

    // For handling session cookies
    implementation("com.github.franmontiel:PersistentCookieJar:v1.0.1")

    // For ViewModel and LiveData (if not already present)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // Use latest version
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0") // Use latest version
    implementation("androidx.activity:activity-compose:1.8.2") // If using Jetpack Compose
}