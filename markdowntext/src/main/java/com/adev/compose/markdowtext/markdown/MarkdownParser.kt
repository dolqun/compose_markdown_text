package com.adev.compose.markdowtext.markdown

class MarkdownParser {
    
    fun parse(markdown: String): List<MarkdownElement> {
        val lines = markdown.lines()
        val elements = mutableListOf<MarkdownElement>()
        var i = 0
        
        while (i < lines.size) {
            val line = lines[i]
            
            when {
                // Code block
                line.trimStart().startsWith("```") -> {
                    val (codeBlock, nextIndex) = parseCodeBlock(lines, i)
                    elements.add(codeBlock)
                    i = nextIndex
                }
                // Headers
                line.startsWith("#") -> {
                    elements.add(parseHeader(line))
                    i++
                }
                // Horizontal rule
                line.trim().matches(Regex("^-{3,}$")) -> {
                    elements.add(MarkdownElement.HorizontalRule)
                    i++
                }
                // Blockquote
                line.trimStart().startsWith(">") -> {
                    val (blockquote, nextIndex) = parseBlockquote(lines, i)
                    elements.add(blockquote)
                    i = nextIndex
                }
                // Unordered list
                line.trimStart().matches(Regex("^[-*+]\\s+.*")) -> {
                    val (list, nextIndex) = parseUnorderedList(lines, i)
                    elements.add(list)
                    i = nextIndex
                }
                // Ordered list
                line.trimStart().matches(Regex("^\\d+\\.\\s+.*")) -> {
                    val (list, nextIndex) = parseOrderedList(lines, i)
                    elements.add(list)
                    i = nextIndex
                }
                // Table
                line.contains("|") && i + 1 < lines.size && lines[i + 1].contains("|") && lines[i + 1].contains("-") -> {
                    val (table, nextIndex) = parseTable(lines, i)
                    elements.add(table)
                    i = nextIndex
                }
                // Empty line
                line.trim().isEmpty() -> {
                    i++
                }
                // Regular paragraph
                else -> {
                    val (paragraph, nextIndex) = parseParagraph(lines, i)
                    elements.add(paragraph)
                    i = nextIndex
                }
            }
        }
        
        return elements
    }
    
    private fun parseHeader(line: String): MarkdownElement.Header {
        val level = line.takeWhile { it == '#' }.length.coerceAtMost(6)
        val text = line.drop(level).trim()
        return MarkdownElement.Header(level, text)
    }
    
    private fun parseCodeBlock(lines: List<String>, startIndex: Int): Pair<MarkdownElement.CodeBlock, Int> {
        val startLine = lines[startIndex].trimStart()
        val language = startLine.drop(3).trim().takeIf { it.isNotEmpty() }
        val codeLines = mutableListOf<String>()
        
        var i = startIndex + 1
        while (i < lines.size && !lines[i].trimStart().startsWith("```")) {
            codeLines.add(lines[i])
            i++
        }
        
        val code = codeLines.joinToString("\n")
        return MarkdownElement.CodeBlock(code, language) to (i + 1)
    }
    
    private fun parseBlockquote(lines: List<String>, startIndex: Int): Pair<MarkdownElement.Blockquote, Int> {
        val blockquoteLines = mutableListOf<String>()
        var i = startIndex
        
        while (i < lines.size && lines[i].trimStart().startsWith(">")) {
            val line = lines[i].trimStart().drop(1).trim()
            blockquoteLines.add(line)
            i++
        }
        
        val blockquoteContent = blockquoteLines.joinToString("\n")
        val elements = parse(blockquoteContent)
        return MarkdownElement.Blockquote(elements) to i
    }
    
    private fun parseUnorderedList(lines: List<String>, startIndex: Int): Pair<MarkdownElement.UnorderedList, Int> {
        val items = mutableListOf<List<TextSpan>>()
        var i = startIndex
        
        while (i < lines.size && lines[i].trimStart().matches(Regex("^[-*+]\\s+.*"))) {
            val line = lines[i].trimStart().drop(2)
            items.add(parseTextSpans(line))
            i++
        }
        
        return MarkdownElement.UnorderedList(items) to i
    }
    
    private fun parseOrderedList(lines: List<String>, startIndex: Int): Pair<MarkdownElement.OrderedList, Int> {
        val items = mutableListOf<List<TextSpan>>()
        var i = startIndex
        
        while (i < lines.size && lines[i].trimStart().matches(Regex("^\\d+\\.\\s+.*"))) {
            val line = lines[i].trimStart().dropWhile { it.isDigit() }.drop(1).trim()
            items.add(parseTextSpans(line))
            i++
        }
        
        return MarkdownElement.OrderedList(items) to i
    }
    
    private fun parseTable(lines: List<String>, startIndex: Int): Pair<MarkdownElement.Table, Int> {
        val headerLine = lines[startIndex]
        val separatorLine = lines[startIndex + 1]
        
        val headers = headerLine.split("|").map { it.trim() }.filter { it.isNotEmpty() }
        val alignments = separatorLine.split("|").map { alignment ->
            val trimmed = alignment.trim()
            when {
                trimmed.startsWith(":") && trimmed.endsWith(":") -> TableAlignment.CENTER
                trimmed.endsWith(":") -> TableAlignment.RIGHT
                else -> TableAlignment.LEFT
            }
        }.filter { true }
        
        val rows = mutableListOf<List<String>>()
        var i = startIndex + 2
        
        while (i < lines.size && lines[i].contains("|")) {
            val row = lines[i].split("|").map { it.trim() }.filter { it.isNotEmpty() }
            if (row.isNotEmpty()) {
                rows.add(row)
            }
            i++
        }
        
        return MarkdownElement.Table(headers, rows, alignments) to i
    }
    
    private fun parseParagraph(lines: List<String>, startIndex: Int): Pair<MarkdownElement.Paragraph, Int> {
        val paragraphLines = mutableListOf<String>()
        var i = startIndex
        
        while (i < lines.size && lines[i].trim().isNotEmpty() && 
               !lines[i].startsWith("#") && 
               !lines[i].trimStart().startsWith(">") &&
               !lines[i].trimStart().matches(Regex("^[-*+]\\s+.*")) &&
               !lines[i].trimStart().matches(Regex("^\\d+\\.\\s+.*")) &&
               !lines[i].trimStart().startsWith("```") &&
               !lines[i].trim().matches(Regex("^-{3,}$"))) {
            paragraphLines.add(lines[i])
            i++
        }
        
        val text = paragraphLines.joinToString(" ").trim()
        val spans = parseTextSpans(text)
        return MarkdownElement.Paragraph(spans) to i
    }
    
    fun parseTextSpans(text: String): List<TextSpan> {
        val spans = mutableListOf<TextSpan>()
        var currentText = text
        
        // Process in order: images, links, bold italic, bold, italic, inline code
        while (currentText.isNotEmpty()) {
            val imageMatch = Regex("!\\[([^\\]]*)\\]\\(([^)]+)\\)").find(currentText)
            val linkMatch = Regex("\\[([^\\]]*)\\]\\(([^)]+)\\)").find(currentText)
            val boldItalicMatch = Regex("\\*\\*\\*([^*]+)\\*\\*\\*").find(currentText)
            val boldMatch = Regex("\\*\\*([^*]+)\\*\\*|__([^_]+)__").find(currentText)
            val italicMatch = Regex("\\*([^*]+)\\*|_([^_]+)_").find(currentText)
            val inlineCodeMatch = Regex("`([^`]+)`").find(currentText)
            
            val matches = listOfNotNull(
                imageMatch?.let { "image" to it },
                linkMatch?.let { "link" to it },
                boldItalicMatch?.let { "bolditalic" to it },
                boldMatch?.let { "bold" to it },
                italicMatch?.let { "italic" to it },
                inlineCodeMatch?.let { "code" to it }
            ).minByOrNull { it.second.range.first }
            
            if (matches != null) {
                val (type, match) = matches
                
                // Add text before match
                if (match.range.first > 0) {
                    val beforeText = currentText.substring(0, match.range.first)
                    if (beforeText.isNotEmpty()) {
                        spans.add(TextSpan.Text(beforeText))
                    }
                }
                
                // Add matched span
                when (type) {
                    "image" -> {
                        val alt = match.groupValues[1]
                        val url = match.groupValues[2]
                        spans.add(TextSpan.Image(alt, url))
                    }
                    "link" -> {
                        val text = match.groupValues[1]
                        val url = match.groupValues[2]
                        spans.add(TextSpan.Link(text, url))
                    }
                    "bolditalic" -> {
                        val text = match.groupValues[1]
                        spans.add(TextSpan.BoldItalic(text))
                    }
                    "bold" -> {
                        val text = match.groupValues[1].takeIf { it.isNotEmpty() } ?: match.groupValues[2]
                        spans.add(TextSpan.Bold(text))
                    }
                    "italic" -> {
                        val text = match.groupValues[1].takeIf { it.isNotEmpty() } ?: match.groupValues[2]
                        spans.add(TextSpan.Italic(text))
                    }
                    "code" -> {
                        val code = match.groupValues[1]
                        spans.add(TextSpan.InlineCode(code))
                    }
                }
                
                currentText = currentText.substring(match.range.last + 1)
            } else {
                // No more matches, add remaining text
                if (currentText.isNotEmpty()) {
                    spans.add(TextSpan.Text(currentText))
                }
                break
            }
        }
        
        return spans
    }
}