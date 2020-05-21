package ru.kodeks.docmanager.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.const.Colors.GREEN_DARK
import timber.log.Timber
import java.util.*
import kotlin.system.measureTimeMillis

const val NAMESPACE = "http://schemas.android.com/apk/res/"
const val NAMESPACE2 = "http://schemas.android.com/apk/res-auto"

class PairedTextView(con: Context, atts: AttributeSet?) :
    RelativeLayout(con, atts) {
    private var inflater: LayoutInflater? = null
    var key: TextView? = null
    var value: TextView? = null
    private var keyText: String? = null
    private var keyWidth = 0
    private var displaymetrics: DisplayMetrics? = null
    private val keySet = false

    //    private var context: Context? = null
    private var attrs: AttributeSet? = null
    private var spannableString: SpannableString? = null

    var valueLineCount = 0
    private var valueText = ""

    fun setNoEllipsize() {
        removeAllViews()
        inflater!!.inflate(R.layout.paired_text, this)
        key = findViewById(R.id.pairedTextKey)
        value = findViewById(R.id.pairedTextValue)
        displaymetrics = resources.displayMetrics
        key?.measure(displaymetrics?.widthPixels ?: 0, displaymetrics?.heightPixels ?: 0)
        keyWidth = key?.measuredWidth ?: 0
        setKeyText(keyText)
        setValueText(valueText)
    }

    fun getValueText(): String {
        return valueText
    }

    fun setEllipsize() {
        removeAllViews()
        inflater!!.inflate(R.layout.paired_text_ellipsized, this)
        key = findViewById(R.id.pairedTextKey)
        value = findViewById(R.id.pairedTextValue)
        value?.ellipsize = TextUtils.TruncateAt.END
        displaymetrics = resources.displayMetrics
        key?.measure(displaymetrics!!.widthPixels, displaymetrics!!.heightPixels)
        keyWidth = key?.measuredWidth ?: 0
        setKeyText(keyText)
        setValueText(valueText)
    }

    fun setMaxLines(maxLines: Int) {
        value!!.maxLines = maxLines
        value!!.ellipsize = TextUtils.TruncateAt.END
    }

    fun setKeyText(k: String?) {
        keyText = k
        key!!.text = keyText
        displaymetrics = resources.displayMetrics
        key!!.measure(displaymetrics!!.widthPixels, displaymetrics!!.heightPixels)
        keyWidth = key!!.measuredWidth
        invalidate()
    }

    fun setKeyTextColor(color: Int) {
        key!!.setTextColor(color)
    }

    fun setValueTextColor(color: Int) {
        value!!.setTextColor(color)
    }

    fun setValueText(newText: String?) {
        if (newText == null || newText.isEmpty()) {
            valueText = ""
            this.visibility = View.GONE
            value!!.text = ""
            return
        } else {
            value!!.text = newText
            valueText = newText
            this.visibility = View.VISIBLE
            setValueSpan(newText)
        }
    }

    fun setValueSpan(string: String?) {
        spannableString = SpannableString(string)
        spannableString!!.setSpan(
            MyLeadingMarginSpan2(1, keyWidth),
            0,
            spannableString!!.length,
            Spanned.SPAN_COMPOSING
        )
        value!!.text = spannableString
        //        value.setText(string);
    }

    fun resetSpan() {
        setKeyText(key!!.text.toString())
        setValueSpan(valueText)
    }

    fun setStrings(k: String?, v: String?) {
        key!!.text = k
        displaymetrics = resources.displayMetrics
        key!!.measure(displaymetrics!!.widthPixels, displaymetrics!!.heightPixels)
        keyWidth = key!!.measuredWidth
        spannableString = SpannableString(v)
        spannableString!!.setSpan(
            MyLeadingMarginSpan2(1, key!!.measuredWidth),
            0,
            spannableString!!.length,
            0
        )
        value!!.text = spannableString
    }

    /**
     * @param executivesListString Строка со списком исполнителей, разделитель - запятая с пробелом.
     * @param mainExecutive        Ответственный исполнитель.
     * @brief Перемещает ответственного исполнителя на первое место в списке и выделяет его цветом.
     */
    fun colorMarkMainExecutive(
        executivesListString: String,
        mainExecutive: String
    ) {
        var executivesListString = executivesListString
        val array = executivesListString.split(", ").toTypedArray()
        val list = ArrayList<String>(array.size)
        /** Add main one first */
        list.add(mainExecutive)
        for (executive in array) {
            /** Then add all the rest, omitting the main one */
            if (!executive.contains(mainExecutive)) {
                list.add(executive)
            }
        }
        val builder = StringBuilder()
        for (i in list.indices) {
            builder.append(list[i]).append(if (i < list.size - 1) ", " else "")
        }
        executivesListString = builder.toString()
        spannableString = SpannableString(executivesListString)
        spannableString!!.setSpan(
            MyLeadingMarginSpan2(1, key!!.measuredWidth),
            0,
            spannableString!!.length,
            0
        )
        val start = executivesListString.indexOf(mainExecutive)
        if (start > -1) {
            spannableString!!.setSpan(
                ForegroundColorSpan(GREEN_DARK),
                start,
                start + mainExecutive.length,
                Spannable.SPAN_COMPOSING
            )
        }
        value!!.text = spannableString
    }

    fun setTabForValueText() {
        val s = value!!.text.toString()
        spannableString = SpannableString(s)
        spannableString!!.setSpan(
            MyLeadingMarginSpan2(1000, key!!.measuredWidth),
            0,
            spannableString!!.length,
            0
        )
        value!!.text = spannableString
    }

    init {
            var ellipsize = false
            if (!isInEditMode) {
                attrs = atts
                ellipsize = attrs!!.getAttributeBooleanValue(
                    NAMESPACE,
                    "ellipse",
                    false
                )
                inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            if (!isInEditMode) {
                keyText = resources!!.getString(
                    attrs!!.getAttributeResourceValue(
//                    "http://schemas.android.com/apk/res/",
                        NAMESPACE,
                        "keytext",
                        R.string.empty
                    )
                )
                val time = measureTimeMillis {
                    inflater!!.inflate(
                        if (ellipsize) R.layout.paired_text_ellipsized else R.layout.paired_text,
                        this
                    )
                }
                Timber.e("TIME: $time, ellipsized? $ellipsize")
                key = findViewById(R.id.pairedTextKey)
                value = findViewById(R.id.pairedTextValue)
            }
            if (!this.isInEditMode) {
                key!!.text = keyText
                keyText = key!!.text.toString()
                CoroutineScope(IO).launch {
                    displaymetrics = resources.displayMetrics
                    key!!.measure(displaymetrics!!.widthPixels, displaymetrics!!.heightPixels)
                    withContext(Main) {
                        keyWidth = key!!.measuredWidth
                    }
                }
            }
            val maxLines =
                attrs!!.getAttributeIntValue(
                    NAMESPACE, "maximumLines", 0
                )
            if (maxLines > 0) {
                setMaxLines(maxLines)
            }
    }
}

class MyLeadingMarginSpan2(private val lines: Int, private val margin: Int) : LeadingMarginSpan2 {

    /* Возвращает значение, на которе должен быть добавлен отступ */
    override fun getLeadingMargin(first: Boolean): Int {
        return if (first) {
            /*
                 * Данный отступ будет применен к количеству строк
                 * возвращаемых getLeadingMarginLineCount()
                 */
            margin
        } else {
            // Отступ для всех остальных строк
            0
        }
    }

    override fun drawLeadingMargin(
        c: Canvas, p: Paint, x: Int, dir: Int,
        top: Int, baseline: Int, bottom: Int, text: CharSequence,
        start: Int, end: Int, first: Boolean, layout: Layout
    ) {
    }

    /*
     * Возвращает количество строк, к которым должен быть
     * применен отступ возвращаемый методом getLeadingMargin(true)
     * Замечание:
     * Отступ применяется только к N строкам первого параграфа.
     */
    override fun getLeadingMarginLineCount(): Int {
        return lines
    }

}