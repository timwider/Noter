package com.example.noter.presentation.view


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.noter.R
import com.example.noter.adapters.NotesTabsAdapter
import com.example.noter.databinding.HolderFragmentBinding
import com.example.noter.presentation.viewmodel.HolderViewModel
import com.example.noter.presentation.viewmodel.HomeViewModel
import com.example.noter.utils.FabAction
import com.example.noter.utils.SelectionMode
import com.example.noter.utils.SelectionModeAction
import com.example.noter.utils.ToolbarLayoutAnimator
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val CREATE_FOLDER_FRAGMENT_TAG = "CreateFolderFragmentTag"
private const val HOME_FRAGMENT_ITEM = 0
private const val FOLDER_FRAGMENT_ITEM = 1

class HolderFragment: Fragment(R.layout.holder_fragment) {

    private lateinit var binding: HolderFragmentBinding
    private val holderViewModel: HolderViewModel by sharedViewModel()
    private val homeViewModel: HomeViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = HolderFragmentBinding.bind(view)
        val adapter = NotesTabsAdapter(this)
        binding.viewPager2.adapter = adapter
        binding.fabAddNoteOrFolder.setOnClickListener(onFabClicked(binding.viewPager2))
        setupTabMediator(viewPager = binding.viewPager2)

        val toolbarLayoutAnimator = ToolbarLayoutAnimator(binding.selectionModeLayout, binding.tabLayout)

        homeViewModel.selectionMode.observe(viewLifecycleOwner) { selectionMode ->
            when (selectionMode) {
                SelectionMode.ENABLED -> toolbarLayoutAnimator.enableSelectionMode()
                SelectionMode.DISABLED -> toolbarLayoutAnimator.disableSelectionMode()
                else -> {}
            }
        }

        binding.selectionModeLayout.children.forEach {
            it.setOnClickListener(selectionActionButtonsClickListener())
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun selectionActionButtonsClickListener(): View.OnClickListener =
        View.OnClickListener { homeViewModel.resolveSelectionModeAction(it.id) }

    private fun onFabClicked(viewPager: ViewPager2): View.OnClickListener =
        View.OnClickListener {
            when (viewPager.currentItem) {
                HOME_FRAGMENT_ITEM -> holderViewModel.setFabAction(action = FabAction.ADD_NOTE)
                FOLDER_FRAGMENT_ITEM -> holderViewModel.setFabAction(action = FabAction.ADD_FOLDER)
            }
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
}