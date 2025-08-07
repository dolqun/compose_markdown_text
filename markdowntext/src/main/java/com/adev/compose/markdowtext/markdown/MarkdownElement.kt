package com.adev.compose.markdowtext.markdown

sealed class MarkdownElement {
    data class Header(val level: Int, val text: String) : MarkdownElement()
    data class Paragraph(val spans: List<TextSpan>) : MarkdownElement()
    data class UnorderedList(val items: List<List<TextSpan>>) : MarkdownElement()
    data class OrderedList(val items: List<List<TextSpan>>) : MarkdownElement()
    data class CodeBlock(val code: String, val language: String? = null) : MarkdownElement()
    data class Blockquote(val elements: List<MarkdownElement>) : MarkdownElement()
    object HorizontalRule : MarkdownElement()
    data class Table(
        val headers: List<String>,
        val rows: List<List<String>>,
        val alignments: List<TableAlignment> = emptyList()
    ) : MarkdownElement()
}

sealed class TextSpan {
    data class Text(val text: String) : TextSpan()
    data class Bold(val text: String) : TextSpan()
    data class Italic(val text: String) : TextSpan()
    data class BoldItalic(val text: String) : TextSpan()
    data class InlineCode(val code: String) : TextSpan()
    data class Link(val text: String, val url: String) : TextSpan()
    data class Image(val alt: String, val url: String) : TextSpan()
}

enum class TableAlignment {
    LEFT, CENTER, RIGHT
}