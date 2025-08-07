package com.adev.compose.markdowtext.util

import androidx.compose.ui.text.style.TextAlign

object RtlUtil {
    /**
     * 检测文本是否包含RTL（从右到左）语言字符
     * 
     * 支持的RTL语言范围：
     * - 阿拉伯语：\u0600-\u06FF
     * - 阿拉伯语扩展：\u0750-\u077F  
     * - 希伯来语：\u0590-\u05FF
     * - 维吾尔语、哈萨克语等使用阿拉伯字母的语言
     */
    fun isRtlText(text: String): Boolean {
        if (text.isEmpty()) return false
        
        return text.any { char ->
            char in '\u0590'..'\u05FF' ||  // 希伯来语
            char in '\u0600'..'\u06FF' ||  // 阿拉伯语基本范围
            char in '\u0750'..'\u077F' ||  // 阿拉伯语扩展
            char in '\u08A0'..'\u08FF' ||  // 阿拉伯语扩展A
            char in '\uFB1D'..'\uFB4F' ||  // 希伯来字母表现形式
            char in '\uFB50'..'\uFDFF' ||  // 阿拉伯字母表现形式A
            char in '\uFE70'..'\uFEFF'     // 阿拉伯字母表现形式B
        }
    }
    
    /**
     * 获取文本对齐方式
     * @param text 输入文本
     * @param defaultAlign 默认对齐方式（LTR语言使用）
     * @return 根据文本语言返回适当的对齐方式
     */
    fun getTextAlign(text: String, defaultAlign: TextAlign = TextAlign.Start): TextAlign {
        return if (isRtlText(text)) TextAlign.End else defaultAlign
    }
}