package com.mercadolivro.controller.support

import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.use_cases.GetCustomerDetails
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class UserDetailsCustomServiceTest {
    @MockK
    private lateinit var getCustomerDetails: GetCustomerDetails

    @InjectMockKs
    private lateinit var sut: UserDetailsCustomService

    @Test
    fun `loads a user by username, with expected exposed properties`() {
        val getCustomerDetailsOutput = GetCustomerDetails.Output.fromCustomerEntity(makeCustomer(1))
        every { getCustomerDetails.detailById(1) } returns getCustomerDetailsOutput

        val result = sut.loadUserByUsername(1.toString())

        assertEquals(getCustomerDetailsOutput.id.toString(), result.username)
        assertEquals(getCustomerDetailsOutput.password, result.password)
        assertTrue(result.isCredentialsNonExpired)
        assertTrue(result.isAccountNonExpired)
        assertTrue(result.isEnabled)
        assertTrue(result.isAccountNonLocked)
    }

    @Test
    fun `indicates user is disabled if his status is different than ACTIVE`() {
        val getCustomerDetailsOutput = GetCustomerDetails.Output.fromCustomerEntity(makeCustomer(1).copy(status = CustomerStatus.INACTIVE))
        every { getCustomerDetails.detailById(1) } returns getCustomerDetailsOutput

        val result = sut.loadUserByUsername(1.toString())

        assertFalse(result.isEnabled)
    }
}