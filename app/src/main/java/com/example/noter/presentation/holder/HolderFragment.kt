package com.example.noter.presentation.holder

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.noter.R
import com.example.noter.adapters.NotesTabsAdapter
import com.example.noter.databinding.HolderFragmentBinding
import com.example.noter.presentation.home.HomeViewModel
import com.example.noter.utils.SelectionMode
import com.example.noter.utils.ToolbarLayoutAnimator
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val CREATE_FOLDER_FRAGMENT_TAG = "CreateFolderFragmentTag"
const val HOME_FRAGMENT_ITEM = 0
const val FOLDER_FRAGMENT_ITEM = 1

class HolderFragment: Fragment(R.layout.holder_fragment) {

    private lateinit var binding: HolderFragmentBinding
    private val holderViewModel: HolderViewModel by sharedViewModel()
    private val homeViewModel: HomeViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = HolderFragmentBinding.bind(view)
        val adapter = NotesTabsAdapter(this)
        binding.viewPager2.adapter = adapter
        binding.fabAddNoteOrFolder.setOnClickListener { holderViewModel.setFabAction(binding.viewPager2.currentItem) }
        setupTabMediator(viewPager = binding.viewPager2)

        binding.viewPager2.currentItem = holderViewModel.getCurrentItem()

        val toolbarLayoutAnimator = ToolbarLayoutAnimator(binding.selectionModeLayout, binding.tabLayout)

        homeViewModel.selectionMode.observe(viewLifecycleOwner) {
            if (it != SelectionMode.NOT_SET) toolbarLayoutAnimator.onSelectionModeChanged(it)
        }

        binding.selectionModeLayout.children.forEach { child ->
            child.setOnClickListener { homeViewModel.resolveSelectionModeAction(child.id) }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupTabMediator(viewPager: ViewPager2) {
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                HOME_FRAGMENT_ITEM -> tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_notes, null)
                FOLDER_FRAGMENT_ITEM -> tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_folder, null)
            }
        }.attach()

    }

    /**
     * This is done via viewModel because savedInstanceState doesn't work as expected
     * (viewpager doesn't set  his page properly)
     */
    override fun onDestroyView() {
        holderViewModel.saveCurrentItem(binding.viewPager2.currentItem)
        super.onDestroyView()
    }
}