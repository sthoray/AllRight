# AllRight

A simple and fast browser for the AllGoods marketplace. Built for Android devices using Kotlin.

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/5b0c01100f364f5db9cc7d18912b58f6)](https://www.codacy.com/gh/sthoray/AllRight?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sthoray/AllRight&amp;utm_campaign=Badge_Grade)
[![Codecov](https://codecov.io/gh/sthoray/AllRight/branch/master/graph/badge.svg)](https://codecov.io/gh/sthoray/AllRight)
[![Android CI](https://github.com/sthoray/AllRight/workflows/Android%20CI/badge.svg)](https://github.com/sthoray/AllRight/actions?query=workflow%3A%22Android+CI%22)
[![Documentation](https://github.com/sthoray/AllRight/workflows/Documentation/badge.svg)](https://sthoray.github.io/AllRight/docs/app)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Build

The app can be built from source using [Android Studio](###Android-Studio) or via the [command line](###Command-line).

### Android Studio

Refer to the Android Studio [User Guide](https://developer.android.com/studio/run) for help building and running the app.

### Command line

To use the included Gradle wrapper you must have a working JDK version 11 or later installation. Once setup, the following command will create an installable APK at `app/build/outputs/apk/debug/app-debug.apk`

```bash
./gradlew assembleDebug
```

## Test

All tests must pass on GitHub before being accepted. To run tests locally, either use Android Studio or `gradlew`. Tests may fail when using `gradlew` depending on your systems JDK version. Try running the tests inside Android Studio if you encounter errors.

```bash
./gradlew test
```

## Documentation

Developer documentation can be found [here](https://sthoray.github.io/AllRight/docs/app). This will always be in sync with the master branch. The `dokka` Gradle task can also be used to generate local documentation.

## Disclaimer

This project is not associated with or officially supported by AllGoods.
