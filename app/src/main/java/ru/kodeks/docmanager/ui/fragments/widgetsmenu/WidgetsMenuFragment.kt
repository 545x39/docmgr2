package ru.kodeks.docmanager.ui.fragments.widgetsmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_widgets_menu.*
import ru.kodeks.docmanager.R

class WidgetsMenuFragment : Fragment(){

//    private val adapter: DesktopListNavigationViewAdapter? = DesktopListNavigationViewAdapter(context)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_widgets_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        widgetExpandableList.setAdapter(DesktopListNavigationViewAdapter(requireContext()))
    }
}