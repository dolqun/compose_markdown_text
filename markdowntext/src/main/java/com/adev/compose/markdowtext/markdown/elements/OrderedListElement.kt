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
import androidx.compose.ui.text.style.TextAlign
import com.adev.compose.markdowtext.markdown.MarkdownElement
import com.adev.compose.markdowtext.util.buildSimpleSpanText
import com.adev.compose.markdowtext.util.RtlUtil

@Composable
fun OrderedListElement(list: MarkdownElement.OrderedList) {
    Column(modifier = Modifier.fillMaxWidth()) {
        for (i in list.items.indices) {
            val spans = list.items[i]
            val text = buildSimpleSpanText(spans)
            val isRtl = RtlUtil.isRtlText(text)
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                if (isRtl) {
                    Text(
                        text = text,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "${i + 1}. ",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                } else {
                    Text(
                        text = "${i + 1}. ",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = text,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}