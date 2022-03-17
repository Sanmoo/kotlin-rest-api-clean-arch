package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.BookRecordRepository
import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecordRepository
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.ports.TestFactories.Companion.makePaginationData
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
internal class JPACustomerRepositoryTest {
    private lateinit var matheus: Customer
    private lateinit var alex: Customer
    private lateinit var marcos: Customer

    @Autowired
    private lateinit var sut: JPACustomerRepository

    @Autowired
    private lateinit var customerRecordRepository: CustomerRecordRepository

    @Autowired
    private lateinit var bookRecordRepository: BookRecordRepository

    @BeforeEach
    fun setup() {
        bookRecordRepository.deleteAll()
        customerRecordRepository.deleteAll()
        marcos = sut.saveCustomer("Marcos", "marcos@test.com")
        alex = sut.saveCustomer("Alex", "alex@test.com")
        matheus = sut.saveCustomer("Matheus", "matheus@test.com")
    }

    @Test
    fun `find customers by name`() {
        // Act
        val customers = sut.getAllByName("Marcos", makePaginationData())

        // Assert
        assertEquals(1, customers.content.size)
        assertEquals(marcos, customers.content[0])
    }

    @Test
    fun `find customers by email`() {
        // Assert
        assertEquals(marcos, sut.getByEmail(marcos.email))
        assertEquals(null, sut.getByEmail("anyother@test.com"))
    }

    @Test
    fun `verifies if there is already a customer with given email`() {
        // Act & Assert
        assertTrue(sut.existsByEmail(marcos.email))
        assertFalse(sut.existsByEmail("anyother@email.com"))
    }
}