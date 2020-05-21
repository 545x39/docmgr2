package ru.kodeks.docmanager.ui.fragments.documentslist.list

import ru.kodeks.docmanager.repository.WorkbenchRepository
import ru.kodeks.docmanager.ui.main.BaseViewModel
import javax.inject.Inject

class DocumentListFragmentViewModel @Inject constructor(private val workbenchRepository: WorkbenchRepository) :
    BaseViewModel() {

    fun getCurrentWidget() = workbenchRepository.getCurrentWidget()
}