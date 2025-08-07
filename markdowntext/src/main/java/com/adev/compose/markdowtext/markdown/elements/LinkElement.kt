package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.adev.compose.markdowtext.util.RtlUtil

@Composable
fun LinkElement(text: String, url: String, onLinkClick: (String) -> Unit) {
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = url)
        withStyle(style = SpanStyle(
            color = Color(0xFF0391FC),
            textDecoration = TextDecoration.Underline
        )) {
            append(text)
        }
        pop()
    }
    
    Text(
        text = annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onLinkClick(url)
            },
        textAlign = RtlUtil.getTextAlign(text)
    )
}