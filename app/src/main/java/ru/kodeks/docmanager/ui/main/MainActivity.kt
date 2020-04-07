package ru.kodeks.docmanager.ui.main

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_base.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.network.resource.UserStateResource
import ru.kodeks.docmanager.di.providerfactory.ViewModelProviderFactory
import javax.inject.Inject

open class MainActivity : DaggerAppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        viewModel = ViewModelProvider(this, providerFactory)
            .get(MainActivityViewModel::class.java)
        /** По умолчанию навигация отключена. */
        navigationDrawerEnabled(false)
        toolbarScrollEnabled(false)
        enableButtons()
//        setTitle("Вход в систему")
//        setIcon(R.drawable.icon_chain)
        viewModel.getUser().observe(this, Observer {
            when (it) {
                is UserStateResource.Error -> showSnackbar(it.error, it.message)
            }
        })
    }

    private fun showSnackbar(error: Throwable? = null, message: String = "") {
        var msg = message
        error?.let {
            it.message?.let { errorMessage ->
                if (!errorMessage.isNullOrEmpty()) {
                    msg = errorMessage
                }
            }
        }
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG).show()
    }

    fun setIcon(icon: Int) {
        titleIcon.setImageDrawable(getDrawable(icon))
    }

    fun setTitle(title: String) {
        screenTitle.text = title
    }

    fun enableButtons(vararg buttons: View) {
        for (button in listOf(
            backButton, syncButton,
            menuButton, settingsButton, searchButton, quick_search_view
        )) {
            button.visibility = GONE
        }
        for (button in buttons) {
            /** Кнопка меню активируется аналогично,
             * но только совместно с самим меню.*/
            if (button.id != R.id.menuButton) {
                button.let {
                    it.setOnClickListener(this)
                    it.visibility = VISIBLE
                }
            }
        }
    }

    fun toolbarScrollEnabled(enabled: Boolean) {
        val layoutParams = collapsingToolbarLayout.layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = when (enabled) {
            true ->
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
            false -> AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
        }
        collapsingToolbarLayout.layoutParams = layoutParams
        collapsingToolbarLayout.requestLayout()
    }

    fun navigationDrawerEnabled(enable: Boolean) {
        menuButton.apply {
            setOnClickListener(this@MainActivity)
            visibility = when (enable) {
                true -> VISIBLE
                false -> GONE
            }
        }
        if (enable) {
            menuButton.apply {
                setOnClickListener(this@MainActivity)
                visibility = VISIBLE
            }
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    override fun onClick(v: View?) {
        v?.id.let {
            when (it) {
                R.id.backButton -> onBackPressed()
                R.id.menuButton -> {
                    when (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        true -> drawerLayout.closeDrawer(GravityCompat.START, true)
                        false -> drawerLayout.openDrawer(GravityCompat.START, true)
                    }
                }
                R.id.syncButton -> {
                    /*TODO do sync */
                }
            }
        }
    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}