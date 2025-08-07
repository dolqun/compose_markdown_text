package com.adev.compose.markdowtext.util

import android.os.Build
import androidx.annotation.RequiresApi
import android.util.Log
import kotlin.code
import kotlin.collections.all
import kotlin.collections.any
import kotlin.collections.sortedByDescending
import kotlin.text.count
import kotlin.text.isEmpty

enum class Lang {
    EN, ZH, UG, KK, AZ, MIXED, UNKNOWN
}

object DetectLang {

    private const val TAG = "DetectLang"
    
    // 维吾尔文的常见字符集
    private val UYGHUR_CHAR_RANGES = listOf(
        '\u0621'..'\u064A',  // 基本阿拉伯字母
        '\u0674'..'\u06D3',  // 扩展阿拉伯字母
        '\u06D5'..'\u06ED',  // 维吾尔文使用的特殊符号
        '\uFB50'..'\uFDFF',  // 阿拉伯表现形式A
        '\uFE70'..'\uFEFF'   // 阿拉伯表现形式B
    )
    
    // 维吾尔文中特有的字符
    private val UYGHUR_SPECIFIC_CHARS = setOf(
        '\u0626', // ARABIC LETTER YEH WITH HAMZA ABOVE
        '\u0686', // ARABIC LETTER TCHEH
        '\u06AD', // ARABIC LETTER KAF WITH RING
        '\u06BE', // ARABIC LETTER HEH DOACHASHMEE
        '\u06C7', // ARABIC LETTER U
        '\u06CB', // ARABIC LETTER VE
        '\u06D0', // ARABIC LETTER E
        '\u06D5'  // ARABIC LETTER AE
    )
    
    // 哈萨克文中特有的字符
    private val KAZAKH_SPECIFIC_CHARS = setOf(
        '\u04B1', // CYRILLIC SMALL LETTER STRAIGHT U WITH STROKE
        '\u049B', // CYRILLIC SMALL LETTER KA WITH DESCENDER
        '\u0493', // CYRILLIC SMALL LETTER GHE WITH STROKE
        '\u04A3', // CYRILLIC SMALL LETTER EN WITH DESCENDER
        '\u04E9', // CYRILLIC SMALL LETTER BARRED O
        '\u04AB'  // CYRILLIC SMALL LETTER ES WITH DESCENDER
    )
    
    // 阿塞拜疆文中特有的字符
    private val AZERBAIJANI_SPECIFIC_CHARS = setOf(
        '\u0259', // LATIN SMALL LETTER SCHWA
        '\u0131', // LATIN SMALL LETTER DOTLESS I
        '\u018F', // LATIN CAPITAL LETTER SCHWA
        '\u011F', // LATIN SMALL LETTER G WITH BREVE
        '\u015F', // LATIN SMALL LETTER S WITH CEDILLA
        '\u00E7'  // LATIN SMALL LETTER C WITH CEDILLA
    )

    /**
     * 检查文本是否包含维吾尔文字符
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun containsUyghur(text: String): Boolean {
        if (text.isEmpty()) return false
        
        // 统计维吾尔文特有字符的数量
        val uyghurSpecificCount = text.count { char -> 
            UYGHUR_SPECIFIC_CHARS.contains(char)
        }
        
        // 如果包含明显的维吾尔文特有字符，直接返回true
        if (uyghurSpecificCount > 0) {
            Log.d(TAG, "Found Uyghur specific characters: $uyghurSpecificCount")
            return true
        }
        
        // 统计所有可能的维吾尔文字符
        val uyghurCharsCount = text.count { char ->
            UYGHUR_CHAR_RANGES.any { range -> char in range }
        }
        
        // 如果超过30%的字符是维吾尔文字符，认为是维吾尔文
        val threshold = 0.3
        val result = uyghurCharsCount.toDouble() / text.length > threshold
        
        if (result) {
            Log.d(TAG, "Text contains Uyghur characters: $uyghurCharsCount/${text.length}")
        }
        
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun detectLanguage(text: String): Lang {
        if (text.isEmpty()) return Lang.UNKNOWN
        
        var enCount = 0
        var zhCount = 0
        var ugCount = 0
        var kkCount = 0
        var azCount = 0
        var otherCount = 0

        for (c in text) {
            when {
                c.isCJK() -> zhCount++
                c.isArabicScript() -> {
                    // 进一步区分是否为维吾尔文特有字符
                    if (UYGHUR_SPECIFIC_CHARS.contains(c)) {
                        ugCount += 2  // 为维吾尔文特有字符增加权重
                    } else {
                        ugCount++
                    }
                }
                c.isEnglishLetter() -> enCount++
                c.isKazakhChar() -> kkCount++
                c.isAzerbaijaniChar() -> azCount++
                else -> otherCount++
            }
        }

        // 记录调试信息
        Log.d(TAG, "Language detection: EN=$enCount, ZH=$zhCount, UG=$ugCount, KK=$kkCount, AZ=$azCount, Other=$otherCount")

        if (listOf(enCount, zhCount, ugCount, kkCount, azCount).all { it == 0 }) {
            return Lang.UNKNOWN
        }

        val total = enCount + zhCount + ugCount + kkCount + azCount
        if (total == 0) return Lang.UNKNOWN

        val enRatio = enCount.toDouble() / total
        val zhRatio = zhCount.toDouble() / total
        val ugRatio = ugCount.toDouble() / total
        val kkRatio = kkCount.toDouble() / total
        val azRatio = azCount.toDouble() / total

        val thresholds = listOf(
            Pair(enRatio, Lang.EN),
            Pair(zhRatio, Lang.ZH),
            Pair(ugRatio, Lang.UG),
            Pair(kkRatio, Lang.KK),
            Pair(azRatio, Lang.AZ)
        ).sortedByDescending { it.first }
        
        // 如果最高比例超过75%，则判定为该语言
        if (thresholds[0].first > 0.75) {
            return thresholds[0].second
        }
        
        // 如果最高比例超过60%，且没有其他比例超过20%，则判定为该语言
        if (thresholds[0].first > 0.6 && (thresholds.size == 1 || thresholds[1].first < 0.2)) {
            return thresholds[0].second
        }

        // 对于维吾尔文，降低阈值，因为维吾尔文经常混合阿拉伯数字和少量拉丁字母
        if (thresholds[0].second == Lang.UG && thresholds[0].first > 0.5) {
            return Lang.UG
        }

        // 对于中文，提高阈值，因为中文字符的权重更大
        if (thresholds[0].second == Lang.ZH && thresholds[0].first > 0.65) {
            return Lang.ZH
        }

        return Lang.MIXED
    }

    private fun Char.isEnglishLetter(): Boolean {
        return this in 'A'..'Z' || this in 'a'..'z'
    }

    private fun Char.isCJK(): Boolean {
        val block = Character.UnicodeBlock.of(this)
        return block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                block == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
                block == Character.UnicodeBlock.CJK_COMPATIBILITY ||
                block == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS ||
                block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
                block == Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT
    }
    
    private fun Char.isKazakhChar(): Boolean {
        return this in KAZAKH_SPECIFIC_CHARS || 
               (this.code >= 0x0410 && this.code <= 0x044F) // 基本西里尔字母
    }
    
    private fun Char.isAzerbaijaniChar(): Boolean {
        return this in AZERBAIJANI_SPECIFIC_CHARS
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun Char.isArabicScript(): Boolean {
        val block = Character.UnicodeBlock.of(this)
        return block == Character.UnicodeBlock.ARABIC ||
                block == Character.UnicodeBlock.ARABIC_EXTENDED_A ||
                block == Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_A ||
                block == Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_B ||
                // 维吾尔文使用阿拉伯字符的扩展
                // Character.UnicodeBlock.ARABIC_EXTENDED_B is not available in all Android versions
                // Check if the character is in the Arabic Extended-B range: U+0870 to U+089F
                (this.code >= 0x0870 && this.code <= 0x089F) ||
                // 包含维吾尔文常用的组合标记
                block == Character.UnicodeBlock.ARABIC_MATHEMATICAL_ALPHABETIC_SYMBOLS ||
                // 检查Uyghur专用的Unicode范围
                // 维吾尔文使用的一些特殊字符在这些范围内
                UYGHUR_CHAR_RANGES.any { range -> this in range }
    }
}