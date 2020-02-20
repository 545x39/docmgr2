package ru.kodeks.docmanager.util.tools

import java.io.PrintWriter
import java.io.StringWriter


/**
 * @param exception Экземпляр исключения
 * @return Стек трейс в виде строки.
 * @brief Возвращает строку со стек трейсом переданного в параметр исключения. Удобен для вывода стек трейса в лог.
 */
fun stackTraceToString(exception: Throwable): String {
    val stringWriter = StringWriter()
    exception.printStackTrace(PrintWriter(stringWriter))
    exception.printStackTrace()
    return stringWriter.toString()

}