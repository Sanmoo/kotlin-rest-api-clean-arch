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

    fun generateToken(id: String): String {
        return Jwts.builder().setSubject(id).setExpiration(Date(System.currentTimeMillis() + expiration!!))
            .signWith(SignatureAlgorithm.HS512, secret!!.toByteArray()).compact()
    }

    fun isValidToken(token: String): Boolean {
        val claims  = getClaims(token)
        if (claims.subject == null || claims.expiration == null || Date().after(claims.expiration)) {
            return false
        }
        return true
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secret!!.toByteArray()).parseClaimsJws(token).body
    }

    fun getSubject(token: String): String {
        return getClaims(token).subject
    }
}