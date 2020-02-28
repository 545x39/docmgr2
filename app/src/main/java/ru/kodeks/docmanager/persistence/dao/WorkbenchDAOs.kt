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
    fun insert(widgetType: WidgetType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(widgetTypeList: List<WidgetType>)
}

@Dao
interface DesktopsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(widgetType: Desktop)
}

@Dao
interface WidgetsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(widget: Widget)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(widgetList: List<Widget>)
}

@Dao
interface ShortcutsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shortcut: Shortcut)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(shortcuts: List<Shortcut>)
}

