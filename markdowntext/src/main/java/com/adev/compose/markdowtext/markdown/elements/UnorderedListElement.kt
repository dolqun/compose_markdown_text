package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adev.compose.markdowtext.markdown.MarkdownElement
import com.adev.compose.markdowtext.markdown.util.buildSimpleSpanText

@Composable
fun UnorderedListElement(list: MarkdownElement.UnorderedList) {
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