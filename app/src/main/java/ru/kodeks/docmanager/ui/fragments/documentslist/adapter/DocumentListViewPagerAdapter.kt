package ru.kodeks.docmanager.ui.fragments.documentslist.adapter

import androidx.core.os.bundleOf
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kodeks.docmanager.ui.fragments.documentslist.list.DocumentListFragment
import ru.kodeks.docmanager.ui.main.MainActivity

const val NUM_ITEMS = 6

class DocumentListViewPagerAdapter(actvity: MainActivity) : FragmentStateAdapter(actvity) {


    override fun getItemCount(): Int {
        return NUM_ITEMS
    }

    override fun createFragment(position: Int): androidx.fragment.app.Fragment {
        return DocumentListFragment().also { it.arguments = bundleOf("tabNumber" to position) }
    }

}