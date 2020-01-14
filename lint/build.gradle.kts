plugins {
    id("kotlin")
}

repositories {
    google()
    jcenter()
}

dependencies {

    compileOnly(kotlin("stdlib-jdk7", Versions.kotlin))
    compileOnly("com.android.tools.lint:lint-api:${Versions.lint}")
    compileOnly("com.android.tools.lint:lint-checks:${Versions.lint}")
}
