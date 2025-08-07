package com.adev.compose.markdowtext.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    codeContent: @Composable (String, String) -> Unit = { _, _ -> },
    imageContent: @Composable (String) -> Unit = {},
    onImageClick: (String) -> Unit = {},
    onClick: (String) -> Unit = {},
    onLongClick: (String) -> Unit = {},
    onLinkClick: (String) -> Unit = {},
) {
    val parser = MarkdownParser()
    val elements = parser.parse(markdown)
    
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        elements.forEachIndexed { index, element ->
            when (element) {
                is MarkdownElement.Header -> {
                    HeaderElement(element)
                }
                is MarkdownElement.Paragraph -> {
                    ParagraphElement(element, onLinkClick, imageContent)
                }
                is MarkdownElement.CodeBlock -> {
                    DefaultCodeContent(element.code)
                }
                is MarkdownElement.UnorderedList -> {
                    UnorderedListElement(element)
                }
                is MarkdownElement.OrderedList -> {
                    OrderedListElement(element)
                }
                is MarkdownElement.Blockquote -> {
                    BlockquoteElement(element)
                }
                is MarkdownElement.HorizontalRule -> {
                    HorizontalRuleElement()
                }
                is MarkdownElement.Table -> {
                    TableElement(element)
                }
            }
        }
    }
}

@Composable
private fun HeaderElement(header: MarkdownElement.Header) {
    val fontSize = when (header.level) {
        1 -> 28.sp
        2 -> 24.sp
        3 -> 20.sp
        4 -> 18.sp
        5 -> 16.sp
        else -> 14.sp
    }
    
    val fontWeight = when (header.level) {
        1, 2 -> FontWeight.Bold
        3, 4 -> FontWeight.SemiBold
        else -> FontWeight.Medium
    }
    
    Text(
        text = header.text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@Composable
private fun ParagraphElement(
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
private fun UnorderedListElement(list: MarkdownElement.UnorderedList) {
    Column(modifier = Modifier.fillMaxWidth()) {
        for (spans in list.items) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "• ",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = buildSimpleSpanText(spans),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun OrderedListElement(list: MarkdownElement.OrderedList) {
    Column(modifier = Modifier.fillMaxWidth()) {
        for (i in list.items.indices) {
            val spans = list.items[i]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "${i + 1}. ",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = buildSimpleSpanText(spans),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BlockquoteElement(blockquote: MarkdownElement.Blockquote) {
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

@Composable
private fun HorizontalRuleElement() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        thickness = 1.dp,
        color = Color.Gray
    )
}

@Composable
private fun TableElement(table: MarkdownElement.Table) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        // Header row
        Row(modifier = Modifier.fillMaxWidth()) {
            for (header in table.headers) {
                Text(
                    text = header,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        HorizontalDivider(color = Color.Gray)
        
        // Data rows
        for (row in table.rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (cell in row) {
                    Text(
                        text = cell,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private fun buildSimpleSpanText(spans: List<TextSpan>): String {
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

@Composable
private fun DefaultCodeContent(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Text(
            text = code,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
private fun DefaultImageContent(url: String) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    )
}