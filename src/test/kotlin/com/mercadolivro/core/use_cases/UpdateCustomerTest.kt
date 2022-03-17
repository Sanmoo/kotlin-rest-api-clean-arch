package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.Role
import com.mercadolivro.core.entities.TestFactories
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.exceptions.OperationNotAllowed
import com.mercadolivro.core.use_cases.exceptions.ResourceNotFound
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import com.mercadolivro.core.use_cases.ports.Encryptor
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class UpdateCustomerTest {
    private lateinit var newStatus: CustomerStatus
    private lateinit var newName: String
    private lateinit var newRoles: Set<Role>
    private lateinit var newEmail: String
    private val newPassword: String = "12345_newPass"
    private lateinit var testingCustomer: Customer

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var encryptor: Encryptor

    @InjectMockKs
    private lateinit var sut: UpdateCustomer

    private fun setup(customerStatus: CustomerStatus = CustomerStatus.ACTIVE) {
        // Arrange
        testingCustomer = TestFactories.makeCustomer().copy(status = customerStatus)
        every { customerRepository.getById(testingCustomer.id) } returns testingCustomer
        every {
            customerRepository.update(
                testingCustomer.id,
                any()
            )
        } just Runs
        every { encryptor.encrypt(testingCustomer.password) } returns testingCustomer.password
        every { encryptor.encrypt(newPassword) } returns newPassword

        newEmail = "new${testingCustomer.email}"
        newRoles = setOf(Role.CUSTOMER)
        newName = "New ${testingCustomer.name}"
        newStatus = CustomerStatus.ACTIVE
    }

    @Test
    fun `updates a customer when password is not modified`() {
        // Arrange
        setup()

        // Act
        sut.update(
            UpdateCustomer.Input(
                testingCustomer.id,
                password = null,
                email = newEmail,
                status = newStatus,
                name = newName,
                roles = newRoles
            )
        )

        // Assert
        verify(exactly = 1) {
            customerRepository.update(
                testingCustomer.id,
                testingCustomer.copy(email = newEmail, roles = newRoles, name = newName, status = newStatus)
            )
        }
        verify(exactly = 0) { encryptor.encrypt(any()) }
    }

    @Test
    fun `updates a customer when only password modified`() {
        // Arrange
        setup()

        // Act
        sut.update(
            UpdateCustomer.Input(
                testingCustomer.id,
                password = newPassword,
                email = null,
                status = null,
                name = null,
                roles = null
            )
        )

        // Assert
        verify(exactly = 1) {
            customerRepository.update(
                testingCustomer.id,
                testingCustomer.copy(password = newPassword)
            )
        }
        verify(exactly = 1) { encryptor.encrypt(newPassword) }
    }

    @Test
    fun `does not update a customer if it can't be found`() {
        // Arrange
        setup()
        every { customerRepository.getById(testingCustomer.id) } returns null

        // Act
        val error = assertThrows<ResourceNotFound> {
            sut.update(
                UpdateCustomer.Input(
                    testingCustomer.id,
                    password = null,
                    email = null,
                    status = null,
                    name = null,
                    roles = null
                )
            )
        }

        // Assert
        assertEquals(Errors.ML201.formatErrorCode(), error.code)
        verify(exactly = 0) { encryptor.encrypt(any()) }
        verify(exactly = 0) { customerRepository.update(any(), any()) }
    }

    @Test
    fun `does not update a customer if their status is not ACTIVE`() {
        // Arrange
        setup(CustomerStatus.INACTIVE)

        // Act
        val error = assertThrows<OperationNotAllowed> {
            sut.update(
                UpdateCustomer.Input(
                    testingCustomer.id,
                    password = null,
                    email = null,
                    status = null,
                    name = null,
                    roles = null
                )
            )
        }

        // Assert
        assertEquals(Errors.ML203.formatErrorCode(), error.code)
        verify(exactly = 0) { encryptor.encrypt(any()) }
        verify(exactly = 0) { customerRepository.update(any(), any()) }
    }
}