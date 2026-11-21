# HELP BOLDIGRAPHI — Native Android (Kotlin + Jetpack Compose)

**Tagline:** *Your Text, Your Style!*  
**Design System:** *Obsidian Luxury Typographer (Dark & Light Mode)*  
**AGP / Gradle:** Android Gradle Plugin `8.7.3` · Gradle Wrapper `8.9` · Kotlin `2.0.21` · Compose BOM `2024.12.01`

---

## Project Structure

```text
android/
├── gradlew
├── gradlew.bat
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── FONT_LICENSES.md
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
└── app/
    ├── build.gradle.kts
    ├── proguard-rules.pro
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── res/
        │   │   ├── drawable/ (ic_launcher_background.xml, ic_launcher_foreground.xml)
        │   │   ├── mipmap-anydpi-v26/ (ic_launcher.xml, ic_launcher_round.xml)
        │   │   ├── font/
        │   │   │   ├── mukta_regular.ttf & mukta_bold.ttf
        │   │   │   ├── hind_regular.ttf & hind_bold.ttf
        │   │   │   ├── noto_sans_devanagari_regular.ttf
        │   │   │   ├── tiro_devanagari_hindi_regular.ttf
        │   │   │   ├── rozha_one_regular.ttf
        │   │   │   ├── syne_bold.ttf
        │   │   │   └── space_grotesk_semibold.ttf
        │   │   ├── values/ (strings.xml, themes.xml)
        │   │   └── xml/file_paths.xml
        │   └── java/com/helpboldigraphi/app/
        │       ├── HelpBoldigraphiApp.kt
        │       ├── MainActivity.kt
        │       ├── data/PreferencesRepository.kt
        │       ├── domain/
        │       │   ├── model/TypographyModels.kt
        │       │   └── engine/
        │       │       ├── UnicodeStylizerEngine.kt
        │       │       ├── DevanagariCatalog.kt
        │       │       └── SocialTemplatesCatalog.kt
        │       ├── platform/AndroidShareHelper.kt
        │       └── ui/
        │           ├── theme/ (AppStrings.kt, Color.kt, Type.kt, Theme.kt)
        │           ├── viewmodel/StudioViewModel.kt
        │           ├── components/ (StyleCard.kt, ShareSheet.kt, PngExportSheet.kt)
        │           └── screens/
        │               ├── StudioScreen.kt
        │               ├── TemplatesScreen.kt
        │               ├── SavedScreens.kt (FavoritesScreen, HistoryScreen)
        │               └── SettingsScreen.kt
        └── test/java/com/helpboldigraphi/app/
            ├── UnicodeStylizerEngineTest.kt
            └── CatalogIntegrityTest.kt
```

---

## Opening in Android Studio

1. Launch **Android Studio Ladybug (2024.2+)** or newer with **JDK 17** configured under **Settings > Build, Execution, Deployment > Build Tools > Gradle**.
2. Choose **File > Open...** and select the `android/` folder.
3. Wait for Gradle Sync (`gradle-8.9-bin.zip` + AGP `8.7.3`) to complete.
4. Select a physical device or emulator (`API 24+`) and click **Run**.

---

## Running Unit Tests

Run all local JVM unit tests (`UnicodeStylizerEngineTest` & `CatalogIntegrityTest`) from `android/`:

```bash
cd android
./gradlew testDebugUnitTest
```

On Windows PowerShell / CMD:
```bat
cd android
gradlew.bat testDebugUnitTest
```

---

## APK Generation Guide

### 1. Debug APK (Direct Device Testing)
```bash
cd android
./gradlew assembleDebug
```
- Output APK: `android/app/build/outputs/apk/debug/app-debug.apk`
- Install via ADB:
  ```bash
  adb install -r app/build/outputs/apk/debug/app-debug.apk
  ```

### 2. Signed Release APK (Production Distribution)
1. Create a release keystore (one-time):
   ```bash
   keytool -genkey -v -keystore release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias boldigraphi
   ```
2. In Android Studio, open **Build > Generate Signed App Bundle / APK... > APK**, select `release.jks`, or configure `signingConfigs.create("release")` in `app/build.gradle.kts` and run:
   ```bash
   cd android
   ./gradlew assembleRelease
   ```
- Output APK: `android/app/build/outputs/apk/release/app-release.apk`
