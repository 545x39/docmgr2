package ru.kodeks.docmanager.ui.fragments.auth.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_auth_form.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.repository.resource.SyncStateResource
import ru.kodeks.docmanager.repository.resource.UserStateResource
import ru.kodeks.docmanager.ui.fragments.auth.base.BaseAuthFragment
import timber.log.Timber

class AuthFormFragment : BaseAuthFragment() {

    private lateinit var viewModel: AuthFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, providerFactory).get(AuthFormViewModel::class.java)
        return inflater.inflate(R.layout.fragment_auth_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        subscribeObservers()
        loginButton.setOnClickListener {
            viewModel.logIn(
                loginEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
        savePasswordCkeckbox.isChecked = viewModel.getAutoLoginUser()
        savePasswordCkeckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.autoLogin(isChecked)
        }
    }

    private fun subscribeObservers() {
        viewModel.getUser().removeObservers(viewLifecycleOwner)
        viewModel.getSyncState().removeObservers(viewLifecycleOwner)
        viewModel.getSyncProgress().observe(viewLifecycleOwner, Observer {
            Timber.e("PROGRESS: $it")
        })
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserStateResource.LoggedOut -> {
                    val serverVerson =
                        "${resources.getString(R.string.server_version)}${viewModel.getUser().value?.user?.serverVersion.orEmpty()}"
                    versionText.text = serverVerson
                }
                is UserStateResource.LoggedIn -> {
                    navController.navigate(R.id.action_log_in_on_init_sucess)
                }
                else -> {
                    Timber.e("State: $it")
                }
            }
        })
        viewModel.getSyncState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is SyncStateResource.Active -> navController.navigate(R.id.action_authFormFragment_to_authProgressFragment)
                is SyncStateResource.Success -> navController.navigate(R.id.action_log_in_on_init_sucess)
            }
        })
    }
}