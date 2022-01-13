package com.thedefiapp.data.repositories

import com.thedefiapp.utils.EMPTY_STRING
import javax.inject.Inject
import javax.inject.Singleton

interface AddressProvider {
    fun getAddress(): String
    fun setAddress(address: String)
}

@Singleton
class AddressProviderImpl @Inject constructor() : AddressProvider {
    private var address = EMPTY_STRING
    override fun getAddress(): String {
        return address
    }

    override fun setAddress(address: String) {
        this.address = address
    }
}
