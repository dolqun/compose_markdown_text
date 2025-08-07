package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adev.compose.markdowtext.util.RtlUtil

@Composable
fun TextElement(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),

        text = text,
        textAlign = RtlUtil.getTextAlign(text)
    )
}

@Composable
fun BoldTextElement(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = TextStyle(fontWeight = FontWeight.Bold),
        textAlign = RtlUtil.getTextAlign(text)
    )
}

@Composable
fun ItalicTextElement(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),

        text = text,
        style = TextStyle(fontStyle = FontStyle.Italic),
        textAlign = RtlUtil.getTextAlign(text)
    )
}

@Composable
fun BoldItalicTextElement(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),

        text = text,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        ),
        textAlign = RtlUtil.getTextAlign(text)
    )
}

@Composable
fun InlineCodeElement(code: String) {
    val isRtl = RtlUtil.isRtlText(code)
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = Color(0xFFD9D9D9)
    ) {
        Text(
            text = code,
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp),
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            ),
            textAlign = if (isRtl) TextAlign.End else TextAlign.Start
        )
    }
}