
plugins {

    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")

    id("androidx.navigation.safeargs.kotlin")
    id("io.fabric")
    id("com.google.firebase.firebase-perf")
    id("com.google.gms.google-services")
}

android {

    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {

        kotlinOptions.jvmTarget = "1.8"
    }

    signingConfigs {

        create("release") {

            storeFile = file(Signing.keyPath)
            storePassword = Signing.storePassword
            keyAlias = Signing.keyAlias
            keyPassword = Signing.keyPassword
        }
    }

    compileSdkVersion(App.compileSdk)
    buildToolsVersion("29.0.2")

    defaultConfig {

        applicationId = App.applicationId
        minSdkVersion(App.minSdk)
        targetSdkVersion(App.targetSdk)

        versionCode = App.versionCode
        versionName = App.versionName

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

    buildFeatures.dataBinding = true
}

dependencies {

    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}")

    implementation("androidx.core:core-ktx:${Versions.androidxCore}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}")
    implementation("androidx.legacy:legacy-support-v4:${Versions.legacy}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigation}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}")
    implementation("com.google.android.material:material:${Versions.material}")
    implementation("joda-time:joda-time:${Versions.jodatime}")

    implementation("com.google.firebase:firebase-firestore:${Versions.firestore}")
    implementation("com.google.firebase:firebase-auth:${Versions.auth}")
    implementation("com.google.firebase:firebase-analytics:${Versions.analytics}")
    implementation("com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}")
    implementation("com.google.firebase:firebase-perf:${Versions.perf}")
    implementation("com.google.android.gms:play-services-auth:${Versions.gmsAuth}")

    testImplementation("junit:junit:${Versions.junit}")
    testImplementation("org.hamcrest:hamcrest-all:${Versions.hamcrest}")

    androidTestImplementation("androidx.test:runner:${Versions.androidxTestRunner}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espresso}")
}
