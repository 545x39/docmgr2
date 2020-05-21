package ru.kodeks.docmanager.const

object Tab {
    /**
     * @brief Вкладка "Все"
     */
    const val ALL = 0

    /**
     * @brief Вкладка "Новые"
     */
    const val NEW = 1

    /**
     * @brief Вкладка "Просмотренные"
     */
    const val VIEWED = 2

    /**
     * @brief Вкладка "На отправку"
     */
    const val PENDING = 3

    /**
     * @brief Вкладка "Сегодня"
     */
    const val TODAY = 4

    /**
     * @brief Вкладка "Неделя"
     */
    const val WEEK = 5

    /**
     * @brief Вкладка "Входящие резолюции"
     */
    const val INCOMING_RESOLUTIONS = 6

    /**
     * @brief Вкладка "Входящие отчёты"
     */
    const val INCOMING_REPORTS = 7

}

object Colors {
    /**
     * Color scheme string constants
     */
    const val SCHEME_BLUE = "Blue"
    const val SCHEME_OCHRE = "Ochre"
    const val SCHEME_LILA = "Lila"
    const val SCHEME_CYAN = "Cyan"
    const val SCHEME_GRAY = "Gray"
    const val SCHEME_GREEN = "Green"
    const val SCHEME_VIOLET = "Violet"
    const val SCHEME_ORANGE = "Orange"
    const val SCHEME_DEFAULT = ""

    /**
     * Colors themselves
     */
    const val BLUE_DARK = -0xeba779
    const val BLUE_LIGHT = -0xde874b
    const val OCHRE_DARK = -0x576f92
    const val OCHRE_LIGHT = -0x224d81
    const val LILA_DARK = -0x69a579
    const val LILA_LIGHT = -0x37874f
    const val CYAN_DARK = -0xba7562
    const val CYAN_LIGHT = -0xa14932
    const val GRAY_DARK = -0x6c6a68
    const val GRAY_LIGHT = -0x434140
    const val GREEN_DARK = -0xcc7578
    const val GREEN_LIGHT = -0xb44d51
    const val VIOLET_DARK = -0x858c67
    const val VIOLET_LIGHT = -0x5d6938
    const val ORANGE = -0x224d81

    /**
     * Station list state rootlets colors
     */
    const val STATION_IN_PROCESS = 0x55FFF799
    const val STATION_OUTDATED = 0x66FF0000
    const val STATION_EXECUTED_IN_TIME = 0x5582CA9C
    const val STATION_INFORMATIONAL_RESOLUTION = 0x55DDDDDD

    /**
     * Main executive highlight color
     */
    const val STATION_MAIN_EXECUTIVE = -0x7d3564

    /** New UI color scheme.  */
    const val UI_GREEN = -0xce7679
    const val UI_BLUE = -0xb96c31
    const val UI_LIGHT_YELLOW = -0xd40
}