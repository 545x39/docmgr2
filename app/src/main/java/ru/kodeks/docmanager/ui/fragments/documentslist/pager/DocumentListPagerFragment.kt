package ru.kodeks.docmanager.ui.fragments.documentslist.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TAB_LABEL_VISIBILITY_LABELED
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_document_list_pager.*
import kotlinx.android.synthetic.main.toolbar_buttons.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.const.Tab.ALL
import ru.kodeks.docmanager.const.Tab.NEW
import ru.kodeks.docmanager.const.Tab.PENDING
import ru.kodeks.docmanager.const.Tab.TODAY
import ru.kodeks.docmanager.const.Tab.VIEWED
import ru.kodeks.docmanager.const.Tab.WEEK
import ru.kodeks.docmanager.ui.fragments.BaseFragment
import ru.kodeks.docmanager.ui.fragments.documentslist.adapter.DocumentListViewPagerAdapter
import ru.kodeks.docmanager.ui.main.MainActivity

class DocumentListPagerFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_document_list_pager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(activity as MainActivity) {
            setIcon(R.drawable.icon_two_sheets)
            setTitle(resources.getString(R.string.documents))
            toolbarScrollEnabled(true)
            enableButtons(syncButton, menuButton)
            navigationDrawerEnabled(true)
            setUpTabs()
        }
    }

    private fun MainActivity.setUpTabs() {
        enableTabs()
        documentListPager.adapter = DocumentListViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, documentListPager) { tab, position ->
            tab.text = when (position) {
                ALL -> resources.getStringArray(R.array.tabNames)[ALL]
                NEW -> resources.getStringArray(R.array.tabNames)[NEW]
                VIEWED -> resources.getStringArray(R.array.tabNames)[VIEWED]
                PENDING -> resources.getStringArray(R.array.tabNames)[PENDING]
                TODAY -> resources.getStringArray(R.array.tabNames)[TODAY]
                WEEK -> resources.getStringArray(R.array.tabNames)[WEEK]
                else -> ""
            }
            tab.icon = resources.getDrawable(
                when (position) {
                    NEW -> R.drawable.icon_sheet_with_plus
                    VIEWED -> R.drawable.icon_eye
                    PENDING -> R.drawable.icon_two_opposite_arrows
                    TODAY, WEEK -> R.drawable.icon_two_sheets
                    else -> R.drawable.icon_folder
                }, null
            )
            tab.tabLabelVisibility = TAB_LABEL_VISIBILITY_LABELED
            tab.orCreateBadge.apply {
                backgroundColor = resources.getColor(R.color.grey_darkest, null)
                number = 999

            }
        }.attach()
        tabLayout.getTabAt(0)?.orCreateBadge?.backgroundColor =
            resources.getColor(R.color.green, null)
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.orCreateBadge.apply {
                    backgroundColor = resources.getColor(R.color.green, null)
                    number = 128
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.orCreateBadge.apply {
                    backgroundColor = resources.getColor(R.color.grey_darkest, null)
                    number = 997
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}