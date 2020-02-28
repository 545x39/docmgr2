package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.kodeks.docmanager.model.data.OrgAddress
import ru.kodeks.docmanager.model.data.Organization

@Dao
interface OrganizationsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(organization: Organization)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(organizations: List<Organization>)
}

@Dao
interface OrganizationAddressesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(address: OrgAddress)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(addresses: List<OrgAddress>)

}