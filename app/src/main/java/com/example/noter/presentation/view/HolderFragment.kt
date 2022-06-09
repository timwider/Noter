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
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val CREATE_FOLDER_FRAGMENT_TAG = "CreateFolderFragmentTag"

class HolderFragment: Fragment(R.layout.holder_fragment) {

    private lateinit var binding: HolderFragmentBinding
    private val holderViewModel: HolderViewModel by sharedViewModel()
    private val homeViewModel: HomeViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = HolderFragmentBinding.bind(view)

        val viewPager = binding.viewPager2
        val adapter = NotesTabsAdapter(this)
        viewPager.adapter = adapter

        binding.fabAddNoteOrFolder.setOnClickListener(onFabClicked(viewPager))

        setupTabMediator(viewPager = viewPager)

        homeViewModel.selectionMode.observe(viewLifecycleOwner) { selectionMode ->
            if (selectionMode == SelectionMode.ENABLED) {
                onSelectionModeEnabledVPLayout()
            } else onSelectionModeDisabledAnimated()
        }

        binding.selectionModeLayout.children.forEach {
            it.setOnClickListener(selectionActionButtonsClickListener())
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private fun selectionActionButtonsClickListener(): View.OnClickListener {
        return View.OnClickListener {
            when (it.id) {
                R.id.selection_action_select_all -> {
                    homeViewModel.setSelectionModeAction(SelectionModeAction.SELECT_ALL)
                }
                R.id.selection_action_delete -> {
                    homeViewModel.setSelectionModeAction(SelectionModeAction.DELETE)
                    onSelectionModeDisabledAnimated()
                    homeViewModel.disableSelectionMode()
                }
                R.id.selection_action_cancel -> {
                    homeViewModel.setSelectionModeAction(SelectionModeAction.CANCEL)
                    homeViewModel.disableSelectionMode()
                }
            }
        }
    }

    private fun onSelectionModeEnabled() {
        binding.selectionModeLayout.apply {

            this.alpha = 0F
            this.visibility = View.VISIBLE

            this.animate()
                .alpha(1F)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        binding.tabLayout.visibility = View.GONE
                        super.onAnimationStart(animation)
                    }
                })
                .start()
        }
    }

    private fun onSelectionModeEnabledVPLayout() {
        binding.tabLayout.apply {

            val defaultTranslationY = this.translationY

            this.animate()
                .alpha(0F)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {

                        this@apply.visibility = View.GONE
                        onSelectionModeEnabled()
                        this@apply.translationY = defaultTranslationY
                        super.onAnimationEnd(animation)
                    }
                })
                .start()
        }
    }

    private fun onSelectionModeDisabledVPLayout() {
        binding.tabLayout.apply {

            this.alpha = 0F
            this.visibility = View.VISIBLE

            this.animate()
                .alpha(1F)
                .setDuration(200)
                .setListener(null)
                .start()
        }
    }

    private fun onSelectionModeDisabledAnimated() {

        binding.selectionModeLayout.apply {

            val defaultTranslationY = this.translationY

            this.animate()
                .alpha(0F)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {

                        this@apply.visibility = View.GONE
                        this@apply.translationY = defaultTranslationY
                        onSelectionModeDisabledVPLayout()
                        super.onAnimationEnd(animation)
                    }
                })
                .start()
        }
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