package ru.kodeks.docmanager.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.ui.ViewModelProviderFactory
import javax.inject.Inject

class AuthFragment : DaggerFragment() {
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_auth_base, container, false)
        parentFragmentManager.beginTransaction()
            .replace(R.id.authFragmentsPlaceholder, AuthFormFragment())
            .commit()
        return view
    }
}