package ru.kodeks.docmanager.ui.main

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_base.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.ui.ViewModelProviderFactory
import ru.kodeks.docmanager.ui.fragments.auth.AuthFragment
import javax.inject.Inject

open class MainActivity : DaggerAppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        /** Параметр owner задаёт "Scope" для ViewModel, т.е. то, к
         * жмзненному циклу чего будет привязана эта ViewModel.
         * ViewModelStoreOwner - интерфейс, который наследуют
         * активности, фрагменты и различные виджеты.*/
        viewModel = ViewModelProvider(this, providerFactory)
            .get(MainActivityViewModel::class.java)
        /** По умолчанию навигация отключена. */
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        enableNavigationDrawer()
//        enableToolbarScroll()
        enableButtons(settingsButton)
        setTitle("Вход в систему")
        setIcon(R.drawable.icon_chain)
        ////
        supportFragmentManager.beginTransaction().add(R.id.contentPlaceholder, AuthFragment()).commit()
//        buttonSync.setOnClickListener {
//            executors.diskIO().execute {
//                parser.parse()
//            }

        //////
//        CoroutineScope(IO).launch {
//            runCatching {
//                Parser().parse()
//             //   sync()
//            }.onFailure {
//                Timber.e(it)
//                sync()
//            }
//        }
    }
    //private fun sync() {
//    executors.networkIO().execute {
//        try {
//            SyncOperation(InitRequestBuilder().build()).execute()
//        } catch (e: Exception) {
//            Timber.e(e)
//            //TODO Здесь должны быть обработчики соответствующих исключений:
//            // как реагировать на те или иные проблемы с сетью или инитом.
//        }
//    }
//}

    private fun setIcon(icon: Int) {
        titleIcon.setImageDrawable(getDrawable(icon))
    }

    fun setTitle(title: String) {
        screenTitle.text = title
    }

    fun enableButtons(vararg buttons: View) {
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

    open fun enableToolbarScroll() {
        val layoutParams = collapsingToolbarLayout.layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
        collapsingToolbarLayout.layoutParams = layoutParams
        collapsingToolbarLayout.requestLayout()
    }

    open fun enableNavigationDrawer() {
        menuButton.apply {
            setOnClickListener(this@MainActivity)
            visibility = VISIBLE
        }
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
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
                R.id.settingsButton -> {
                    /*TODO open settings */
                }
                R.id.syncButton -> {
                    /*TODO do sync */
                }
            }
        }
    }
}