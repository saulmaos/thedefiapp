package com.thedefiapp.di

import com.thedefiapp.data.repositories.*
import com.thedefiapp.utils.NumberFormatter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class UtilsModule {
    @Binds
    abstract fun bindStringResourceRepository(stringResourceDataSource: StringResourceDataSource): StringResourceRepository

    @Binds
    abstract fun bindAddress(addressProviderImpl: AddressProviderImpl): AddressProvider

    @Binds
    abstract fun bindPriceFormatterRepository(priceFormatterDataSource: NumberFormatter.Builder): NumberFormatterRepository
}
