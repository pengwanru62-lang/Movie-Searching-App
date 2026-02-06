# Movie Searching App

简体中文说明 — 包含两个变体：Compose 与 XML

**项目简介**

这是一个用于搜索电影的 Android 示例应用，包含两个独立的模块：

- `MovieApp-Compose/`：使用 Jetpack Compose 实现的版本。
- `MovieApp-xml/`：使用传统 XML 布局实现的版本。

**主要功能**

- 基于关键字搜索电影。
- 列表展示与电影详情页（海报、简介等）。

**开发与运行要求**

- JDK 11+（或项目 `local.properties` 指定的版本）
- Android Studio Arctic Fox 及以上（推荐）
- Android SDK / 平台工具与模拟器或真机

**快速开始（在 Windows 上）**

1. 打开项目：在 Android Studio 中选择 `Open an existing project`，打开工作区根目录。
2. 使用 Gradle 构建：在工作区根目录或子模块目录下运行（PowerShell / CMD）：

```
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug    # 如需安装到已连接设备
```

3. 在 Android Studio 中运行：选择要运行的模块（`app`），选择目标设备或模拟器，点击 Run。

**模块说明**

- `MovieApp-Compose/app/`：Compose 版本的 Android 源码和资源。
- `MovieApp-xml/app/`：XML 版本的 Android 源码和资源。

如果你只想构建某个模块，可进入对应目录并运行：

```
cd MovieApp-Compose
.\gradlew.bat :app:assembleDebug

cd ..\MovieApp-xml
.\gradlew.bat :app:assembleDebug
```

**常见命令**

- 运行单元测试（如果存在）： `.\gradlew.bat test` 
- 运行 Android 测试（设备/模拟器连接）： `.\gradlew.bat connectedAndroidTest`

**贡献**

欢迎提交 issue 或 PR。请在提交前运行格式化和相关测试。

**License**

请在仓库中添加或替换为合适的开源许可证（例如 MIT、Apache-2.0）。


