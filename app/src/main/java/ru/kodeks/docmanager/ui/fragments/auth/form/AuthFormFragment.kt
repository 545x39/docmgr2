package ru.kodeks.docmanager.ui.fragments.auth.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.repository.UserRepository
import ru.kodeks.docmanager.ui.ViewModelProviderFactory
import javax.inject.Inject

class AuthFormFragment : DaggerFragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var userRepository: UserRepository
    private lateinit var viewModel: AuthFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, providerFactory).get(AuthFormViewModel::class.java)
        subscribeObservers()
        return inflater.inflate(R.layout.fragment_auth_form, container, false)
    }

    private fun subscribeObservers() {
        //TODO
    }
}