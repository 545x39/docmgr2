package ru.kodeks.docmanager.ui.fragments.documentslist.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.ui.fragments.BaseFragment

class DocumentListFragment : BaseFragment() {

    private lateinit var viewModel: DocumentListFragmentViewModel

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
        CoroutineScope(IO).launch {
            viewModel.getDocuments()
        }
    }

}