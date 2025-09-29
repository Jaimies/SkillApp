#!/bin/bash
./gradlew assembleDebug &&
    adb install app/build/outputs/apk/debug/app-debug.apk &&
    adb shell am start com.maxpoliakov.skillapp/.ui.MainActivity &&
    adb logcat *:E
