# AllRight

A simple and fast browser for the AllGoods marketplace. Built for Android devices using Kotlin.

![Android Build](https://github.com/sthoray/AllRight/workflows/Android%20Build/badge.svg)
![Documentation](https://github.com/sthoray/AllRight/workflows/Documentation/badge.svg)

## Build from source

The app can be built from source using [Android Studio](###Android-Studio) or via the [command line](###Command-line).

### Android Studio

Refer to the Android Studio [User Guide](https://developer.android.com/studio/run) for help building and running the app.

### Command line

To use the included Gradle wrapper you must have a working JDK version 8 or later installation. Once setup, the following command will create an installable APK at `app/build/outputs/apk/debug/app-debug.apk`

On Windows:

```cmd
.\gradlew.bat assembleDebug
```

On MacOS or Linux:

```bash
./gradlew assembleDebug
```

## Documentation

Developer documentation can be found [here](https://sthoray.github.io/AllRight/index.html). This will always be in sync with the master branch. The `dokka` Gradle task can also be used to generate local documentation.

## Disclaimer

This project is not associated with or officially supported by AllGoods.
