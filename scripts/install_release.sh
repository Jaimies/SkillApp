#!/bin/bash
./gradlew assembleRelease && 
    adb install app/build/outputs/apk/release/app-release.apk &&
    adb shell am start com.maxpoliakov.skillapp/.ui.MainActivity &&
    adb logcat *:E
