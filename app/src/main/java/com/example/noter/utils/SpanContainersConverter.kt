package com.example.noter.utils

import android.util.Log
import androidx.room.TypeConverter

private const val SPAN_TYPE_INDEX = 0
private const val SELECTION_START_INDEX = 1
private const val SELECTION_END_INDEX = 2
private const val BOLD_STRING = "bold"
private const val ITALIC_STRING = "italic"
private const val UNDERLINE_STRING = "underline"
private const val STRIKETHROUGH_STRING = "strikethrough"
private const val SPAN_CONTAINER_DELIMITER = " "
private const val LIST_CONVERTER_DELIMITER = ":"

class SpanContainersConverter {

    fun fromContainersToString(containers: List<SpanContainer>): List<String> {

        return if (containers.isNotEmpty()) {
            val stringContainers = mutableListOf<String>()
            for (container in containers) {
                val spanType = when(container.spanType) {
                    TextFormatterType.BOLD -> BOLD_STRING
                    TextFormatterType.ITALIC -> ITALIC_STRING
                    TextFormatterType.UNDERLINE -> UNDERLINE_STRING
                    TextFormatterType.STRIKETHROUGH -> STRIKETHROUGH_STRING
                }
                val containerAsString = "$spanType ${container.start} ${container.end}"
                stringContainers.add(containerAsString)
            }
            stringContainers.toList()
        } else emptyList()
    }


    fun fromStringToContainers(stringContainers: List<String>): List<SpanContainer> {

        // SpanContainer structure is: spanType selectionStart selectionEnd

        if (stringContainers.isNotEmpty()) {
            val spanContainers = mutableListOf<SpanContainer>()
            for (stringContainer in stringContainers) {
                val spanType = when(stringContainer.split(SPAN_CONTAINER_DELIMITER)[SPAN_TYPE_INDEX]) {
                    BOLD_STRING -> TextFormatterType.BOLD
                    ITALIC_STRING -> TextFormatterType.ITALIC
                    UNDERLINE_STRING -> TextFormatterType.UNDERLINE
                    STRIKETHROUGH_STRING -> TextFormatterType.STRIKETHROUGH
                    else -> TextFormatterType.BOLD
                }

                try {
                    val start = stringContainer.split(SPAN_CONTAINER_DELIMITER)[SELECTION_START_INDEX]
                    val end = stringContainer.split(SPAN_CONTAINER_DELIMITER)[SELECTION_END_INDEX]
                    val normalContainer = SpanContainer(spanType, start.toInt(), end.toInt())
                    spanContainers.add(normalContainer)
                } catch (e: IndexOutOfBoundsException) {
                    Log.d("taggg", "out of bounds! list = $stringContainers")
                }
            }
            return spanContainers
        } else return emptyList()
    }
}

class ListConverter {

    @TypeConverter
    fun toStr(list: List<String>): String =
        list.joinToString(LIST_CONVERTER_DELIMITER)

    @TypeConverter
    //"$spanType ${container.start} ${container.end}"
    fun toList(plainString: String): List<String> {
        val mutableList = mutableListOf<String>()
        for (i in plainString.split(LIST_CONVERTER_DELIMITER)) {
            mutableList.add(i)
        }
        return mutableList.toList()
    }
}