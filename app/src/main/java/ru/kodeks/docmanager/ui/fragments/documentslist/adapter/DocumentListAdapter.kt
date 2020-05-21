package ru.kodeks.docmanager.ui.fragments.documentslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.model.data.Document
import timber.log.Timber
import javax.inject.Inject

class DocumentListAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Document>() {

        override fun areItemsTheSame(oldItem: Document, newItem: Document) =
            oldItem.uid == newItem.uid

        override fun areContentsTheSame(oldItem: Document, newItem: Document) = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.document_list_item, parent, false)
//        return ViewHolder(view/*,interaction*/)
        Timber.d("CREATING HOLDER...")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Document>) {
        differ.submitList(list)
    }

    class ViewHolder(
        itemView: View
//        ,private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.documentRowTitle)
        fun bind(item: Document) = with(itemView) {
            itemView.setOnClickListener {
                //TODO
//                adapterPosition
            }
            /**Bind view with data")*/
            title.text = item.name

//    android:id="@+id/iconType"
//    android:id="@+id/iconControl"
//    android:id="@+id/iconIsImportant"
//    android:id="@+id/iconIsVeryImportant"
//    android:id="@+id/iconClip"
            ////
//    android:id="@+id/documemntListCard"
//    android:id="@+id/documentListRow"
//    android:id="@+id/documentRowTitle"
//    android:id="@+id/documentNumberPairedText"
//    android:id="@+id/projectNumberPairedText"
//    android:id="@+id/correspondentPairedTextFrom"
//    android:id="@+id/correspondentPairedTextTo"
//    android:id="@+id/subjectPairedText"
//    android:id="@+id/considerationTextPairedText"
//    android:id="@+id/outgoingResolutionPairedText"
//    android:id="@+id/incomingResolutionTermPairedText"
//    android:id="@+id/outgoingResolutionTermPairedText"
//    android:id="@+id/subjectNamePairedText"
//    android:id="@+id/resolutionAuthorPairedText"
//    android:id="@+id/importance"
//    android:id="@+id/documentTypePairedText"
//    android:id="@+id/documentDatePairedText"
//    android:id="@+id/expandButtonsLayout"
//    android:id="@+id/buttonCreateResolution"
//    android:id="@+id/iconMore"
//    android:id="@+id/iconLess"
        }
    }
}
