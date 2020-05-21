package ru.kodeks.docmanager.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.reactivestreams.Publisher
import ru.kodeks.docmanager.db.Database
import ru.kodeks.docmanager.db.relation.DesktopWithWidgets
import ru.kodeks.docmanager.db.relation.WidgetWithDocuments
import ru.kodeks.docmanager.model.data.Document
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkbenchRepository @Inject constructor(
    database: Database,
    preferences: SharedPreferences
) : AbstractRepository(database, preferences) {

    private var workbenchLiveData: MutableLiveData<List<DesktopWithWidgets>> = MutableLiveData()

    private var currentWidgetLiveData = MutableLiveData<WidgetWithDocuments>()

    private var documentsLiveData = MediatorLiveData<List<Document>>()

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun getCurrentWidget(): LiveData<WidgetWithDocuments> = currentWidgetLiveData

//    private lateinit var desktopList: List<DesktopWithWidgets>

    fun getWorkbencLiveData(): LiveData<List<DesktopWithWidgets>> = workbenchLiveData

    fun setCurrentWidget(widget: WidgetWithDocuments) {
        if (widget.widget.id != currentWidgetLiveData.value?.widget?.id ?: 0) {
            currentWidgetLiveData.value = widget
        }
    }

    fun getCurrentWidgetDocuments(): LiveData<List<Document>> = documentsLiveData

    init {
        CoroutineScope(Main).launch {
//            desktopList = database.desktopDao().getDesktops()
            withContext(IO) {
                //
                val fromPublisher =
                    LiveDataReactiveStreams.fromPublisher<List<DesktopWithWidgets>>(Publisher
                    {
                        database.desktopDao()
                            .getDesktopsFlowable()
                            .onErrorReturn { listOf() }
                            .subscribeOn(Schedulers.io())
                    })
//                database.desktopDao().getDesktopsFlowable()
//                    .toObservable()
//                    .observeOn(Schedulers.io())
//                    .subscribe {
//                        Timber.e("SUBSCRIBED")
//                    }
//                    .also { disposables.add(it) }

                database.desktopDao().getDesktops().let {
                    workbenchLiveData.postValue(it)
                    var widgetWithDocuments = it[0].widgets[0]

                    it.forEach { desktop ->
                        if (desktop.desktop.title == "Документы") {
                            widgetWithDocuments = desktop.widgets[0]
                            return@forEach
                        }
                    }
                    withContext(Main) {
                        setCurrentWidget(widgetWithDocuments)
                    }
                }
            }
        }
        //

    }

}