package com.adev.compose.markdowtext.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * 剪贴板工具类
 */
object ClipboardUtil {
    fun copyToClipboard(context: Context, text: String, message: String = "代码已复制到剪贴板") {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("code", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

/**
 * 获取剪贴板复制函数
 */
@Composable
fun getClipboardCopyFunction(): (String) -> Unit {
    val context = LocalContext.current
    return { text ->
        ClipboardUtil.copyToClipboard(context, text)
    }
}