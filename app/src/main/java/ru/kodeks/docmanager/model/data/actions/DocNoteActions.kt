package ru.kodeks.docmanager.model.data.actions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.model.data.DocNote

/** Группа действий по изменению пометок в одном документе, см. <see cref="IoModel.ChangeDocNotesRequest"/>. Тип документа передавать не нужно - он игнорируется.*/
data class DocNoteAction(
        /** Измененные пометки.*/
        @SerializedName("changedNotes")
        @Expose
        var changedNotes: List<DocNote>? = null,
        /** Все пометки с учетом успешно выполненных операций по изменению пометок.*/
        /** Возвращается в любом случае вне зависимости от наличия или отсутствия ошибок.*/
        @SerializedName("allNotes")
        @Expose
        var allNotes: List<DocNote>? = null
) : BaseDocumentAction<DocNoteAction>()