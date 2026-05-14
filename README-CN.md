# Audio Detection Tool

(简体中文 / [English](./README.md))

## 概述

这个应用程序旨在检测用户的音频输入，并提供关于音高和其他细节的分析。

|      平台       |                     预览                     |       备注       |
|:-------------:|:------------------------------------------:|:--------------:|
| JVM (Desktop) |     ![JVM View](./assets/jvm_view.png)     |      功能完整      |
|    Android    | ![Android View](./assets/android_view.png) |      功能完整      |
|      iOS      |     ![iOS View](./assets/ios_view.png)     |      未测试       |
|     WASM      |    ![WASM View](./assets/wasm_view.png)    |      未测试       |
|      Web      |     ![Web View](./assets/web_view.png)     |     效果不理想      |

## 功能

- 音高检测

### 未来功能

- 频谱图
- 录音
- 分析音频文件

## 如何运行

这是针对 Android、iOS、Web、Desktop (JVM) 的 Kotlin 多平台项目。

* [/composeApp](./composeApp/src) 用于跨您的 Compose Multiplatform 应用程序共享的代码。
  它包含几个子文件夹：
  - [commonMain](./composeApp/src/commonMain/kotlin) 用于所有目标通用的代码。
  - 其他文件夹用于仅为文件夹名称中指示的平台编译的 Kotlin 代码。
    例如，如果您想在 Kotlin 应用的 iOS 部分使用 Apple 的 CoreCrypto，
    [iosMain](./composeApp/src/iosMain/kotlin) 文件夹将是此类调用的正确位置。
    同样，如果您想编辑 Desktop (JVM) 特定部分，[jvmMain](./composeApp/src/jvmMain/kotlin)
    文件夹是合适的位置。

* [/iosApp](./iosApp/iosApp) 包含 iOS 应用程序。即使您使用 Compose Multiplatform 共享 UI，
  您也需要此入口点作为 iOS 应用。这也是您应该为项目添加 SwiftUI 代码的地方。

### 构建和运行 Android 应用程序

要构建和运行 Android 应用的开发版本，请使用 IDE 工具栏中的运行配置小部件，或直接从终端构建：
- 在 macOS/Linux 上
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- 在 Windows 上
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### 构建和运行 Desktop (JVM) 应用程序

要构建和运行桌面应用的开发版本，请使用 IDE 工具栏中的运行配置小部件，或直接从终端运行：
- 在 macOS/Linux 上
  ```shell
  ./gradlew :composeApp:run
  ```
- 在 Windows 上
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### 构建和运行 Web 应用程序

要构建和运行 Web 应用的开发版本，请使用 IDE 工具栏中的运行配置小部件，或直接从终端运行：
- 对于 Wasm 目标（更快，现代浏览器）：
  - 在 macOS/Linux 上
    ```shell
    ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
    ```
  - 在 Windows 上
    ```shell
    .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
    ```
- 对于 JS 目标（较慢，支持旧版浏览器）：
  - 在 macOS/Linux 上
    ```shell
    ./gradlew :composeApp:jsBrowserDevelopmentRun
    ```
  - 在 Windows 上
    ```shell
    .\gradlew.bat :composeApp:jsBrowserDevelopmentRun
    ```

### 构建和运行 iOS 应用程序

要构建和运行 iOS 应用的开发版本，请使用 IDE 工具栏中的运行配置小部件，或在 Xcode 中打开 [/iosApp](./iosApp) 目录并从那里运行。

---

了解更多关于 [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)，
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform)，
[Kotlin/Wasm](https://kotl.in/wasm/)…

我们欢迎您在公共 Slack 频道 [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web) 中提供关于 Compose/Web 和 Kotlin/Wasm 的反馈。
如果您遇到任何问题，请在 [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP) 上报告。
