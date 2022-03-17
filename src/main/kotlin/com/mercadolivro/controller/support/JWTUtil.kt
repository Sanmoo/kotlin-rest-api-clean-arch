package com.mercadolivro.controller.support

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import java.util.*

class JWTUtil {
    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    @Value("\${jwt.secret}")
    private val secret: String? = null

    fun generateToken(id: String?): String {
        return Jwts.builder().setSubject(id).setExpiration(Date(System.currentTimeMillis() + expiration!!))
            .signWith(SignatureAlgorithm.HS512, secret!!.toByteArray()).compact()
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secret!!.toByteArray()).parseClaimsJws(token).body
    }

    fun getSubject(token: String): String {
        return getClaims(token).subject
    }
}