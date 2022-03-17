package com.mercadolivro.controller.support

import com.mercadolivro.core.use_cases.GetCustomerDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class UserDetailsCustomService(
    private val getCustomerDetails: GetCustomerDetails
): UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        val detailById = getCustomerDetails.detailById(id.toInt())
        return CustomerSpringUserDetails(detailById)
    }
}