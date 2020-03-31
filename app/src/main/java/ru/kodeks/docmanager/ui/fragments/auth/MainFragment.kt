package ru.kodeks.docmanager.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_main.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.network.resource.UserStateResource
import ru.kodeks.docmanager.ui.ViewModelProviderFactory
import ru.kodeks.docmanager.ui.fragments.ProgressBarFragment
import ru.kodeks.docmanager.ui.fragments.auth.form.AuthFormFragment
import ru.kodeks.docmanager.ui.fragments.auth.progress.AuthProgressFragment
import javax.inject.Inject

class MainFragment : DaggerFragment() {
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        viewModel = ViewModelProvider(this, providerFactory).get(MainViewModel::class.java)
        switchFragment(ProgressBarFragment(), false)
        subscribeObservers()
        return view
    }

    private fun switchFragment(fragment: Fragment, addToBackStack: Boolean = true){
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.authFragmentsPlaceholder,
                fragment
            ).apply {
                if (addToBackStack){
                    addToBackStack(fragment::class.simpleName)
                }
            }.commit()
    }

    private fun subscribeObservers() {
        viewModel.userRepository.cachedUser.observe(viewLifecycleOwner, Observer{
            when(it){
                is UserStateResource.NotInitialized -> {switchFragment(AuthFormFragment(), false)}
                is UserStateResource.Synchronizing -> {switchFragment((AuthProgressFragment()), false)}
                is UserStateResource.LoggedIn -> {switchFragment((ProgressBarFragment()), true)}
                else -> {Snackbar.make(versionText, "Unknown user state: $it", Snackbar.LENGTH_SHORT).show()}
            }
        })
        viewModel.userRepository.logIn()
    }
}