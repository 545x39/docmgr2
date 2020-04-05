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
import ru.kodeks.docmanager.network.resource.UserStateResource
import ru.kodeks.docmanager.ui.fragments.auth.base.BaseAuthFragment

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
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            when(it){
                is UserStateResource.Synchronizing ->{navController.navigate(R.id.action_authFormFragment_to_authProgressFragment)}
            }
        })
    }
}