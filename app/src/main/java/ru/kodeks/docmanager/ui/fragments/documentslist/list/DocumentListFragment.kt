package ru.kodeks.docmanager.ui.fragments.documentslist.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_document_list.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.ui.fragments.BaseFragment
import ru.kodeks.docmanager.ui.fragments.documentslist.adapter.DocumentListAdapter
import timber.log.Timber
import javax.inject.Inject

class DocumentListFragment : BaseFragment() {

    private lateinit var viewModel: DocumentListFragmentViewModel

    @Inject
//    lateinit var adapter: DocumentListRecyclerViewAdapter
    lateinit var adapter: DocumentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_document_list, container, false)
        viewModel =
            ViewModelProvider(this, providerFactory).get(DocumentListFragmentViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        documentListRecyclerView.layoutManager = LinearLayoutManager(context)
        Timber.e("ADAPTER: ${documentListRecyclerView.adapter}")
        documentListRecyclerView.adapter = adapter
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.getCurrentWidget().removeObservers(viewLifecycleOwner)
        viewModel.getCurrentWidget().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.documents)
        })
    }
}