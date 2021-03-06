package ru.kodeks.docmanager.model.data

/** Статусы инстанций маршрутов рассмотрения и согласования.*/
object StationStates {
    /** Не определено*/
    const val UNDEFINED = 0

    /** Документ по движению поступил. Статус устанавливается при назначении пользователя в движение (новое движение)*/
    const val RECEIVED = 1

    /** Документ по движению прочитан. Статус устанавливается, если пользователь вошел в карточку соответствующего входящего документа.*/
    const val VIEWED = 2

    /** Документ по движению отработан. Статус устанавливается, если пользвоатель породил дело по данному
    документу или перерапределил его на другого пользователя*/
    const val WORKEDOFF = 3

    /** Документ по движению закрыт. Статус устанавливается для всех движений, если закрыватеся соответствующее дело или
    закрывается соответствующий документ (например исходящим).*/
    const val CLOSED = 4

    /** Документ по движению еще не поступил. Статус устанавливается из "шаблона" маршрута и предлагается пользователю
    как готовое движение с готовым заполненным исполнителем. Движения с данным статусом не должны отображаться в интерфейсе пользователя.*/
    const val NOT_RECEIVED_YET = 5

    /** Резолюция сохранена как проект*/
    const val PROJECT = 6

    /** Резолюция снята (отменена)*/
    const val CANCELLED = 7
}
