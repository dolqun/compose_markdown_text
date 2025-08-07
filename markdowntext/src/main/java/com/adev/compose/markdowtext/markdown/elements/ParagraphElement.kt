package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.adev.compose.markdowtext.markdown.MarkdownElement
import com.adev.compose.markdowtext.markdown.TextSpan

@Composable
fun ParagraphElement(
    paragraph: MarkdownElement.Paragraph,
    onLinkClick: (String) -> Unit,
    imageContent: @Composable (String) -> Unit
) {
    val annotatedString = buildAnnotatedString {
        for (span in paragraph.spans) {
            when (span) {
                is TextSpan.Text -> append(span.text)
                is TextSpan.Bold -> {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(span.text)
                    pop()
                }
                is TextSpan.Italic -> {
                    pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    append(span.text)
                    pop()
                }
                is TextSpan.BoldItalic -> {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic))
                    append(span.text)
                    pop()
                }
                is TextSpan.InlineCode -> {
                    pushStyle(SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = Color.LightGray
                    ))
                    append(span.code)
                    pop()
                }
                is TextSpan.Link -> {
                    pushStringAnnotation(tag = "URL", annotation = span.url)
                    pushStyle(SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    ))
                    append(span.text)
                    pop()
                    pop()
                }
                is TextSpan.Image -> append(span.alt)
            }
        }
    }
    
    Text(
        text = annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val urlAnnotations = annotatedString.getStringAnnotations("URL", 0, annotatedString.length)
                if (urlAnnotations.isNotEmpty()) {
                    onLinkClick(urlAnnotations[0].item)
                }
            }
    )
    
    // Render images
    for (span in paragraph.spans) {
        if (span is TextSpan.Image) {
            DefaultImageContent(span.url)
        }
    }
}

@Composable
private fun DefaultImageContent(url: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )
    }
}