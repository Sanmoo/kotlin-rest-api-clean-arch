package com.mercadolivro.controller.support

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadolivro.controller.dto.LoginRequest
import com.mercadolivro.core.use_cases.GetCustomerDetails
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val getCustomerDetails: GetCustomerDetails,
    private val jwtUtil: JWTUtil
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val loginRequest = jacksonObjectMapper().readValue(request.inputStream, LoginRequest::class.java)
        val id = getCustomerDetails.detailByEmail(loginRequest.email).id
        val authToken = UsernamePasswordAuthenticationToken(id, loginRequest.password)
        return authenticationManager.authenticate(authToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication
    ) {
        // Need to make sure it is not null so that we avoid a missing branch
        // in test coverage report
        val nonNullObject = authResult.principal!!
        val id = (nonNullObject as CustomerSpringUserDetails).id
        val token = jwtUtil.generateToken(id.toString())
        response.addHeader("Authorization", "Bearer $token")
    }
}