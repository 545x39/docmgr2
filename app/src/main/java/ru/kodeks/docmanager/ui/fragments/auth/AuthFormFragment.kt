package ru.kodeks.docmanager.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kodeks.docmanager.R

class AuthFormFragment : //Dagger
Fragment() {

//    @Inject
//    lateinit var providerFactory: ViewModelProviderFactory
//    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        return inflater.inflate(R.layout.fragment_auth_form, container, false)
    }
}