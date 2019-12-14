plugins {
    id("kotlin")
    id("java-library")
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}")

    // Lint
    compileOnly("com.android.tools.lint:lint-api:${Versions.lint}")
    compileOnly("com.android.tools.lint:lint-checks:${Versions.lint}")

    // Lint testing
    testImplementation("com.android.tools.lint:lint:${Versions.lint}")
    testImplementation("com.android.tools.lint:lint-tests:${Versions.lint}")
    testImplementation("junit:junit:4.12")
}
