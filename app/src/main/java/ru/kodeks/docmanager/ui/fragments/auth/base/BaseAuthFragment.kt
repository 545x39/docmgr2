package ru.kodeks.docmanager.ui.fragments.auth.base

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_base.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.di.providerfactory.ViewModelProviderFactory
import ru.kodeks.docmanager.ui.main.MainActivity
import javax.inject.Inject

open class BaseAuthFragment : DaggerFragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(activity as MainActivity) {
            setIcon(R.drawable.icon_chain)
            setTitle(getString(R.string.log_in_to_system))
            enableButtons(settingsButton)
            settingsButton.setOnClickListener {
                navController.navigate(R.id.action_from_login_to_settings)
            }
        }
    }
}