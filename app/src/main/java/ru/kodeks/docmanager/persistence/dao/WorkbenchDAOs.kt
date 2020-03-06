package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.kodeks.docmanager.model.data.*

@Dao
interface WidgetCategoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(widgetCategory: WidgetCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(widgetCategoryList: List<WidgetCategory>)
}

@Dao
interface WidgetTypeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(widgetType: WidgetType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(widgetTypeList: List<WidgetType>)
}

@Dao
interface DesktopsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(desktop: Desktop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(desktopa: List<Desktop>)
}

@Dao
interface WidgetsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(widget: Widget)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(widgets: List<Widget>)
}

@Dao
interface ShortcutsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shortcut: Shortcut)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(shortcuts: List<Shortcut>)
}

