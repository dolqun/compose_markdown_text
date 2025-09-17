# Jetpack Compose Markdown 渲染插件

一个轻量级的 Jetpack Compose 插件，用于在 Android 应用中高效渲染 Markdown 文本，支持 RTL 语言。

![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-2025.06.01-brightgreen.svg)
![Kotlin](https://img.shields.io/badge/kotlin-2.2.0-blue.svg)

## 🎬 效果演示

<!-- TODO: 添加演示 GIF -->
![Markdown渲染演示](docs/demo.mp4)

## 📦 安装

### Gradle 依赖

```kotlin
dependencies {
    implementation("com.adev.compose:markdowntext:1.0.0")
}
```

### 本地构建

```bash
./gradlew :markdowntext:build
./gradlew :markdowntext:publishToMavenLocal
```

## 🚀 快速开始

### 基础用法

```kotlin
import com.adev.compose.markdowtext.markdown.MarkdownText

@Composable
fun MarkdownScreen() {
    val content = """
        # 欢迎使用
        这是一个**功能齐全**的*Markdown*渲染插件。
        
        ## 特性
        - 支持所有基础Markdown语法
        - 支持RTL语言（维吾尔语、阿拉伯语）
        - 支持代码高亮
        - 支持图片加载
        
        [访问官网](https://example.com)
    """
    
    MarkdownText(
        markdown = content,
        modifier = Modifier.fillMaxSize()
    )
}
```

### 进阶用法

```kotlin
@Composable
fun AdvancedMarkdown() {
    val markdown = """
        # 复杂示例
        
        ## 代码块
        ```kotlin
        fun main() {
            println("Hello Markdown!")
        }
        ```
        
        ## 表格
        | 名称 | 年龄 | 职业 |
        |------|------|------|
        | 张三 | 25   | 工程师 |
        | 李四 | 28   | 设计师 |
        
        ## 图片
        ![示例图片](https://picsum.photos/400/200)
    """
    
    MarkdownText(
        markdown = markdown,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        onLinkClick = { url ->
            // 处理链接点击
        }
    )
}
```

## 📋 API 参数说明

### MarkdownText 可组合函数

```kotlin
@Composable
fun MarkdownText(
    markdown: String,                           // 必填：Markdown文本内容
    modifier: Modifier = Modifier,              // 可选：布局修饰符
    isShowLineNumbers: Boolean = false,         // 可选：代码块显示行号
    codeContent: @Composable (String, String) -> Unit = { code, language ->
        CodeBlockElement(code, language, isShowLineNumbers) // 可选：自定义代码块渲染
    },
    imageContent: @Composable (String) -> Unit = { url ->
        ImageElement(url, onClick = {})         // 可选：自定义图片渲染
    },
    onClick: (String) -> Unit = {},             // 可选：文本点击事件
    onLongClick: (String) -> Unit = {},         // 可选：文本长按事件
    onLinkClick: (String) -> Unit = {},         // 可选：链接点击事件
)
```

### 参数详解

| 参数名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `markdown` | `String` | - | **必填** Markdown文本内容 |
| `modifier` | `Modifier` | `Modifier` | 布局修饰符，控制大小、内边距等 |
| `isShowLineNumbers` | `Boolean` | `false` | 代码块是否显示行号 |
| `codeContent` | `@Composable` | `CodeBlockElement` | 自定义代码块渲染函数 |
| `imageContent` | `@Composable` | `ImageElement` | 自定义图片渲染函数 |
| `onClick` | `(String) -> Unit` | `{}` | 文本点击回调，返回完整文本 |
| `onLongClick` | `(String) -> Unit` | `{}` | 文本长按回调，返回完整文本 |
| `onLinkClick` | `(String) -> Unit` | `{}` | 链接点击回调，返回URL地址 |

## 🎯 实战示例

### 1. 新闻内容展示

```kotlin
@Composable
fun NewsContent(news: News) {
    Column {
        MarkdownText(
            markdown = news.content,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onLinkClick = { url ->
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                )
            }
        )
    }
}
```

### 2. 帮助文档页面

```kotlin
@Composable
fun HelpDocument() {
    val helpMd = remember {
        """
        # 使用帮助
        
        ## 快速开始
        欢迎使用本应用！请按以下步骤操作：
        
        1. **注册账号** - 点击右上角注册按钮
        2. **完善信息** - 填写个人资料
        3. **开始使用** - 探索各项功能
        
        ## 常见问题
        - **Q: 如何重置密码？**
          A: 点击登录页面的"忘记密码"
        
        - **Q: 如何联系客服？**
          A: 发送邮件至 support@example.com
        """
    }
    
    MarkdownText(
        markdown = helpMd,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    )
}
```

### 3. 维吾尔语内容

```kotlin
@Composable
fun UyghurContent() {
    val uyghurText = """
        # قاچىلاش تەسىساتى
        
        ## ئادەتتىكى ئىشلىتىش
        بۇ **ئەپ** تىن پايدىلىنىپ ئادەتتىكى markdown تېكىستىنى كۆرسىتىشكە بولىدۇ.
        
        ### ئىقتىدارلار
        - ئۇيغۇرچە تېكىستنى قوللايدۇ
        - كود بوغچىسىنى قوللايدۇ
        - رەسىمنى قوللايدۇ
        
        [تەپسىلىي](https://example.com/ug)
    """
    
    MarkdownText(
        markdown = uyghurText,
        modifier = Modifier.fillMaxSize()
    )
}
```

## ⚙️ 配置参数

### 依赖版本

```kotlin
// build.gradle.kts
android {
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
}
```

### ProGuard 规则

```proguard
# MarkdownText
-keep class com.adev.compose.markdowtext.** { *; }
-dontwarn com.adev.compose.markdowtext.**
```

## 🔧 集成检查清单

- [ ] 添加 Gradle 依赖
- [ ] 设置最低 SDK 版本为 24+
- [ ] 配置 ProGuard (如需要)
- [ ] 测试 RTL 语言支持
- [ ] 配置图片加载 (Coil)
- [ ] 处理链接点击事件

## 📱 运行示例

```bash
# 安装示例应用到设备
./gradlew :app:installDebug

# 或直接运行
./gradlew :app:run
```

## 🎨 主题适配

自动适配 Material 3 主题：

```kotlin
@Composable
fun ThemedMarkdown() {
    val colors = MaterialTheme.colorScheme
    
    MarkdownText(
        markdown = content,
        modifier = Modifier.background(colors.background)
    )
}
```

## 📊 性能优化

- **延迟加载**: 大文本分段渲染
- **图片缓存**: 自动使用 Coil 缓存
- **内存优化**: 合理使用 `@Composable` 重组
- **布局优化**: 使用 `Column` + `verticalScroll`

## 🐛 常见问题

**Q: 图片不显示？**
A: 确保添加了 Coil 依赖和 Internet 权限

**Q: RTL 语言显示异常？**
A: 检查设备语言设置，确保支持 RTL

**Q: 链接点击无效？**
A: 需要手动实现 `onLinkClick` 处理跳转逻辑

## 📞 技术支持

- **Issues**: [GitHub Issues](https://github.com/adev/markdown-compose/issues)
- **示例代码**: 查看 `app/src/main/java/com/adev/compose/markdown/sample/`

---
**MIT License** - 免费用于商业和个人项目