package com.thedefiapp.usecases.room

import com.thedefiapp.data.local.db.entities.AddressEntity
import com.thedefiapp.data.repositories.AddressDatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAddressesUseCase @Inject constructor(private val addressDatabaseRepository: AddressDatabaseRepository) {
    operator fun invoke(): Flow<List<AddressEntity>> {
        return addressDatabaseRepository.getAllAddresses()
    }
}

class InsertAddressUseCase @Inject constructor(private val addressDatabaseRepository: AddressDatabaseRepository) {
    suspend operator fun invoke(address: String) {
        addressDatabaseRepository.insertAddress(AddressEntity(address))
    }
}

class DeleteAllAddressesUseCase @Inject constructor(private val addressDatabaseRepository: AddressDatabaseRepository) {
    suspend operator fun invoke() {
        addressDatabaseRepository.deleteAllAddresses()
    }
}
