package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.adev.compose.markdowtext.markdown.MarkdownElement
import com.adev.compose.markdowtext.util.RtlUtil

@Composable
fun HeaderElement(header: MarkdownElement.Header) {
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
            .padding(vertical = 4.dp),
        textAlign = RtlUtil.getTextAlign(header.text)
    )
}