package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.exceptions.ResourceNotFound
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class GetCustomerDetailsTest {
    private lateinit var expectedResult: GetCustomerDetails.Output
    private lateinit var testingCustomer: Customer

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @InjectMockKs
    private lateinit var sut: GetCustomerDetails

    @BeforeEach
    fun setup() {
        // Arrange
        testingCustomer = makeCustomer()
        every { customerRepository.getById(testingCustomer.id) } returns testingCustomer
        every { customerRepository.getByEmail(testingCustomer.email) } returns testingCustomer
        expectedResult = GetCustomerDetails.Output.fromCustomerEntity(testingCustomer)
    }

    @Test
    fun `finds a customer by id`() {
        // Act
        val result = sut.detailById(testingCustomer.id)

        // Assert
        assertEquals(expectedResult, result)
        verify(exactly = 1) { customerRepository.getById(testingCustomer.id) }
    }

    @Test
    fun `finds a customer by email and expose expected properties`() {
        // Act
        val result = sut.detailByEmail(testingCustomer.email)

        // Assert
        assertEquals(expectedResult, result)
        assertEquals(expectedResult.email, result.email)
        assertEquals(expectedResult.id, result.id)
        assertEquals(expectedResult.name, result.name)
        assertEquals(expectedResult.status, result.status)
        assertEquals(expectedResult.password, result.password)
        assertEquals(expectedResult.roles, result.roles)
        verify(exactly = 1) { customerRepository.getByEmail(testingCustomer.email) }
    }

    @Test
    fun `throws an error if cannot find a customer by email`() {
        // Arrange
        every { customerRepository.getByEmail(testingCustomer.email) } returns null

        // Act
        val error = assertThrows<ResourceNotFound> {
            sut.detailByEmail(testingCustomer.email)
        }

        // Assert
        assertEquals(Errors.ML201.formatErrorCode(), error.code)
    }

    @Test
    fun `throws an error if cannot find a customer by id`() {
        // Arrange
        every { customerRepository.getById(testingCustomer.id) } returns null

        // Act
        val error = assertThrows<ResourceNotFound> {
            sut.detailById(testingCustomer.id)
        }

        // Assert
        assertEquals(Errors.ML201.formatErrorCode(), error.code)
    }
}