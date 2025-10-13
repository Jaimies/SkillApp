# SkillApp

SkillApp is a native time-tracking application for Android that focuses on simplicity that doesn't collect any of your personal data.

## Features

* Completely offline: the app doesn't even have the INTERNET permission
* Organize related skills into groups
* Various measurement units: Hours, Kilometers, Times, Pages, Reps, Steps, Calories
* Set your goals and achieve them
* See your productivity at a glance with statistics
* Local backup into a folder of your choosing
* Stay productive at any time of day with the Dark mode

## Download

* The app is available for download on [F-Droid](https://f-droid.org/en/packages/com.theskillapp.skillapp/).
* You can also download get the latest version under [releases](https://github.com/Jaimies/SkillApp/releases).

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


