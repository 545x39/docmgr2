package ru.kodeks.docmanager.ui.auth

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kodeks.docmanager.AppExecutors
import ru.kodeks.docmanager.DocManagerApp
import ru.kodeks.docmanager.persistence.parser.Parser
import ru.kodeks.docmanager.ui.BaseActivity
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var app: DocManagerApp

    lateinit var viewModel: ViewModel

    @Inject
    lateinit var parser: Parser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, providerFactory).get(AuthActivityViewModel::class.java)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
//                .setAction("Action", null).show()
//        }

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


}
