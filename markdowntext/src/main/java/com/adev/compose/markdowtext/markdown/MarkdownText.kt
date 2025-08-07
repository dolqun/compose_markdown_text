package com.adev.compose.markdowtext.markdown

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adev.compose.markdowtext.markdown.elements.*
import com.adev.compose.markdowtext.markdown.elements.ImageElement

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    isShowLineNumbers: Boolean = false,
    codeContent: @Composable (String, String) -> Unit = { code, language ->
        CodeBlockElement(code, language, isShowLineNumbers = isShowLineNumbers)
    },
    imageContent: @Composable (String) -> Unit = { url ->
        ImageElement(url, onClick = {})
    },
    onClick: (String) -> Unit = {},
    onLongClick: (String) -> Unit = {},
    onLinkClick: (String) -> Unit = {},
) {
    val parser = MarkdownParser()
    val elements = parser.parse(markdown)

    Column(
        modifier = modifier
            .combinedClickable(
                onClick = {
                    onClick.invoke(markdown)
                },
                onLongClick = {
                    onLongClick.invoke(markdown)
                }
            ),
        verticalArrangement = Arrangement
            .spacedBy(4.dp)
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
                    codeContent(element.code, element.language ?: "")
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


