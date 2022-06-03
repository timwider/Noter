package com.example.noter.presentation.view


import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.noter.R
import com.example.noter.adapters.NotesTabsAdapter
import com.example.noter.databinding.HolderFragmentBinding
import com.example.noter.presentation.viewmodel.HolderViewModel
import com.example.noter.utils.FabAction
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val CREATE_FOLDER_FRAGMENT_TAG = "CreateFolderFragmentTag"

class HolderFragment: Fragment(R.layout.holder_fragment) {

    private lateinit var binding: HolderFragmentBinding
    private val holderViewModel: HolderViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = HolderFragmentBinding.bind(view)

        val viewPager = binding.viewPager2
        val adapter = NotesTabsAdapter(this)
        viewPager.adapter = adapter

        binding.fabAddNoteOrFolder.setOnClickListener(onFabClicked(viewPager))

        setupTabMediator(viewPager = viewPager)

        super.onViewCreated(view, savedInstanceState)
    }

    private fun onFabClicked(viewPager: ViewPager2): View.OnClickListener =
        View.OnClickListener {
            when (viewPager.currentItem) {
                0 -> holderViewModel.setFabAction(action = FabAction.ADD_NOTE)
                1 -> holderViewModel.setFabAction(action = FabAction.ADD_FOLDER)
            }
        }

    private fun setupTabMediator(viewPager: ViewPager2) {
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_notes, null)
                1 -> tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_folder, null)
            }
        }.attach()

    }
}