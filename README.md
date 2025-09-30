# SkillApp

SkillApp is a native time-tracking application for Android that focuses on simplicty. 

## Features

* Different measurement units - hours, kilometers, times, pages (with reps, steps, calories, and kilograms coming soon)
* Skills can be organized into groups
* Dark theme

## Download

The app is available for download on [Google Play](https://play.google.com/store/apps/details?id=com.theskillapp.skillapp).

The app is also planned to be released on F-Droid in the near future.

## Build

To build the project, do the following:

1. Clone the repository
1. Enter the directory the repository was cloned to.
1. If the `$ANDROID_HOME` variable is not defined, you have 2 options:
    * Define `$ANDROID_HOME`, .e.g. by adding `export ANDROID_HOME=/path/to/Android/Sdk/` to your `~/.profile`
    * Create the `local.properties` file inside the project root with the following contents:
      
        ```
        sdk.dir=/path/to/Android/Sdk
        ```
1. If you want to perform a release build, add appropriate signing information to `buildSrc/src/main/kotlin/Signing.kt`.
1. Run `./gradlew assembleDebug` (for Linux and MacOS) or `gradle assembleDebug` (for Windows). That will produce a debug APK.


