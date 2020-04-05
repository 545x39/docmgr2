package ru.kodeks.docmanager.ui.fragments.auth.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.network.resource.UserStateResource

class StartFragment : BaseAuthFragment(){

    private lateinit var viewModel: BaseAuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, providerFactory).get(BaseAuthViewModel::class.java)
        return inflater.inflate(R.layout.fragment_progress_bar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        viewModel.logIn()
    }

    private fun subscribeObservers() {
        viewModel.getUser().removeObservers(viewLifecycleOwner)
        /** Навигация в зависимости от первоначального состояния пользователя. */
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserStateResource.LoggedIn -> {
                    navController.navigate(R.id.action_log_in_saved_user)
                }
                is UserStateResource.NotInitialized,
                is UserStateResource.Synchronizing,
                is UserStateResource.LoggedOut -> {
                    navController.navigate(R.id.action_start_to_login_navigation)
                }
            }
        })
    }
}