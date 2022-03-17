package com.mercadolivro.controller.support

import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.use_cases.GetCustomerDetails
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomerSpringUserDetails(
    private val customerData: GetCustomerDetails.Output
) : UserDetails {
    val id = customerData.id

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return customerData.roles.map { SimpleGrantedAuthority(it.description) }.toMutableList()
    }

    override fun getPassword(): String = customerData.password

    override fun getUsername(): String = customerData.id.toString()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = customerData.status == CustomerStatus.ACTIVE
}