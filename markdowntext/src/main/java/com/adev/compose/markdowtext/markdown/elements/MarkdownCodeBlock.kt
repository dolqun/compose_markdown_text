package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 优化的代码块组件，支持语法高亮、行号和复制功能
 */
@Composable
fun MarkdownCodeBlock(
    code: String,
    language: String,
    showLineNumbers: Boolean = false,
    onCopyClick: (String) -> Unit = {}
) {
    val lines = code.split("\n")
    val horizontalScrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF6F8FA)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        // 顶部语言标签和复制按钮
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEAECEF))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = language,
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            IconButton(
                onClick = {
                    onCopyClick.invoke(code)
                },
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // 代码内容
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 12.dp, bottom = 12.dp, top = 8.dp)
        ) {
            // 行号列
            if (showLineNumbers){
                Column(
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    lines.forEachIndexed { index, _ ->
                        Text(
                            text = "${index + 1}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(end = 4.dp, top = 0.dp, bottom = 0.dp)
                                .width(20.dp),
                            lineHeight = 20.sp
                        )
                    }
                }
            }


            // 代码内容列 - 添加水平滚动
            Box(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(horizontalScrollState)
            ) {
                Column {
                    lines.forEach { line ->
                        val highlightedLine = highlightCode(line, language)
                        Text(
                            text = highlightedLine,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(vertical = 0.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 代码高亮函数 - 简化的语法高亮
 */
private fun highlightCode(code: String, language: String): AnnotatedString {
    return buildAnnotatedString {
        append(code)
        
        val normalizedLang = language.lowercase()
        
        when (normalizedLang) {
            "kotlin" -> applyKotlinHighlighting(code)
            "java" -> applyJavaHighlighting(code)
            "javascript", "js" -> applyJavaScriptHighlighting(code)
            "typescript", "ts" -> applyTypeScriptHighlighting(code)
            "python", "py" -> applyPythonHighlighting(code)
            "c++", "cpp" -> applyCppHighlighting(code)
            "c" -> applyCHighlighting(code)
            "csharp", "c#" -> applyCSharpHighlighting(code)
            else -> applyGenericHighlighting(code)
        }
    }
}

private fun AnnotatedString.Builder.applyKotlinHighlighting(code: String) {
    val keywords = listOf("fun", "val", "var", "class", "interface", "object", "if", "else", "when", "for", "while", "return", "import", "package", "private", "public", "protected", "internal", "data", "sealed")
    val literals = listOf("true", "false", "null")
    
    applyHighlighting(code, keywords, literals)
}

private fun AnnotatedString.Builder.applyJavaHighlighting(code: String) {
    val keywords = listOf("public", "private", "protected", "class", "interface", "extends", "implements", "import", "package", "return", "if", "else", "for", "while", "try", "catch", "finally", "throw", "throws")
    val literals = listOf("true", "false", "null")
    
    applyHighlighting(code, keywords, literals)
}

private fun AnnotatedString.Builder.applyJavaScriptHighlighting(code: String) {
    val keywords = listOf("function", "const", "let", "var", "if", "else", "for", "while", "return", "import", "export", "class", "extends", "async", "await")
    val literals = listOf("true", "false", "null", "undefined")
    
    applyHighlighting(code, keywords, literals)
}

private fun AnnotatedString.Builder.applyTypeScriptHighlighting(code: String) {
    val keywords = listOf("function", "const", "let", "var", "if", "else", "for", "while", "return", "import", "export", "class", "extends", "interface", "type", "async", "await")
    val literals = listOf("true", "false", "null", "undefined")
    
    applyHighlighting(code, keywords, literals)
}

private fun AnnotatedString.Builder.applyPythonHighlighting(code: String) {
    val keywords = listOf("def", "class", "if", "else", "elif", "for", "while", "import", "from", "return", "try", "except", "finally", "with", "as")
    val literals = listOf("True", "False", "None")
    
    applyHighlighting(code, keywords, literals)
}

private fun AnnotatedString.Builder.applyCppHighlighting(code: String) {
    val keywords = listOf("int", "float", "double", "char", "void", "class", "public", "private", "protected", "namespace", "using", "return", "if", "else", "for", "while", "try", "catch", "throw")
    val literals = listOf("true", "false", "nullptr", "NULL")
    
    applyHighlighting(code, keywords, literals)
}

private fun AnnotatedString.Builder.applyCHighlighting(code: String) {
    val keywords = listOf("int", "float", "double", "char", "void", "struct", "union", "enum", "return", "if", "else", "for", "while", "switch", "case", "break", "continue")
    val literals = listOf("true", "false", "NULL")
    
    applyHighlighting(code, keywords, literals)
}

private fun AnnotatedString.Builder.applyCSharpHighlighting(code: String) {
    val keywords = listOf("public", "private", "protected", "class", "interface", "namespace", "using", "return", "if", "else", "for", "foreach", "while", "try", "catch", "finally", "throw")
    val literals = listOf("true", "false", "null")
    
    applyHighlighting(code, keywords, literals)
}

private fun AnnotatedString.Builder.applyGenericHighlighting(code: String) {
    // 通用高亮：字符串、注释、数字
    highlightStrings(code)
    highlightComments(code)
    highlightNumbers(code)
}

private fun AnnotatedString.Builder.applyHighlighting(code: String, keywords: List<String>, literals: List<String>) {
    highlightStrings(code)
    highlightComments(code)
    highlightNumbers(code)
    highlightKeywords(code, keywords)
    highlightLiterals(code, literals)
    highlightTypes(code)
    highlightFunctions(code)
    highlightAnnotations(code)
}

private fun AnnotatedString.Builder.highlightStrings(code: String) {
    val stringPattern = Regex("\"(\\\\.|[^\"])*\"|'([^']*?)'")
    stringPattern.findAll(code).forEach { match ->
        addStyle(SpanStyle(color = CodeColors.STRING), match.range.first, match.range.last + 1)
    }
}

private fun AnnotatedString.Builder.highlightComments(code: String) {
    val commentPattern = Regex("//.*|/\\*[\\s\\S]*?\\*/")
    commentPattern.findAll(code).forEach { match ->
        addStyle(SpanStyle(color = CodeColors.COMMENT), match.range.first, match.range.last + 1)
    }
}

private fun AnnotatedString.Builder.highlightNumbers(code: String) {
    val numberPattern = Regex("\\b\\d+(\\.\\d+)?([eE][+-]?\\d+)?\\b|\\b0[xX][0-9a-fA-F]+\\b")
    numberPattern.findAll(code).forEach { match ->
        addStyle(SpanStyle(color = CodeColors.NUMBER), match.range.first, match.range.last + 1)
    }
}

private fun AnnotatedString.Builder.highlightKeywords(code: String, keywords: List<String>) {
    keywords.forEach { keyword ->
        val pattern = "\\b$keyword\\b".toRegex()
        pattern.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = CodeColors.KEYWORD, fontWeight = FontWeight.Bold), match.range.first, match.range.last + 1)
        }
    }
}

private fun AnnotatedString.Builder.highlightLiterals(code: String, literals: List<String>) {
    literals.forEach { literal ->
        val pattern = "\\b$literal\\b".toRegex()
        pattern.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = CodeColors.LITERAL), match.range.first, match.range.last + 1)
        }
    }
}

private fun AnnotatedString.Builder.highlightTypes(code: String) {
    // 匹配以大写字母开头的类型名
    val typePattern = "\\b[A-Z][a-zA-Z0-9_]*\\b".toRegex()
    typePattern.findAll(code).forEach { match ->
        addStyle(SpanStyle(color = CodeColors.TYPE), match.range.first, match.range.last + 1)
    }
}

private fun AnnotatedString.Builder.highlightFunctions(code: String) {
    // 匹配函数调用 (函数名后面跟括号)
    val functionPattern = "\\b([a-z][a-zA-Z0-9_]*)\\s*(?=\\()".toRegex()
    functionPattern.findAll(code).forEach { match ->
        addStyle(SpanStyle(color = CodeColors.FUNCTION), match.range.first, match.range.last + 1)
    }
}

private fun AnnotatedString.Builder.highlightAnnotations(code: String) {
    // 匹配注解 (@开头的标识符)
    val annotationPattern = "@[a-zA-Z][a-zA-Z0-9_]*".toRegex()
    annotationPattern.findAll(code).forEach { match ->
        addStyle(SpanStyle(color = CodeColors.ATTRIBUTE), match.range.first, match.range.last + 1)
    }
    
    // 匹配预处理器指令
    val preprocessorPattern = "#\\s*[a-zA-Z][a-zA-Z0-9_]*".toRegex()
    preprocessorPattern.findAll(code).forEach { match ->
        addStyle(SpanStyle(color = CodeColors.PREPROCESSOR), match.range.first, match.range.last + 1)
    }
}

/**
 * 颜色常量 - 使用指定的颜色方案
 */
private object CodeColors {
    val KEYWORD = Color(0xFF7C0787)      // 紫色 (关键字)
    val LITERAL = Color(0xFF0033B3)      // 蓝色 (字面量)
    val TYPE = Color(0xFF00627A)         // 褐色 (类型)
    val STRING = Color(0xFF067D17)       // 绿色 (字符串)
    val COMMENT = Color(0xFF808080)      // 灰色 (注释)
    val NUMBER = Color(0xFF0033B3)       // 蓝色 (数字)
    val FUNCTION = Color(0xFF4682B4)     // 钢蓝色 (函数)
    val PREPROCESSOR = Color(0xFF8B008B) // 深洋红色 (预处理)
    val ATTRIBUTE = Color(0xFFFF8C00)    // 橙色 (属性/注解)
}