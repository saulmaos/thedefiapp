package com.thedefiapp.data.models

import com.thedefiapp.utils.EMPTY_STRING

data class HttpError(
    val code: String = EMPTY_STRING,
    val message: String = EMPTY_STRING,
    val description: List<HttpErrorDescription> = emptyList()
)

data class HttpErrorDescription(
    val message: String,
    val path: List<String>,
    val type: String,
    val context: HttpErrorContext
)

data class HttpErrorContext(
    val key: String,
    val label: String
)
