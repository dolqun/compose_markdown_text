package com.adev.compose.markdowtext.markdown

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adev.compose.markdowtext.markdown.elements.*

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
                    CodeBlockElement(element.code, element.language)
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