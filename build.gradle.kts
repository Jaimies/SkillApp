buildscript {
    repositories {
        google()
        jcenter()

        maven(url = uri("https://maven.fabric.io/public"))
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.2.0-rc03")

        classpath("com.google.gms:google-services:4.3.3")
        classpath("io.fabric.tools:gradle:1.31.2")
        classpath("com.google.firebase:perf-plugin:1.3.1")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(buildDir)
}
