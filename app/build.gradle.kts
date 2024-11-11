import java.util.Properties

// build.gradle.kts (app module)

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.user_module"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.user_module"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Load environment variables from local.properties
        val localProperties = File(rootDir, "local.properties")
        val properties = Properties()

        if (localProperties.exists()) {
            properties.load(localProperties.inputStream())
        }

        val smtpHost: String = properties.getProperty("smtpHost", "")
        val smtpPort: String = properties.getProperty("smtpPort", "")
        val emailAddress: String = properties.getProperty("emailAddress", "")
        val emailPassword: String = properties.getProperty("emailPassword", "")

        // Pass them as buildConfigFields
        buildConfigField("String", "SMTP_HOST", "\"$smtpHost\"")
        buildConfigField("String", "SMTP_PORT", "\"$smtpPort\"")
        buildConfigField("String", "EMAIL_ADDRESS", "\"$emailAddress\"")
        buildConfigField("String", "EMAIL_PASSWORD", "\"$emailPassword\"")
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
    // Enable BuildConfig generation
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.javamail)
    implementation(libs.activation)
    implementation(libs.bcrypt)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.volley)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)
}
