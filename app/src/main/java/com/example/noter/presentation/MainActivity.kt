package com.example.noter.presentation


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.noter.R
import com.example.noter.presentation.holder.HolderFragment
import com.example.noter.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window?.statusBarColor = ResourcesCompat.getColor(resources, R.color.white, null)

        if (savedInstanceState == null) {

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                // Replace whatever is in the fragment_container view with this fragment
                replace<HolderFragment>(R.id.main_fragment_container)
            }
        }
    }

    override fun onBackPressed() {
        if (homeViewModel.isSelectionModeEnabled()) {
            homeViewModel.disableSelectionMode()
        } else super.onBackPressed()
    }
}