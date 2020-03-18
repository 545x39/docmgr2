package ru.kodeks.docmanager.ui.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.content_main.*
import ru.kodeks.docmanager.AppExecutors
import ru.kodeks.docmanager.DocManagerApp
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.User
import ru.kodeks.docmanager.network.operations.SyncOperation
import ru.kodeks.docmanager.network.request.InitRequestBuilder
import ru.kodeks.docmanager.persistence.parser.Parser
import ru.kodeks.docmanager.ui.BaseActivity
import timber.log.Timber
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var user: User

    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var preferences: SharedPreferences

    @Inject
    lateinit var app: DocManagerApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }

        buttonSync.setOnClickListener {
            executors.diskIO().execute {
                Parser()
                    .parse()
            }
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
