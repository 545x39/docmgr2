package ru.kodeks.docmanager.ui.fragments.widgetsmenu

import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.const.TypeId.APPROVED
import ru.kodeks.docmanager.const.TypeId.APPROVED_SUBST
import ru.kodeks.docmanager.const.TypeId.MY_REPORTS
import ru.kodeks.docmanager.const.TypeId.MY_REPORTS_SUBST
import ru.kodeks.docmanager.const.TypeId.RESOLUTION_PROJECTS_COMMON
import ru.kodeks.docmanager.const.TypeId.RESOLUTION_PROJECTS_COMMON_SUBST
import ru.kodeks.docmanager.const.TypeId.RESOLUTION_PROJECTS_GOV
import ru.kodeks.docmanager.const.TypeId.RESOLUTION_PROJECTS_GOV_SUBST
import ru.kodeks.docmanager.const.TypeId.RESOLUTION_REPORTS
import ru.kodeks.docmanager.const.TypeId.RESOLUTION_REPORTS_SUBST
import ru.kodeks.docmanager.const.TypeId.SENT_TO_APPROVAL
import ru.kodeks.docmanager.const.TypeId.SIGNED
import ru.kodeks.docmanager.const.TypeId.SIGNED_SUBST
import ru.kodeks.docmanager.const.TypeId.TO_APPROVAL
import ru.kodeks.docmanager.const.TypeId.TO_APPROVAL_SUBST
import ru.kodeks.docmanager.const.TypeId.TO_REWORK
import ru.kodeks.docmanager.const.TypeId.TO_REWORK_SUBST
import ru.kodeks.docmanager.const.TypeId.TO_SIGN
import ru.kodeks.docmanager.const.TypeId.TO_SIGN_SUBST
import ru.kodeks.docmanager.const.TypeId.UNDER_CONSIDERATION
import ru.kodeks.docmanager.const.TypeId.UNDER_CONSIDERATION_ADDNL
import ru.kodeks.docmanager.const.TypeId.UNDER_CONSIDERATION_ADDNL_2
import ru.kodeks.docmanager.const.TypeId.UNDER_CONSIDERATION_SUBST
import ru.kodeks.docmanager.const.TypeId.UNDER_EXECUTION
import ru.kodeks.docmanager.const.TypeId.UNDER_EXECUTION_SUBST

object WidgetIconGetter {
    fun getIcon(widgetType: Int): Int {
        return when (widgetType) {
            UNDER_EXECUTION, UNDER_EXECUTION_SUBST -> R.drawable.icon_hammer
            UNDER_CONSIDERATION, UNDER_CONSIDERATION_SUBST, UNDER_CONSIDERATION_ADDNL, UNDER_CONSIDERATION_ADDNL_2 -> R.drawable.icon_sand_glass
            TO_APPROVAL, TO_APPROVAL_SUBST -> R.drawable.icon_sheet_with_question_sign
            APPROVED, APPROVED_SUBST -> R.drawable.icon_sheet_with_check_mark
            TO_REWORK, TO_REWORK_SUBST -> R.drawable.icon_sheet_with_cross
            TO_SIGN, TO_SIGN_SUBST -> R.drawable.icon_pencil_with_sand_glass
            SIGNED, SIGNED_SUBST -> R.drawable.icon_pencil
            RESOLUTION_REPORTS, RESOLUTION_REPORTS_SUBST, MY_REPORTS, MY_REPORTS_SUBST -> R.drawable.icon_sheet_with_arrow_out
            RESOLUTION_PROJECTS_COMMON, RESOLUTION_PROJECTS_COMMON_SUBST, RESOLUTION_PROJECTS_GOV, RESOLUTION_PROJECTS_GOV_SUBST -> R.drawable.icon_sheet_with_russian_p_letter_and_arrow_in
            SENT_TO_APPROVAL -> R.drawable.icon_two_sheets
            else -> R.drawable.icon_two_sheets
        }
    }
}