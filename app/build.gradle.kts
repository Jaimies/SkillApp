plugins {

    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")

    id("androidx.navigation.safeargs.kotlin")
    id("io.fabric")
    id("com.google.firebase.firebase-perf")
}
android {

    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    signingConfigs {

        create("release") {
            storeFile = file("/home/alex/StudioProjects/Timeo/signing.jks")
            storePassword = "PASSWORD"
            keyAlias = "KEY_ALIAS"
            keyPassword = "PASSWORD"
        }
    }

    compileSdkVersion(29)
    buildToolsVersion("29.0.2")
    defaultConfig {
        applicationId = "com.jdevs.timeo"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "0.0.1-alpha08"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders = mapOf("crashlyticsEnabled" to true)
        }

        getByName("debug") {

            manifestPlaceholders = mapOf("crashlyticsEnabled" to false)
        }
    }

    dataBinding {

        isEnabled = true
    }
}

dependencies {
    val materialVersion = "1.0.0"
    val coroutinesVersion = "1.3.2"

    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.61")

    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")

    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    implementation("com.google.firebase:firebase-analytics:17.2.1")
    implementation("com.google.firebase:firebase-firestore:21.3.0")
    implementation("com.google.firebase:firebase-auth:19.2.0")
    implementation("com.crashlytics.sdk.android:crashlytics:2.10.1")
    implementation("com.google.firebase:firebase-perf:19.0.2")

    implementation("androidx.navigation:navigation-fragment-ktx:2.2.0-rc03")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.0-rc03")

    implementation("com.google.android.material:material:$materialVersion")
    implementation("com.google.android.gms:play-services-auth:17.0.0")

    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("joda-time:joda-time:2.9.4")

    testImplementation("junit:junit:4.12")
    testImplementation("org.hamcrest:hamcrest-all:1.3")

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

apply(plugin = "com.google.gms.google-services")
