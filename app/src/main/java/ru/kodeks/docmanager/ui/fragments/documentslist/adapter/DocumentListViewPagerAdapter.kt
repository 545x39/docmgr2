package ru.kodeks.docmanager.ui.fragments.documentslist.adapter

import androidx.core.os.bundleOf
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kodeks.docmanager.ui.fragments.documentslist.list.DocumentListFragment
import ru.kodeks.docmanager.ui.fragments.documentslist.pager.DocumentListPagerFragment
import ru.kodeks.docmanager.ui.main.MainActivity
import java.util.*

const val NUM_ITEMS = 6

class DocumentListViewPagerAdapter(actvity: MainActivity) : FragmentStateAdapter(actvity) {

    private val fragments: ArrayList<DocumentListPagerFragment> = arrayListOf()
    override fun getItemCount(): Int {
        return NUM_ITEMS
    }

    override fun createFragment(position: Int): androidx.fragment.app.Fragment {
        return DocumentListFragment().also { it.arguments = bundleOf("tabNumber" to position) }
    }

//    override fun getItem(position: Int): Fragment {
//        return fragments[position]
//    }
//
//    override fun getCount(): Int {
//        return fragments.size
//    }
//

//    override fun getPageTitle(position: Int): CharSequence? {
//        val orientation = getResources().configuration.orientation
//        return when (position) {
//            ALL -> getResources().getStringArray(R.array.tabNames)[Tab.ALL]
//            NEW -> getResources().getStringArray(R.array.tabNames)[Tab.NEW]
//            VIEWED -> if (orientation == Configuration.ORIENTATION_LANDSCAPE) //            getResources().getStringArray(R.array.tabNames)[Tab.VIEWED]
//                "Просмотр." else "Просм."
//            PENDING -> if (orientation == Configuration.ORIENTATION_LANDSCAPE) getResources().getStringArray(R.array.tabNames)[Tab.PENDING] else "На отпр."
//            TODAY -> getResources().getStringArray(R.array.tabNames)[Tab.TODAY]
//            WEEK -> getResources().getStringArray(R.array.tabNames)[Tab.WEEK]
//            else -> ""
//        }
}