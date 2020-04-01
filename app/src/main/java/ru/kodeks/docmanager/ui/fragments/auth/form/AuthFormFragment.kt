package ru.kodeks.docmanager.ui.fragments.auth.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_auth_form.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.ui.ViewModelProviderFactory
import javax.inject.Inject

class AuthFormFragment : DaggerFragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: AuthFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auth_form, container, false)
        viewModel = ViewModelProvider(this, providerFactory).get(AuthFormViewModel::class.java)
        subscribeObservers()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener {
            viewModel.userRepository.logIn(loginEditText.text.toString(), passwordEditText.text.toString())
        }
        savePasswordCkeckbox.isChecked = viewModel.userRepository.autoLogin
        savePasswordCkeckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.autoLogin(isChecked)
        }
    }

    private fun subscribeObservers() {

    }
}