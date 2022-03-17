package com.mercadolivro.core.use_cases.ports

interface Encryptor {
    fun encrypt(source: String): String
}