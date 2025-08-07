package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adev.compose.markdowtext.markdown.MarkdownElement
import com.adev.compose.markdowtext.markdown.TextSpan

@Composable
fun BlockquoteElement(blockquote: MarkdownElement.Blockquote) {
    val textContent = blockquote.elements.joinToString(" ") { element ->
        when (element) {
            is MarkdownElement.Header -> element.text
            is MarkdownElement.Paragraph -> element.spans.joinToString("") { span ->
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
            else -> ""
        }
    }
    val isRtl = com.adev.compose.markdowtext.util.RtlUtil.isRtlText(textContent)
    
    Row(modifier = Modifier.fillMaxWidth()) {
        if (!isRtl) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .background(Color(0xFFD4D4D4), RoundedCornerShape(4.dp))
            )
        }
        Column(
            modifier = Modifier
                .padding(start = if (isRtl) 0.dp else 16.dp, end = if (isRtl) 16.dp else 0.dp)
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
                        Text(
                            text = text,
                            textAlign = if (isRtl) TextAlign.End else TextAlign.Start
                        )
                    }
                    else -> {}
                }
            }
        }
        if (isRtl) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(100.dp)
                    .background(Color(0xFF0094E3), RoundedCornerShape(4.dp))
            )
        }
    }
}