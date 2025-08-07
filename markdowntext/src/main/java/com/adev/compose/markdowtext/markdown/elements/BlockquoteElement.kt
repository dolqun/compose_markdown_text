package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adev.compose.markdowtext.markdown.MarkdownElement
import com.adev.compose.markdowtext.markdown.TextSpan

@Composable
fun BlockquoteElement(blockquote: MarkdownElement.Blockquote) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(100.dp)
                .background(Color.Blue)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            for (element in blockquote.elements) {
                when (element) {
                    is MarkdownElement.Header -> HeaderElement(element)
                    is MarkdownElement.Paragraph -> {
                        val text = element.spans.joinToString("") { span ->
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
                        Text(text = text)
                    }
                    else -> {}
                }
            }
        }
    }
}