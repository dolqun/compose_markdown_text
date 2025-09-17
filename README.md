# Jetpack Compose Markdown Renderer

A lightweight Jetpack Compose plugin for rendering Markdown text in Android applications with RTL language support.

![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-2025.06.01-brightgreen.svg)
![Kotlin](https://img.shields.io/badge/kotlin-2.2.0-blue.svg)

## 🎬 Demo

<!-- TODO: Add demo GIF -->
![Markdown Rendering Demo](docs/demo.mp4)

## 📦 Installation

### Gradle Dependency

```kotlin
dependencies {
    implementation("com.adev.compose:markdowntext:1.0.0")
}
```

### Local Build

```bash
./gradlew :markdowntext:build
./gradlew :markdowntext:publishToMavenLocal
```

## 🚀 Quick Start

### Basic Usage

```kotlin
import com.adev.compose.markdowtext.markdown.MarkdownText

@Composable
fun MarkdownScreen() {
    val content = """
        # Welcome
        This is **bold** text and this is *italic* text.
        
        ## Features
        - Full Markdown support
        - RTL language support (Uyghur, Arabic)
        - Code highlighting
        - Image loading
        
        [Visit Website](https://example.com)
    """
    
    MarkdownText(
        markdown = content,
        modifier = Modifier.fillMaxSize()
    )
}
```

### Advanced Usage

```kotlin
@Composable
fun AdvancedMarkdown() {
    val markdown = """
        # Advanced Example
        
        ## Code Block
        ```kotlin
        fun main() {
            println("Hello Markdown!")
        }
        ```
        
        ## Table
        | Name | Age | Role |
        |------|-----|------|
        | John | 25  | Developer |
        | Jane | 28  | Designer |
        
        ## Image
        ![Sample](https://picsum.photos/400/200)
    """
    
    MarkdownText(
        markdown = markdown,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        onLinkClick = { url ->
            // Handle link click
        }
    )
}
```

## 📋 API Reference

### MarkdownText Composable

```kotlin
@Composable
fun MarkdownText(
    markdown: String,                           // Required: Markdown text content
    modifier: Modifier = Modifier,              // Optional: Layout modifier
    isShowLineNumbers: Boolean = false,         // Optional: Show line numbers in code blocks
    codeContent: @Composable (String, String) -> Unit = { code, language ->
        CodeBlockElement(code, language, isShowLineNumbers) // Optional: Custom code block rendering
    },
    imageContent: @Composable (String) -> Unit = { url ->
        ImageElement(url, onClick = {})         // Optional: Custom image rendering
    },
    onClick: (String) -> Unit = {},             // Optional: Text click callback
    onLongClick: (String) -> Unit = {},         // Optional: Text long-click callback
    onLinkClick: (String) -> Unit = {},         // Optional: Link click callback
)
```

### Parameter Details

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `markdown` | `String` | - | **Required** Markdown text content |
| `modifier` | `Modifier` | `Modifier` | Layout modifier for sizing/padding |
| `isShowLineNumbers` | `Boolean` | `false` | Show line numbers in code blocks |
| `codeContent` | `@Composable` | `CodeBlockElement` | Custom code block renderer |
| `imageContent` | `@Composable` | `ImageElement` | Custom image renderer |
| `onClick` | `(String) -> Unit` | `{}` | Text click callback with full text |
| `onLongClick` | `(String) -> Unit` | `{}` | Text long-click callback with full text |
| `onLinkClick` | `(String) -> Unit` | `{}` | Link click callback with URL |

## 🎯 Real-World Examples

### 1. News Content Display

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

### 2. Help Documentation

```kotlin
@Composable
fun HelpDocument() {
    val helpText = """
        # Getting Started
        
        ## Quick Guide
        Welcome to our app! Follow these steps:
        
        1. **Sign Up** - Click register button
        2. **Complete Profile** - Fill your info
        3. **Start Using** - Explore features
        
        ## FAQ
        - **Q: How to reset password?**
          A: Click "Forgot Password" on login
        
        - **Q: Contact support?**
          A: Email support@example.com
    """
    
    MarkdownText(
        markdown = helpText,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    )
}
```

### 3. RTL Language (Uyghur)

```kotlin
@Composable
fun UyghurContent() {
    val uyghurText = """
        # ئەپ توغۇرلاش
        
        ## ئادەتتىكى ئىشلىتىش
        بۇ **ئەپ** تىن پايدىلىنىپ markdown تېكىستىنى كۆرسىتىشكە بولىدۇ.
        
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

## ⚙️ Configuration

### Version Requirements

```kotlin
// build.gradle.kts
android {
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
}
```

### ProGuard Rules

```proguard
# MarkdownText
-keep class com.adev.compose.markdowtext.** { *; }
-dontwarn com.adev.compose.markdowtext.**
```

## 🔧 Integration Checklist

- [ ] Add Gradle dependency
- [ ] Set minimum SDK to 24+
- [ ] Configure ProGuard (if needed)
- [ ] Test RTL language support
- [ ] Setup image loading (Coil)
- [ ] Handle link click events

## 📱 Running Examples

```bash
# Install sample app
./gradlew :app:installDebug

# Or run directly
./gradlew :app:run
```

## 🎨 Theming

Automatically adapts to Material 3 theme:

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

## 📊 Performance

- **Lazy Loading**: Large text renders in segments
- **Image Caching**: Automatic Coil integration
- **Memory Optimization**: Efficient recompositions
- **Layout Optimization**: `Column` + `verticalScroll`

## 🐛 Troubleshooting

**Q: Images not displaying?**
A: Ensure Coil dependency and Internet permission are added

**Q: RTL language issues?**
A: Check device language settings for RTL support

**Q: Link clicks not working?**
A: Implement `onLinkClick` for navigation handling

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/adev/markdown-compose/issues)
- **Examples**: Check `app/src/main/java/com/adev/compose/markdown/sample/`

---
**MIT License** - Free for commercial and personal use