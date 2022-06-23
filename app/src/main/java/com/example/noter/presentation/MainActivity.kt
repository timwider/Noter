package com.example.noter.presentation


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.example.noter.R
import com.example.noter.presentation.view.HolderFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window?.statusBarColor = ResourcesCompat.getColor(resources, R.color.white, null)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, HolderFragment())
            .commit()
    }
}