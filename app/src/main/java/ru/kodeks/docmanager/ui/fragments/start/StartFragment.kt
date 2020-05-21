package ru.kodeks.docmanager.ui.fragments.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.repository.resource.UserStateResource
import ru.kodeks.docmanager.ui.fragments.BaseFragment
import ru.kodeks.docmanager.ui.fragments.auth.AuthFragmentViewModel

class StartFragment : BaseFragment() {

    private lateinit var viewModel: AuthFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, providerFactory).get(AuthFragmentViewModel::class.java)
        return inflater.inflate(R.layout.fragment_progress_bar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.getUser().removeObservers(viewLifecycleOwner)
        viewModel.getSyncState().removeObservers(viewLifecycleOwner)
        /** Навигация в зависимости от первоначального состояния пользователя. */
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserStateResource.LoggedIn -> {
                    navController.navigate(R.id.action_auth_to_main)
                }
                is UserStateResource.NotInitialized,
                is UserStateResource.LoggedOut -> {
                    navController.navigate(R.id.action_start_to_auth)
                }
            }
        })
    }
}