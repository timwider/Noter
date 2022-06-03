package com.example.noter.utils

import android.util.Log
import androidx.room.TypeConverter

class SpanContainersConverter {


    fun fromContainersToString(containers: List<SpanContainer>): List<String> {

        if (containers.isNotEmpty()) {
            val stringContainers = mutableListOf<String>()
            for (container in containers) {
                val spanType = when(container.spanType) {
                    TextFormatterType.BOLD -> "bold"
                    TextFormatterType.ITALIC -> "italic"
                    TextFormatterType.UNDERLINED -> "underlined"
                    TextFormatterType.STRIKETHROUGH -> "strikethrough"
                }
                val containerAsString = "$spanType ${container.start} ${container.end}"
                stringContainers.add(containerAsString)
            }
            return stringContainers.toList()
        } else return emptyList()
    }


    fun fromStringToContainers(stringContainers: List<String>): List<SpanContainer> {

        if (stringContainers.isNotEmpty()) {
            val spanContainers = mutableListOf<SpanContainer>()

            for (stringContainer in stringContainers) {
                // spanType is the first element there
                val spanType = when(stringContainer.split(" ")[0]) {
                    "bold" -> TextFormatterType.BOLD
                    "italic" -> TextFormatterType.ITALIC
                    "underlined" -> TextFormatterType.UNDERLINED
                    "strikethrough" -> TextFormatterType.STRIKETHROUGH
                    else -> TextFormatterType.BOLD
                }

                try {
                    val start = stringContainer.split(" ")[1]
                    val end = stringContainer.split(" ")[2]
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
        list.joinToString(":")

    @TypeConverter
    //"$spanType ${container.start} ${container.end}"
    fun toList(plainString: String): List<String> {
        val mutableList = mutableListOf<String>()
        for (i in plainString.split(":")) {
            mutableList.add(i)
        }
        return mutableList.toList()
    }
}