package com.thedefiapp.data.local.db.dao

import androidx.room.*
import com.thedefiapp.data.local.db.entities.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(address: AddressEntity)

    @Query("SELECT * FROM address_table")
    fun getAll(): Flow<List<AddressEntity>>

    @Query("DELETE FROM address_table")
    suspend fun deleteAll()
}
