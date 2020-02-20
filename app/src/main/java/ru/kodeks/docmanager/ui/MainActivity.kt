package ru.kodeks.docmanager.ui

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.constants.LogTag.TAG
import ru.kodeks.docmanager.network.Parser
import ru.kodeks.docmanager.network.operations.SyncOperation
import ru.kodeks.docmanager.network.request.InitRequestBuilder
import ru.kodeks.docmanager.util.DocManagerApp
import ru.kodeks.docmanager.util.tools.stackTraceToString

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        buttonSync.setOnClickListener {
            run {
//                getTestJson()
//                doSync()
                DocManagerApp.instance.executors.diskIO().execute {  Parser().parse()}
            }
        }
    }

    private fun doSync() {
        DocManagerApp.instance.executors.networkIO().execute {
            try {
                SyncOperation(InitRequestBuilder().build()).execute()
            } catch (e: Exception) {
                Log.e(TAG, stackTraceToString(e))
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