package ru.kodeks.docmanager.ui.fragments.documentslist.list

import ru.kodeks.docmanager.model.data.Document
import ru.kodeks.docmanager.repository.DocumentsRepository
import ru.kodeks.docmanager.ui.main.BaseViewModel
import javax.inject.Inject

class DocumentListFragmentViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var documentsRepository: DocumentsRepository

    suspend fun getDocuments(): List<Document> {
        return documentsRepository.getDocuments()
    }
}