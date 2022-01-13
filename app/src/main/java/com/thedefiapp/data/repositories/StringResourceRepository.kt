package com.thedefiapp.data.repositories

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface StringResourceRepository {
    fun getString(@StringRes id: Int, vararg formatArgs: Any): String
}

class StringResourceDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : StringResourceRepository {
    override fun getString(id: Int, vararg formatArgs: Any): String {
        return context.getString(id, *formatArgs)
    }
}
