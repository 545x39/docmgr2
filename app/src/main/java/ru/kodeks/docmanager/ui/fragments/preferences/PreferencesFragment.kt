package ru.kodeks.docmanager.ui.fragments.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import kotlinx.android.synthetic.main.activity_main.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.ui.main.MainActivity

class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(activity as MainActivity) {
            setIcon(R.drawable.icon_gear)
            setTitle(getString(R.string.preferences))
            enableButtons(backButton)
        }
    }
}