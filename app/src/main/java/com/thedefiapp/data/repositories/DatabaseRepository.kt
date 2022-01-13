package com.thedefiapp.data.repositories

import com.thedefiapp.data.local.db.dao.AddressDao
import com.thedefiapp.data.local.db.entities.AddressEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddressDatabaseRepository @Inject constructor(private val addressDao: AddressDao) {
    suspend fun insertAddress(address: AddressEntity) {
        addressDao.insert(address)
    }

    fun getAllAddresses(): Flow<List<AddressEntity>> =
        addressDao.getAll()

    suspend fun deleteAllAddresses() =
        addressDao.deleteAll()
}
