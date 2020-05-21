package ru.kodeks.docmanager.ui.fragments.documentslist.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TAB_LABEL_VISIBILITY_LABELED
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_document_list_pager.*
import kotlinx.android.synthetic.main.toolbar_buttons.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.const.Tab.ALL
import ru.kodeks.docmanager.const.Tab.NEW
import ru.kodeks.docmanager.const.Tab.PENDING
import ru.kodeks.docmanager.const.Tab.TODAY
import ru.kodeks.docmanager.const.Tab.VIEWED
import ru.kodeks.docmanager.const.Tab.WEEK
import ru.kodeks.docmanager.ui.fragments.BaseFragment
import ru.kodeks.docmanager.ui.fragments.documentslist.adapter.DocumentListViewPagerAdapter
import ru.kodeks.docmanager.ui.fragments.widgetsmenu.WidgetIconGetter
import ru.kodeks.docmanager.ui.main.MainActivity
import timber.log.Timber

class DocumentListPagerFragment : BaseFragment() {

    private lateinit var viewModel: DocumentListPagerFragmentViewModel

    var adapter: DocumentListViewPagerAdapter? = null

    lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, providerFactory)
                .get(DocumentListPagerFragmentViewModel::class.java)
        return inflater.inflate(R.layout.fragment_document_list_pager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        CoroutineScope(Main).launch {
            with(activity as MainActivity) {
                toolbarScrollEnabled(true)
                enableButtons(syncButton, menuButton)
                navigationDrawerEnabled(true)
                this@DocumentListPagerFragment.searchView = searchView
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.getCurrentWidget().removeObservers(viewLifecycleOwner)
        viewModel.getCurrentWidget().observe(viewLifecycleOwner, Observer {
            Timber.e("RESETTING TABS")
            with(activity as MainActivity) {
                setScreenTitle(it.widget.title.orEmpty())
                setIcon(WidgetIconGetter.getIcon(it.widget.type ?: 0))
            }
            setUpTabs()
        })
    }

    private fun setUpTabs() {
        with(activity as MainActivity) {
            enableTabs()
            if (adapter == null) {
                documentListPager.offscreenPageLimit = 4
                adapter = DocumentListViewPagerAdapter(this)
                documentListPager.adapter = adapter
            }
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
                    setBadge(this)
                }
            }.attach()
            tabLayout.getTabAt(0)?.orCreateBadge?.backgroundColor =
                resources.getColor(R.color.green, null)
            tabLayout.addOnTabSelectedListener(Listener())
        }
        adapter?.notifyDataSetChanged()
    }

    private fun setBadge(badge: BadgeDrawable) {
        badge.apply {
            CoroutineScope(IO).launch {
                val count = viewModel.getCurrentWidget().value?.documents?.size ?: 0
                isVisible = count > 0
                number = count
            }
        }
    }

    inner class Listener : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            tab.orCreateBadge.apply {
                backgroundColor = resources.getColor(R.color.green, null)
                setBadge(this)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            tab.orCreateBadge.apply {
                backgroundColor = resources.getColor(R.color.grey_darkest, null)
                setBadge(this)
            }
        }

        override fun onTabReselected(tab: TabLayout.Tab) {}
    }
}