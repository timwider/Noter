package com.example.noter.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import com.example.noter.R
import java.lang.IndexOutOfBoundsException

interface BaseTextFormatter {

    fun formatText(
        text: Spannable,
        type: TextFormatterType,
        selectionStart: Int,
        selectionEnd: Int
    ) : SpannableStringBuilder

    fun grabAllSpans(text: Spannable) : List<SpanContainer>

    fun getSpecificSpans(
        text: Spannable,
        spanType: TextFormatterType,
        spans: Array<out CharacterStyle>,
        spanContainers: MutableList<SpanContainer>,
        // This is for StyleSpan spans. ID tells us whether it's bold or italic.
        areStyleSpans: Boolean
    )

    fun setSpansFromContainers(containers: List<SpanContainer>?, text: Spannable): SpannableStringBuilder

    fun setTypeFromId(id: Int): TextFormatterType
}

class TextFormatter: BaseTextFormatter {

    override fun formatText(
        text: Spannable,
        type: TextFormatterType,
        selectionStart: Int,
        selectionEnd: Int
    ): SpannableStringBuilder {

        val textStyle = resolveTextStyle(type)
        val spannableString = SpannableStringBuilder(text)
        spannableString.setSpan(textStyle, selectionStart, selectionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spannableString
    }

    override fun grabAllSpans(text: Spannable): List<SpanContainer> {
        val spanContainers = mutableListOf<SpanContainer>()
        val spanClasses = arrayOf(StyleSpan::class.java, UnderlineSpan::class.java, StrikethroughSpan::class.java)
        for (spanClass in spanClasses) {
            val spanType = TextFormatterType.valueOf(spanClass.simpleName)
            getSpecificSpans(
                text = text,
                spans = text.getSpans(0, text.length, spanClass),
                spanContainers = spanContainers,
                areStyleSpans = spanClass == StyleSpan::class.java,
                spanType = spanType
            )
        }
        return spanContainers.toSet().toList()
    }

    override fun getSpecificSpans(
        text: Spannable,
        spanType: TextFormatterType,
        spans: Array<out CharacterStyle>,
        spanContainers: MutableList<SpanContainer>,
        areStyleSpans: Boolean
    ) {
        if (spans.isNotEmpty()) {
            var innerSpanType = spanType
            for (style in spans) {
                if (areStyleSpans) innerSpanType = resolveStyleSpanType(style)
                val container = SpanContainer(innerSpanType, text.getSpanStart(style), text.getSpanEnd(style))
                spanContainers.add(container)
            }
        }
    }

    private fun resolveStyleSpanType(style: CharacterStyle): TextFormatterType {
        return if ( (style as StyleSpan).style == 1 ) TextFormatterType.BOLD else TextFormatterType.ITALIC
    }

    private fun resolveTextStyle(type: TextFormatterType) : CharacterStyle {
        return when (type) {
            TextFormatterType.BOLD -> StyleSpan(Typeface.BOLD)
            TextFormatterType.ITALIC -> StyleSpan(Typeface.ITALIC)
            TextFormatterType.UNDERLINED -> UnderlineSpan()
            TextFormatterType.STRIKETHROUGH -> StrikethroughSpan()
        }
    }

    override fun setSpansFromContainers(containers: List<SpanContainer>?, text: Spannable): SpannableStringBuilder {
        val spannableText = SpannableStringBuilder(text)
        containers?.let {
            for (container in containers) {
                val textStyle = resolveTextStyle(type = container.spanType)
                val spanEnd = if (container.start <= text.length) text.length else container.end
                spannableText.setSpan(textStyle, container.start, spanEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
        return spannableText
    }

    override fun setTypeFromId(id: Int): TextFormatterType {
        return when(id) {
            R.id.iv_bold -> TextFormatterType.BOLD
            R.id.iv_italic -> TextFormatterType.ITALIC
            R.id.iv_underlined -> TextFormatterType.UNDERLINED
            R.id.iv_strikethrough -> TextFormatterType.STRIKETHROUGH
            else -> TextFormatterType.BOLD
        }
    }
}

enum class TextFormatterType(val simpleName: String) {
    BOLD("StyleSpans"),
    ITALIC("StyleSpans"),
    UNDERLINED("UnderlineSpan"),
    STRIKETHROUGH("StrikeThroughSpan")
}