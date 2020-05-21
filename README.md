# AllRight

A blazing fast browser for the AllGoods marketplace. Built for Android devices using Kotlin.

![](https://github.com/sthoray/AllRight/workflows/Android%20Master%20Build/badge.svg)

## Build debug APK

The app can be built using [Android Studio](###Android-Studio) or via the [command line](###Command-line).

### Android Studio

To create a build using [Android Studio](https://developer.android.com/studio), clone this repository using Android Studio (or your preferred git tool). Please refer to Android Studio [User Guide](https://developer.android.com/studio/run) for help building and running the app.

### Command line

Clone this repository. You can then build the app using the included Gradle wrapper. You will need to have a working JDK installation between versions 8 and 13. Java 14 and later are not supported by Gradle 5.6.4.

On Windows:

```cmd
.\gradlew.bat assembleDebug
```

On Mac or Linux:

```bash
./gradlew assembleDebug
```

This creates an APK named `app-debug.apk` in `app\build\outputs\apk\debug` which can be installed on a device.

## Disclaimer

This project is not associated with or officially supported by AllGoods.
