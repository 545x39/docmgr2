package ru.kodeks.docmanager.ui.fragments.auth.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_auth_progress.*
import kotlinx.android.synthetic.main.fragment_progress_bar.progressBar
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.network.resource.UserStateResource
import ru.kodeks.docmanager.ui.fragments.auth.base.BaseAuthFragment
import ru.kodeks.docmanager.ui.main.MainActivity

class AuthProgressFragment : BaseAuthFragment() {

    private lateinit var viewModel: AuthProgressViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, providerFactory).get(AuthProgressViewModel::class.java)
        return inflater.inflate(R.layout.fragment_auth_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.getUser().removeObservers(viewLifecycleOwner)
        viewModel.getProgress().observe(viewLifecycleOwner, Observer {
            progressBar.progress = it
        })
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserStateResource.Synchronizing -> {
                    progressStatusText.text = it.message
                }
                is UserStateResource.Error -> {
                    progressStatusText.text = it.message
                    navController.navigate(R.id.action_authProgressFragment_to_authFormFragment)
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(activity as MainActivity) {
            setIcon(R.drawable.icon_two_round_arrows)
            setTitle(getString(R.string.loading))
            hideKeyboard()
        }
    }
}