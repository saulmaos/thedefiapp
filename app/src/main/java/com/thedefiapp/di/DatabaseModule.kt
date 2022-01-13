package com.thedefiapp.di

import android.content.Context
import com.thedefiapp.data.local.db.AppDatabase
import com.thedefiapp.data.local.db.dao.AddressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideAddressDao(appDatabase: AppDatabase): AddressDao =
        appDatabase.addressDao
}
