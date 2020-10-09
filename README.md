<p>
    <h1 align="center">
        AllRight
    </h1>
</p>

<p align="center">
    <a href="https://www.codacy.com/gh/sthoray/AllRight?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sthoray/AllRight&amp;utm_campaign=Badge_Grade">
        <img src="https://app.codacy.com/project/badge/Grade/5b0c01100f364f5db9cc7d18912b58f6" alt="Code quality">
    </a>
    <a href="https://codecov.io/gh/sthoray/AllRight">
        <img src="https://codecov.io/gh/sthoray/AllRight/branch/master/graph/badge.svg" alt="Code coverage">
    </a>
    <a href="https://sthoray.github.io/AllRight/docs/app">
        <img src="https://github.com/sthoray/AllRight/workflows/Documentation/badge.svg" alt="Docs status">
    </a>
    <a href="https://github.com/sthoray/AllRight/actions?query=workflow%3A%22Android+CI%22">
        <img src="https://github.com/sthoray/AllRight/workflows/Android%20CI/badge.svg" alt="Build status">
    </a>
    <a href="./LICENSE">
        <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License: Apache 2.0">
    </a>
</p>

<p align="center">
    <a href="#about">About</a>
  • <a href="#build">Build</a>
  • <a href="#test">Test</a>
  • <a href="#documentation">Documentation</a>
  • <a href="#disclaimer">Disclaimer</a>
</p>


## About

A simple and fast browser for the AllGoods marketplace. Built for Android devices using Kotlin.

AllRight is designed to make browsing AllGoods categories painless. We aim to minimise complex layouts to provide reliable and responsive interfaces. When the app first launches, users can immeditally select a category to start browsing. To further refine results, filters and marketplace categories can be adjusted in the filters page by tapping on the floating FILTER button. Uers can discard their filters and return to existing results by using their devices back button instead of the APPLY FILTERS button at the bottom of the page. When viewing a product's details, the currently displayed image will be enlarged once tapped on. The devices back button will navigate back to the product details as usual. The VIEW LISTING button at the bottom of the product details allows users to  visit the product in their web browser for more information or to make a purchase. 

<p align="center">
    <a href="https://play.google.com/store/apps/details?id=com.sthoray.allright">
        <img src="https://cdn.rawgit.com/steverichey/google-play-badge-svg/master/img/en_get.svg" width="30%">
    </a>
</p>

## Build

The app can be built from source using [Android Studio](###Android-Studio) or via the [command line](###Command-line). Signed release variants should be downloaded from the [Google Play Store](https://play.google.com/store/apps/details?id=com.sthoray.allright).

### Android Studio

Please refer to the Android Studio [User Guide](https://developer.android.com/studio/run) for help building and running the app.

### Command line

To use the included Gradle wrapper you must have a working JDK version 11 or later installation. Once setup, the following command will create an installable APK at `app/build/outputs/apk/debug/app-debug.apk`

```bash
./gradlew assembleDebug
```

## Test

All tests must pass on GitHub before being accepted. To run tests locally, either use Android Studio or `gradlew`. Tests may fail when using `gradlew` depending on your systems JDK version. Try running the tests inside Android Studio if you encounter errors.

```bash
./gradlew test                  # Unit tests
./gradlew connectedAndroidTest  # Instrumented tests
```

## Documentation

Developer documentation can be found [here](https://sthoray.github.io/AllRight/docs/app). This is generated every time the `master` branch recieves a new commit. The `dokkaHtml` Gradle task can be used to generate local documentation on your machine.

## Disclaimer

This project is not associated with or officially supported by ALLGOODS GROUP LIMITED.
