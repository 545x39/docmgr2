package ru.kodeks.docmanager.repository

import android.content.SharedPreferences
import ru.kodeks.docmanager.db.Database

abstract class AbstractRepository constructor(
    val database: Database,
    var preferences: SharedPreferences
)