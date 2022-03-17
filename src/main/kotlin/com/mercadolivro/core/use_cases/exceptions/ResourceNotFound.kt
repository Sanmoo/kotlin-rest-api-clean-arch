package com.mercadolivro.core.use_cases.exceptions

class ResourceNotFound(
    override val message: String,
    val code: String
): Exception()
