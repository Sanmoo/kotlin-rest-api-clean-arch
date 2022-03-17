package com.mercadolivro.adapters

import com.mercadolivro.core.use_cases.ports.Encryptor
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class BCryptEncryptor(
    private val bCrypt: BCryptPasswordEncoder
) : Encryptor {
    override fun encrypt(source: String): String {
        return bCrypt.encode(source)
    }
}