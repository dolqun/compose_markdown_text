package com.adev.compose.markdowtext.markdown.util

import com.adev.compose.markdowtext.markdown.TextSpan

fun buildSimpleSpanText(spans: List<TextSpan>): String {
    return spans.joinToString("") { span ->
        when (span) {
            is TextSpan.Text -> span.text
            is TextSpan.Bold -> span.text
            is TextSpan.Italic -> span.text
            is TextSpan.BoldItalic -> span.text
            is TextSpan.InlineCode -> span.code
            is TextSpan.Link -> span.text
            is TextSpan.Image -> span.alt
        }
    }
}