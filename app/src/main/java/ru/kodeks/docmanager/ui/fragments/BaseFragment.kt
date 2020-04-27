package ru.kodeks.docmanager.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.android.support.DaggerFragment
import ru.kodeks.docmanager.di.factory.ViewModelProviderFactory
import javax.inject.Inject

open class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)
    }
}