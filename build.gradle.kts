
buildscript {
    repositories {
        google()
        jcenter()

        maven(url = uri("https://maven.fabric.io/public"))
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}")
        classpath("com.google.gms:google-services:${Versions.gms}")
        classpath("io.fabric.tools:gradle:${Versions.fabric}")
        classpath("com.google.firebase:perf-plugin:${Versions.perfClasspath}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
