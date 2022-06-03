package com.example.noter.utils

import android.content.res.Resources
import com.example.noter.R
import java.util.Calendar

class Calendar {

    private val calendar = Calendar.getInstance()

    private fun getCurrentDay(): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun getCurrentMonthAsID(): Int {
        return monthNumToRes(calendar.get(Calendar.MONTH))
    }

    private fun monthNumToRes(monthNum: Int) : Int {
        return  when (monthNum) {
            0 -> R.string.january
            1 -> R.string.february
            2 -> R.string.march
            3 -> R.string.april
            4 -> R.string.may
            5 -> R.string.june
            6 -> R.string.july
            7 -> R.string.august
            8 -> R.string.september
            9 -> R.string.october
            10 -> R.string.november
            11 -> R.string.december
            else -> 0
        }
    }

    fun getCurrentDate(resources: Resources): String {
        val day = getCurrentDay()
        val month = resources.getString(getCurrentMonthAsID())
        return "$day $month"
    }
}