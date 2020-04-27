package ru.kodeks.docmanager.const

enum class WidgetType {
    /**
     * @brief Экземпляр перечисления для виджетов, относящихся к процессу рассмотрения документов.
     */
    CONSIDERATION,

    /**
     * @brief Экземпляр перечисления для виджетов, относящихся к процессу согласования документов.
     */
    APPROVAL,

    /**
     * @brief Экземпляр перечисления для виджетов проектов резолюций.
     */
    RESOLUTION_PROJECT,

    /**
     * @brief Экземпляр перечисления для виджетов, не относимых ни к одному другому типу.
     */
    NEITHER;

}

object TypeId {
    /**
     * @brief На рассмотрении
     */
    const val UNDER_CONSIDERATION = 16

    /**
     * @brief На рассмотрении (зам, например виджет "Резолюции")
     */
    const val UNDER_CONSIDERATION_ADDNL = 137

    /**
     * @brief На рассмотрении
     */
    const val UNDER_CONSIDERATION_ADDNL_2 = 139

    /**
     * @brief На рассмотрении (зам)
     */
    const val UNDER_CONSIDERATION_SUBST = 37

    /**
     * @brief На согласование
     */
    const val TO_APPROVAL = 32

    /**
     * @brief На согласование (зам)
     */
    const val TO_APPROVAL_SUBST = 38

    /**
     * @brief Согласованные
     */
    const val APPROVED = 33

    /**
     * @brief Согласованные (зам)
     */
    const val APPROVED_SUBST = 40

    /**
     * @brief Отправленные на согласование
     */
    const val SENT_TO_APPROVAL = 59

    /**
     * @brief Подписанные
     */
    const val SIGNED = 188

    /**
     * @brief Подписанные (зам)
     */
    const val SIGNED_SUBST = 190

    /**
     * @brief На доработку
     */
    const val TO_REWORK = 191

    /**
     * @brief На доработку (зам)
     */
    const val TO_REWORK_SUBST = 192

    /**
     * @brief На подпись
     */
    const val TO_SIGN = 187

    /**
     * @brief На подпись (зам)
     */
    const val TO_SIGN_SUBST = 189

    /**
     * @brief На исполнении
     */
    const val UNDER_EXECUTION = 17

    /**
     * @brief На исполнении (зам)
     */
    const val UNDER_EXECUTION_SUBST = 39

    /**
     * @brief Отчеты по резолюциям
     */
    const val RESOLUTION_REPORTS = 150

    /**
     * @brief Отчеты по резолюциям (зам)
     */
    const val RESOLUTION_REPORTS_SUBST = 151

    /**
     * @brief Мои отчеты
     */
    const val MY_REPORTS = 160

    /**
     * @brief Мои отчеты (зам)
     */
    const val MY_REPORTS_SUBST = 161

    /**
     * @brief Проекты резолюций без особых признаков
     */
    const val RESOLUTION_PROJECTS_COMMON = 197

    /**
     * @brief Проекты резолюций без особых признаков (зам)
     */
    const val RESOLUTION_PROJECTS_COMMON_SUBST = 163

    /**
     * @brief Правительственные проекты резолюций (зам)
     */
    const val RESOLUTION_PROJECTS_GOV = 196

    /**
     * @brief Правительственные проекты резолюций (зам)
     */
    const val RESOLUTION_PROJECTS_GOV_SUBST = 138

    /**
     * @brief Личный контроль
     */
    const val PERSONAL_CONTROL = 210

    /**
     * @brief Для ознакомления
     */
    const val FOR_REVIEW = 164

    /**
     * @brief Важные
     */
    const val IMPORTANT = 208

    /**
     * @brief Очень важные
     */
    const val VERY_IMPORTANT = 209

    /**
     * @brief Оперативный архив
     */
    const val OPERATIONAL_ARCHIVE = 219

    /**
     * @brief ОРД для ознакомления
     */
    const val ORGANIZATIONAL_FOR_REVIEW = 146

//        fun getType(widgetId: Int): WidgetType {
//            return getTypeByWidgetTypeId(
//                WidgetManager.getInstance().getWidgetTypeId(widgetId)
//            )
//        }
}

fun getTypeByWidgetTypeId(widgetType: Int): WidgetType {
    return when (widgetType) {
        TypeId.OPERATIONAL_ARCHIVE, TypeId.ORGANIZATIONAL_FOR_REVIEW -> WidgetType.NEITHER
        TypeId.RESOLUTION_PROJECTS_COMMON, TypeId.RESOLUTION_PROJECTS_COMMON_SUBST, TypeId.RESOLUTION_PROJECTS_GOV, TypeId.RESOLUTION_PROJECTS_GOV_SUBST, TypeId.IMPORTANT, TypeId.VERY_IMPORTANT -> WidgetType.RESOLUTION_PROJECT
        TypeId.UNDER_CONSIDERATION, TypeId.UNDER_CONSIDERATION_SUBST, TypeId.UNDER_CONSIDERATION_ADDNL, TypeId.UNDER_CONSIDERATION_ADDNL_2, TypeId.UNDER_EXECUTION, TypeId.UNDER_EXECUTION_SUBST, TypeId.RESOLUTION_REPORTS, TypeId.RESOLUTION_REPORTS_SUBST, TypeId.MY_REPORTS, TypeId.MY_REPORTS_SUBST, TypeId.PERSONAL_CONTROL, TypeId.FOR_REVIEW -> WidgetType.CONSIDERATION
        TypeId.TO_APPROVAL, TypeId.TO_APPROVAL_SUBST, TypeId.APPROVED, TypeId.APPROVED_SUBST, TypeId.SIGNED, TypeId.SIGNED_SUBST, TypeId.TO_REWORK, TypeId.TO_REWORK_SUBST, TypeId.TO_SIGN, TypeId.TO_SIGN_SUBST -> WidgetType.APPROVAL
        else -> WidgetType.CONSIDERATION
    }
}