plugins {

    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")

    id("androidx.navigation.safeargs.kotlin")
//    id("io.fabric")
//    id("com.google.firebase.firebase-perf")
    id("com.google.gms.google-services")
    id("io.gitlab.arturbosch.detekt") version Versions.detekt
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

        testInstrumentationRunner = "com.jdevs.timeo.TimeoTestRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
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

    sourceSets {

        getByName("androidTest") {

            java.setSrcDirs(listOf("src/sharedTest/kotlin", "src/androidTest/kotlin"))
        }

        getByName("test") {

            java.setSrcDirs(listOf("src/sharedTest/kotlin", "src/test/kotlin"))
        }
    }

    dataBinding.isEnabled = true
}

detekt {

    ignoreFailures = true
    autoCorrect = true

    reports {

        xml.enabled = false
        txt.enabled = false
    }
}

dependencies {

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}")

    // Core Android libraries and legacy support
    implementation("androidx.core:core-ktx:${Versions.androidxCore}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}")
    implementation("androidx.legacy:legacy-support-v4:${Versions.legacy}")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")

    // Extensions
    implementation("androidx.fragment:fragment-ktx:${Versions.fragmentKtx}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigation}")

    // Material theme
    implementation("com.google.android.material:material:${Versions.material}")

    // Firebase
    implementation("com.google.firebase:firebase-auth:${Versions.auth}")
    implementation("com.google.android.gms:play-services-auth:${Versions.gmsAuth}")
    implementation("com.google.firebase:firebase-firestore:${Versions.firestore}")
    debugImplementation("com.google.code.gson:gson:2.8.6")
//    implementation("com.google.firebase:firebase-analytics:${Versions.analytics}")
//    implementation("com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}")
//    implementation("com.google.firebase:firebase-perf:${Versions.perf}")

    // Room
    implementation("androidx.room:room-runtime:${Versions.room}")
    implementation("androidx.room:room-ktx:${Versions.room}")
    kapt("androidx.room:room-compiler:${Versions.room}")

    // Paging
    implementation("androidx.paging:paging-runtime:${Versions.paging}")

    // Dependency injection
    implementation("com.google.dagger:dagger:${Versions.dagger}")
    kapt("com.google.dagger:dagger-compiler:${Versions.dagger}")
    kaptAndroidTest("com.google.dagger:dagger-compiler:${Versions.dagger}")

    // Data Binding
    kapt("com.android.databinding:compiler:${Versions.dataBinding}")

    // Lint
    lintChecks(project(path = ":lint"))

    // Test
    testImplementation("junit:junit:${Versions.junit}")
    testImplementation("org.hamcrest:hamcrest-all:${Versions.hamcrest}")

    // Android Test

    androidTestImplementation("androidx.test.ext:junit:${Versions.junitExt}")
    androidTestImplementation("androidx.test:runner:${Versions.androidxTestRunner}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espresso}")
    androidTestImplementation("androidx.room:room-testing:${Versions.room}")
    androidTestImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}")
    androidTestImplementation("org.mockito:mockito-core:${Versions.mockito}")
    androidTestImplementation("com.linkedin.dexmaker:dexmaker-mockito:${Versions.dexMaker}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Versions.espresso}")

    // Testing code should not be included in the main code.
    // Once https://issuetracker.google.com/128612536 is fixed this can be fixed.

    implementation("androidx.fragment:fragment-testing:${Versions.fragment}")
    implementation("androidx.test:core:${Versions.androidxTestCore}")

    // Browsing SQLite database
    // To enable uncomment the following line, run adb forward tcp:8080 tcp:8080
    // (/home/alex/Android/Sdk/platform-tools/adb forward tcp:8080 tcp:8080)
    // and connect to http://localhost:8080
    // debugImplementation("com.amitshekhar.android:debug-db:1.0.6")
}
