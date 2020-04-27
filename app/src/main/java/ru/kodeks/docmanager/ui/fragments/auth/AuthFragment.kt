package ru.kodeks.docmanager.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.fragment_auth_form.authFormLayout
import kotlinx.android.synthetic.main.fragment_auth_form.loginButton
import kotlinx.android.synthetic.main.fragment_auth_form.loginEditText
import kotlinx.android.synthetic.main.fragment_auth_form.passwordEditText
import kotlinx.android.synthetic.main.fragment_auth_form.savePasswordCkeckbox
import kotlinx.android.synthetic.main.fragment_auth_form.versionText
import kotlinx.android.synthetic.main.toolbar_buttons.*
import ru.kodeks.docmanager.BuildConfig
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.const.SYNC_PROGRESS
import ru.kodeks.docmanager.di.factory.ViewModelProviderFactory
import ru.kodeks.docmanager.repository.resource.SyncStateResource
import ru.kodeks.docmanager.repository.resource.UserStateResource
import ru.kodeks.docmanager.ui.main.MainActivity
import javax.inject.Inject

class AuthFragment : DaggerFragment() {

    private lateinit var viewModel: AuthFragmentViewModel

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, providerFactory).get(AuthFragmentViewModel::class.java)
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        subscribeObservers()
        loginButton.setOnClickListener {
            with(activity as MainActivity) { hideKeyboard() }
            viewModel.logIn(
                loginEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
        savePasswordCkeckbox.isChecked = viewModel.getAutoLoginUser()
        savePasswordCkeckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.autoLogin(isChecked)
        }
        setVersionText()
    }

    private fun setVersionText() {
        versionText.text = buildString {
            append(resources.getString(R.string.version))
            append(BuildConfig.VERSION_NAME)
            viewModel.getUser().value?.user?.serverVersion?.let {
                append(", ${resources.getString(R.string.server_version)} $it")
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.getUser().removeObservers(viewLifecycleOwner)
        viewModel.getSyncState().removeObservers(viewLifecycleOwner)
        viewModel.getProgress().observe(viewLifecycleOwner, Observer {
            progressBar.progress = it
        })
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserStateResource.LoggedOut, is UserStateResource.NotInitialized -> {
                    authFormLayout.visibility = VISIBLE
                    authProgressLayout.visibility = GONE
                }
                is UserStateResource.LoggedIn -> {
                    navController.navigate(R.id.action_auth_to_main)
                }
            }
        })
        viewModel.getSyncState().observe(viewLifecycleOwner, Observer { info ->
            when (info) {
                is SyncStateResource.InProgress -> {
                    authFormLayout.visibility = GONE
                    authProgressLayout.visibility = VISIBLE
//                    with(activity as MainActivity) { hideKeyboard() }
                    info.data?.progress?.getString(SYNC_PROGRESS).let {
                        if (!it.isNullOrBlank())
                        progressStatusText.text = it
                    }
                }
                is SyncStateResource.Success -> {
                    navController.navigate(R.id.action_auth_to_main)
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(activity as MainActivity) {
            setIcon(R.drawable.icon_chain)
            setTitle(getString(R.string.enter_system))
            enableButtons(settingsButton)
            toolbarScrollEnabled(false)
            navigationDrawerEnabled(false)
            enableTabs(false)
            settingsButton.setOnClickListener {
                navController.navigate(R.id.action_auth_to_preferences)
            }
        }
    }
}