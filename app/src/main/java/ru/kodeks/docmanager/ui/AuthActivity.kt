package ru.kodeks.docmanager.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.content_main.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.di.StubUser
import ru.kodeks.docmanager.network.Parser
import ru.kodeks.docmanager.network.operations.SyncOperation
import ru.kodeks.docmanager.network.request.InitRequestBuilder
import ru.kodeks.docmanager.util.AppExecutors
import timber.log.Timber
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var user: StubUser

    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("APPLICATION INSTANCE: $user")
        Timber.e("PREF: $preferences")
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }

        buttonSync.setOnClickListener {
            executors.diskIO().execute { Parser().parse() }
//            CoroutineScope(IO).launch {
//                runCatching {
//                    Parser().parse()
//                }.onFailure {
//                    stackTraceToString(it)
//                    sync() }
//            }
        }
    }

    private fun sync() {
        executors.networkIO().execute {
            try {
                SyncOperation(InitRequestBuilder().build()).execute()
            } catch (e: Exception) {
                Timber.e(e)
                //TODO Здесь должны быть обработчики соответствующих исключений:
                // как реагировать на те или иные проблемы с сетью или инитом.
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
