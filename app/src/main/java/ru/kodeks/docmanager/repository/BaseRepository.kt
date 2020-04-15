package ru.kodeks.docmanager.repository

import android.content.SharedPreferences
import ru.kodeks.docmanager.persistence.Database
import javax.inject.Inject

open class BaseRepository @Inject constructor(val database: Database, var preferences: SharedPreferences)