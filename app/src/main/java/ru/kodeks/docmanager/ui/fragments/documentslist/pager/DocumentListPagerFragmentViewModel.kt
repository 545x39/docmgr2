package ru.kodeks.docmanager.ui.fragments.documentslist.pager

import ru.kodeks.docmanager.db.relation.WidgetWithDocuments
import ru.kodeks.docmanager.repository.WorkbenchRepository
import ru.kodeks.docmanager.ui.main.BaseViewModel
import javax.inject.Inject

class DocumentListPagerFragmentViewModel @Inject constructor(private val workbenchRepository: WorkbenchRepository) :
    BaseViewModel() {

    fun getCurrentWidget() = workbenchRepository.getCurrentWidget()
    fun setCurrentWidget(widget: WidgetWithDocuments){
        workbenchRepository.setCurrentWidget(widget)
    }
}