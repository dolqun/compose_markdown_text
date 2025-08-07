package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adev.compose.markdowtext.markdown.MarkdownElement
import com.adev.compose.markdowtext.util.RtlUtil

@Composable
fun TableElement(table: MarkdownElement.Table) {
    val headerBg = Color(0xFFE3F2FD)
    val evenRowBg = Color(0xFFF5F5F5)
    val borderColor = Color(0xFFBDBDBD)

    Column(
        modifier = Modifier
            .padding(2.dp)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
    ) {
        // 表头
        TableRow(
            cells = table.headers,
            background = headerBg,
            textBold = true,
            borderColor = borderColor,
            isLastRow = false          // 表头不是最后一行，需要横线
        )

        // 数据行
        table.rows.forEachIndexed { index, row ->
            val isLast = index == table.rows.lastIndex
            TableRow(
                cells = row,
                background = if (index % 2 == 0) evenRowBg else Color.White,
                textBold = false,
                borderColor = borderColor,
                isLastRow = isLast
            )
        }
    }
}

@Composable
private fun TableRow(
    cells: List<String>,
    background: Color,
    textBold: Boolean,
    borderColor: Color,
    isLastRow: Boolean          // true = 最后一行，不画横线
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(background)
    ) {
        cells.forEachIndexed { colIndex, cell ->
            val isLastCol = colIndex == cells.lastIndex

            // 单元格文字
            Text(
                text = cell,
                fontWeight = if (textBold) FontWeight.Bold else FontWeight.Normal,
                textAlign = RtlUtil.getTextAlign(cell, TextAlign.Center),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp, horizontal = 4.dp)
            )

            // 竖线：不是最后一列才画
            if (!isLastCol) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(borderColor)
                )
            }
        }
    }

    // 横线：不是最后一行才画
    if (!isLastRow) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(borderColor)
        )
    }
}