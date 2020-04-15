package ru.kodeks.docmanager.ui.fragments.documentslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_base.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.ui.main.MainActivity

class DocumentListFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_document_list, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(activity as MainActivity) {
            setIcon(R.drawable.icon_two_sheets)
            setTitle("Документы")
            enableButtons(backButton, syncButton)
//            settingsButton.setOnClickListener {
//                navController.navigate(R.id.action_from_login_to_settings)
//            }
        }
    }
}