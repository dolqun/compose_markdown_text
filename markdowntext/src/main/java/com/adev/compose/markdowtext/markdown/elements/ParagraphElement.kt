package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adev.compose.markdowtext.markdown.MarkdownElement
import com.adev.compose.markdowtext.markdown.TextSpan
import com.adev.compose.markdowtext.util.RtlUtil

@Composable
fun ParagraphElement(
    paragraph: MarkdownElement.Paragraph,
    onLinkClick: (String) -> Unit,
    imageContent: @Composable (String) -> Unit
) {
    val textContent = paragraph.spans.joinToString("") { span ->
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

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (span in paragraph.spans) {
            when (span) {
                is TextSpan.Text -> TextElement(span.text)
                is TextSpan.Bold -> BoldTextElement(span.text)
                is TextSpan.Italic -> ItalicTextElement(span.text)
                is TextSpan.BoldItalic -> BoldItalicTextElement(span.text)
                is TextSpan.InlineCode -> InlineCodeElement(span.code)
                is TextSpan.Link -> LinkElement(text = span.text, url = span.url, onLinkClick = onLinkClick)
                is TextSpan.Image -> imageContent.invoke(span.url)
            }
        }
    }
}