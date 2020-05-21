//package ru.kodeks.docmanager.ui.fragments.documentslist.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.AsyncListDiffer
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.DiffUtil.DiffResult
//import androidx.recyclerview.widget.RecyclerView
//import ru.kodeks.docmanager.R
//import ru.kodeks.docmanager.model.data.Document
//import javax.inject.Inject
//
//class DocumentListRecyclerViewAdapter @Inject constructor() :
//    RecyclerView.Adapter<ViewHolder>() {
//
//    private var documents: List<Document> = listOf()
//
////    private val differ = AsyncListDiffer(this, diffCallback)
//
//    fun setDocuments(list: List<Document>) {
//        val oldList = documents
//        val result: DiffResult = DiffUtil.calculateDiff(DocumentDiffCallback(oldList, list))
//        documents = list
//        result.dispatchUpdatesTo(this)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.document_list_item, parent, false)
//        )
//    }
//
//    override fun getItemCount() = documents.size
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        documents[position].apply {
//            holder.title.text = name
//        }
//    }
//}
//
//class DocumentDiffCallback(
//    private val oldList: List<Document>,
//    private val newList: List<Document>
//) :
//    DiffUtil.Callback() {
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
//        oldList[oldItemPosition].uid == newList[newItemPosition].uid
//
//    override fun getOldListSize() = oldList.size
//
//    override fun getNewListSize() = newList.size
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
//        oldList[oldItemPosition] == newList[newItemPosition]
//}
//
//class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//    val title: TextView = itemView.findViewById(R.id.documentRowTitle)
//
////    android:id="@+id/iconType"
////    android:id="@+id/iconControl"
////    android:id="@+id/iconIsImportant"
////    android:id="@+id/iconIsVeryImportant"
////    android:id="@+id/iconClip"
//    ////
////    android:id="@+id/documemntListCard"
////    android:id="@+id/documentListRow"
////    android:id="@+id/documentRowTitle"
////    android:id="@+id/documentNumberPairedText"
////    android:id="@+id/projectNumberPairedText"
////    android:id="@+id/correspondentPairedTextFrom"
////    android:id="@+id/correspondentPairedTextTo"
////    android:id="@+id/subjectPairedText"
////    android:id="@+id/considerationTextPairedText"
////    android:id="@+id/outgoingResolutionPairedText"
////    android:id="@+id/incomingResolutionTermPairedText"
////    android:id="@+id/outgoingResolutionTermPairedText"
////    android:id="@+id/subjectNamePairedText"
////    android:id="@+id/resolutionAuthorPairedText"
////    android:id="@+id/importance"
////    android:id="@+id/documentTypePairedText"
////    android:id="@+id/documentDatePairedText"
////    android:id="@+id/expandButtonsLayout"
////    android:id="@+id/buttonCreateResolution"
////    android:id="@+id/iconMore"
////    android:id="@+id/iconLess"
//}