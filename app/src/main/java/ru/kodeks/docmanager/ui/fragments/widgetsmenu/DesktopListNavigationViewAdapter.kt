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

class DesktopListNavigationViewAdapter(context: Context) : BaseExpandableListAdapter() {

    private var inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getGroup(groupPosition: Int) = Any()

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
        viewHolder.title.text = "На нассмотрение"
        viewHolder.icon.setImageResource(R.drawable.icon_folder)
//        val color: Int = getDataList().get(groupPosition).getColor()
//        if (color >= 0) {
//            viewHolder.icon.setColorFilter(color)
//        }
        viewHolder.indicator.rotation = if (isExpanded) 180f else .0f
        viewHolder.indicator.visibility =
            if (getChildrenCount(groupPosition) == 0) View.GONE else View.VISIBLE
        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 4
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return Any()
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
        /** Take only ENABLED widgets, mess occurs otherwise. */
//        val widget = with(dataList!![groupPosition]) {
//            return@with DesktopManager.getInstance().desktops.firstOrNull { this.id == it.id }?.widgetList?.filter { !it.disabled }?.get(childPosition)
//        }
//        if (widget != null) {
//            with(viewHolder) {
//                widgetIcon.setColorFilter(widget.color1)
//                widgetIcon.setImageResource(WidgetIconGetter.getIcon(widget.typeId))
//                widgetTitle.text = widget.title
//                widgetCounter.setTextColor(widget.color1)
//            }
//            CounterSetterTask(widget.id, viewHolder.widgetCounter).execute()
//            viewHolder.widgetCounter.visibility = View.VISIBLE
//        }
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return 8
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
}