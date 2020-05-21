package ru.kodeks.docmanager.ui.fragments.widgetsmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_widgets_menu.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.ui.fragments.BaseFragment
import ru.kodeks.docmanager.ui.main.MainActivity

class WidgetsMenuFragment : BaseFragment() {

    //    private val adapter: DesktopListNavigationViewAdapter? = DesktopListNavigationViewAdapter(context)
    lateinit var viewModel: WidgetsMenuFragmentViewModel
    private lateinit var adapter: WidgetsMenuAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_widgets_menu, container, false)
        viewModel =
            ViewModelProvider(this, providerFactory).get(WidgetsMenuFragmentViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        adapter = WidgetsMenuAdapter(requireContext())
        widgetExpandableList.setAdapter(adapter)
        widgetExpandableList.setOnChildClickListener(ExpandableListView.OnChildClickListener() { _, _, groupPosition, childPosition, _ ->
            viewModel.getWorkbench().value?.get(groupPosition)?.widgets?.get(childPosition)?.let {
                with(activity as MainActivity) {
                    setScreenTitle(it.widget.title.orEmpty())
                }
                val v = it.copy()
                viewModel.setCurrentWidget(v)
            }
            return@OnChildClickListener true
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeOdbervers()
    }

    private fun subscribeOdbervers() {
        viewModel.getWorkbench().removeObservers(viewLifecycleOwner)
        /** Навигация в зависимости от первоначального состояния пользователя. */
        viewModel.getWorkbench().observe(viewLifecycleOwner, Observer {
            if (this::adapter.isInitialized) {
                adapter.list = it
                adapter.notifyDataSetChanged()
            }
        })
//        viewModel.getCurrentWidget().removeObservers(viewLifecycleOwner)
//        viewModel.getCurrentWidget().observe(viewLifecycleOwner, Observer {
//            Timber.e("Observer triggered! ${it.widget.title}, docs: ${it.documents.size}")
//        })
    }
}