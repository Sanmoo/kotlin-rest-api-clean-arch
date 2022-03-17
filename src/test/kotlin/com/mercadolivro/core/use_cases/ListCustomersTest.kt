package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.ports.TestFactories.Companion.makePaginatedResult
import com.mercadolivro.core.ports.TestFactories.Companion.makePaginationData
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ListCustomersTest {
    @MockK
    private lateinit var customerRepository: CustomerRepository

    @InjectMockKs
    private lateinit var listCustomers: ListCustomers

    @Test
    fun `lists all customers`() {
        // Arrange
        val paginationData = makePaginationData()
        val paginatedResult = makePaginatedResult(paginationData, makeCustomer())
        every { customerRepository.getAll(paginationData) } returns paginatedResult
        val expectedOutput =
            ListCustomers.Output(paginatedResult.copyToAnotherType(CreateCustomer.Output::fromCustomer))

        // Act
        val result = listCustomers.list(ListCustomers.Input(name = null, paginationData = paginationData))

        // Assert
        assertEquals(expectedOutput, result)
        assertEquals(expectedOutput.list, result.list)
    }

    @Test
    fun `lists customers by name`() {
        // Arrange
        val paginationData = makePaginationData()
        val paginatedResult = makePaginatedResult(paginationData, makeCustomer())
        every { customerRepository.getAllByName("Search...", paginationData) } returns paginatedResult
        val expectedOutput =
            ListCustomers.Output(paginatedResult.copyToAnotherType(CreateCustomer.Output::fromCustomer))

        // Act
        val result = listCustomers.list(ListCustomers.Input(name = "Search...", paginationData = paginationData))

        // Assert
        assertEquals(expectedOutput, result)
    }
}