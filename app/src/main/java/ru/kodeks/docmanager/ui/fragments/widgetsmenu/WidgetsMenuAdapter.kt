package ru.kodeks.docmanager.ui.fragments.widgetsmenu

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.const.Colors
import ru.kodeks.docmanager.db.relation.DesktopWithWidgets
import ru.kodeks.docmanager.model.data.Widget

class WidgetsMenuAdapter(context: Context) : BaseExpandableListAdapter() {

    var list: List<DesktopWithWidgets> = listOf()

    private var inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getGroup(groupPosition: Int) = list[groupPosition].desktop

    override fun isChildSelectable(groupPosition: Int, childPosition: Int) = true

    override fun hasStableIds() = true

    @SuppressLint("InflateParams")
    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        var viewHolder: GroupViewHolder
        if (view == null) {
            view = inflater.inflate(R.layout.new_ui_navigation_group_list_item, null)
            viewHolder = GroupViewHolder(view)
            view.tag = viewHolder
        }
        viewHolder = view!!.tag as GroupViewHolder
        val text = when (list.isNotEmpty()) {
            true -> list[groupPosition].desktop.title.orEmpty()
            false -> ""
        }
        viewHolder.title.text = text
        viewHolder.icon.setImageResource(R.drawable.icon_folder)
        val scheme = when (list.isNotEmpty()) {
            true -> list[groupPosition].desktop.colorSchema
            false -> ""
        }
        val color = getWidgetColor(scheme)
        viewHolder.icon.setColorFilter(color)
        viewHolder.title.setTextColor(color)

        viewHolder.indicator.rotation = if (isExpanded) 180f else .0f
        viewHolder.indicator.visibility =
            if (getChildrenCount(groupPosition) == 0) View.GONE else View.VISIBLE
        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return list[groupPosition].widgets.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return list[groupPosition].widgets[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        var viewHolder: ChildViewHolder
        if (view == null) {
            view = inflater.inflate(R.layout.navigation_view_widget_list_item, null)
            viewHolder = ChildViewHolder(view)
            view!!.tag = viewHolder
        }
        ////
//        if (dataList!![groupPosition].type != NavigationDataType.DESKTOP) {
//            return convertView
//        }
        ////
        viewHolder = view.tag as ChildViewHolder
        var widget: Widget? = null
        runCatching { widget = list[groupPosition].widgets[childPosition].widget }
        if (widget != null) {
            with(viewHolder) {
                widgetIcon.setColorFilter(getWidgetColor(widget?.color))
                widgetIcon.setImageResource(WidgetIconGetter.getIcon(widget?.type ?: 0))
                widgetTitle.text = widget?.title.orEmpty()
                widgetCounter.apply {
                    setTextColor(getWidgetColor(widget?.color))
                    when (val count = list[groupPosition].widgets[childPosition].documents.size) {
                        0 -> visibility = View.GONE
                        else -> {
                            text = "$count"
                            visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return list.size
    }
}

class GroupViewHolder(view: View) {
    var icon: ImageView = view.findViewById(R.id.navigation_view_list_group_icon)
    var indicator: ImageView = view.findViewById(R.id.navigation_view_list_group_indicator)
    var title: TextView = view.findViewById(R.id.navigation_view_list_group_title)
}

class ChildViewHolder(@NonNull convertView: View) {
    var widgetIcon: ImageView = convertView.findViewById(R.id.widget_icon)
    var widgetTitle: TextView = convertView.findViewById(R.id.widget_title)
    val widgetCounter: TextView = convertView.findViewById(R.id.widget_counter)
}

fun getWidgetColor(colorSchema: String?): Int {
//    if (colorSchema == null) {
//        return BLUE_DARK
//    }
    return when (colorSchema) {
        Colors.SCHEME_OCHRE -> Colors.OCHRE_DARK
        Colors.SCHEME_LILA -> Colors.LILA_DARK
        Colors.SCHEME_CYAN -> Colors.CYAN_DARK
        Colors.SCHEME_GRAY -> Colors.GRAY_DARK
        Colors.SCHEME_GREEN -> Colors.GREEN_DARK
        Colors.SCHEME_VIOLET -> Colors.VIOLET_DARK
        else -> Colors.BLUE_DARK
    }
}