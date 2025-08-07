package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.runtime.Composable
import com.adev.compose.markdowtext.util.getClipboardCopyFunction

/**
 * 代码块组件 - 使用优化的MarkdownCodeBlock
 */
@Composable
fun CodeBlockElement(code: String, language: String? = null,isShowLineNumbers: Boolean = false) {
    val lang = language ?: "text"
    val copyFunction = getClipboardCopyFunction()
    
    MarkdownCodeBlock(
        code = code,
        language = lang,
        showLineNumbers = isShowLineNumbers,
        onCopyClick = copyFunction
    )
}