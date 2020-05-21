package ru.kodeks.docmanager.ui.fragments.widgetsmenu

import androidx.lifecycle.LiveData
import dagger.Module
import ru.kodeks.docmanager.db.relation.DesktopWithWidgets
import ru.kodeks.docmanager.db.relation.WidgetWithDocuments
import ru.kodeks.docmanager.repository.WorkbenchRepository
import ru.kodeks.docmanager.ui.main.BaseViewModel
import javax.inject.Inject

@Module
class WidgetsMenuFragmentViewModel @Inject constructor(private val workbenchRepository: WorkbenchRepository) :
    BaseViewModel() {

    fun setCurrentWidget(widget: WidgetWithDocuments){
        workbenchRepository.setCurrentWidget(widget)
    }

    fun getCurrentWidget() = workbenchRepository.getCurrentWidget()

    fun getWorkbench(): LiveData<List<DesktopWithWidgets>> {
        return workbenchRepository.getWorkbencLiveData()
    }
}