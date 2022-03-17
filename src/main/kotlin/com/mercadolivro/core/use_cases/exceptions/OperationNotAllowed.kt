package com.mercadolivro.core.use_cases.exceptions

class OperationNotAllowed (
    val reason: String,
    val code: String
): Exception()
