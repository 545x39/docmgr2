package ru.kodeks.docmanager.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import ru.kodeks.docmanager.R

class QuickSearchView(
    context: Context,
    attrs: AttributeSet?
) : SearchView(context, attrs) {
    val searchIcon: ImageView
    private val closeButton: ImageView

    //    private lateinit var resources: Resources
    val searchText: TextView
    val searchEditFrame: LinearLayout
    var isOpened = false
    private var onResizeListener: OnResizeListener? = null
    fun setOnResizeListener(onResizeListener: OnResizeListener?) {
        this.onResizeListener = onResizeListener
    }

    private fun onResize(expand: Boolean) {
        isOpened = expand
        val coordinates = IntArray(2)
        getLocationOnScreen(coordinates)
        if (onResizeListener != null) {
            onResizeListener!!.onResize()
        }
        //        Log.d("TAG", (isOpened? "OPENED " : "CLOSED ") + " x: " + coordinates[0] + ", width: " + getWidth() + ", width in dp: " + pxToDp(getWidth()));
    }

//    fun pxToDp(px: Int): Int {
//        val displayMetrics = context.resources.displayMetrics
//        return (px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
//    }

    interface OnResizeListener {
        fun onResize()
    }

    init {
//        resources = context.resources
        searchText = findViewById(R.id.search_src_text)
        searchText.setTextColor(resources.getColor(R.color.grey_darkest, null))
        searchText.textSize = 15f
        searchIcon = findViewById(androidx.appcompat.R.id.search_button)
        searchIcon.setColorFilter(resources.getColor(R.color.grey_darkest, null))
        closeButton = findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton.setColorFilter(resources.getColor(R.color.grey_darkest, null))
        val layoutParams = searchIcon.layoutParams
        val size = (35 * resources.displayMetrics.density).toInt()
        searchEditFrame = findViewById(androidx.appcompat.R.id.search_edit_frame)
        val lprms = searchEditFrame.layoutParams as LinearLayout.LayoutParams
        lprms.leftMargin = 0
        lprms.rightMargin = 0
        searchEditFrame.layoutParams = lprms
        layoutParams.width = size
        layoutParams.height = size
        searchIcon.layoutParams = layoutParams
        closeButton.layoutParams = layoutParams
    }
}