# shop-items-demo

A small, polished Android shopping-list app built with Kotlin and Jetpack Compose.

## Features

- Add shopping items from a focused, keyboard-friendly input
- Mark items as purchased and filter by **All**, **To buy**, or **Done**
- Delete individual items, clear purchased items, and undo accidental deletions
- Persist the list locally between launches with `SharedPreferences`
- Accessible Material 3 UI with dark-mode and dynamic-color support

## Build

1. Install Android SDK 35 and set `ANDROID_HOME`.
2. Run `./gradlew assembleDebug`.
3. Install `app/build/outputs/apk/debug/app-debug.apk`.

Run local unit tests with `./gradlew test`.
