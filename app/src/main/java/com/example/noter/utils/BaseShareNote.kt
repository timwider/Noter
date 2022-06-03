package com.example.noter.utils

import android.content.Intent

interface BaseShareNote {

    fun constructIntent() : Intent
}