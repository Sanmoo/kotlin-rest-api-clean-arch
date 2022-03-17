package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.exceptions.OperationNotAllowed
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import com.mercadolivro.core.use_cases.ports.Encryptor
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CreateCustomerTest {
    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var passwordEncryptor: Encryptor

    @InjectMockKs
    private lateinit var createCustomer: CreateCustomer

    @Test
    fun `creates a customer`() {
        // Arrange
        val testingCustomer = makeCustomer()
        every {
            customerRepository.create(
                name = testingCustomer.name,
                email = testingCustomer.email,
                password = testingCustomer.password,
                roles = testingCustomer.roles,
                status = testingCustomer.status
            )
        } returns testingCustomer
        every { customerRepository.existsByEmail(testingCustomer.email) } returns false
        every { passwordEncryptor.encrypt(testingCustomer.password) } returns testingCustomer.password
        val expectedOutput = CreateCustomer.Output.fromCustomer(testingCustomer)

        // Act
        val creationOutput = createCustomer.createCustomer(
            CreateCustomer.Input(
                testingCustomer.name,
                testingCustomer.email,
                testingCustomer.password
            )
        )

        // Assert
        assertEquals(expectedOutput, creationOutput)
        assertEquals(expectedOutput.id, creationOutput.id)
        assertEquals(expectedOutput.email, creationOutput.email)
        assertEquals(expectedOutput.name, creationOutput.name)
        assertEquals(expectedOutput.status, creationOutput.status)
    }

    @Test
    fun `does not create a customer if e-mail is already taken`() {
        // Arrange
        val testingCustomer = makeCustomer()
        every { customerRepository.existsByEmail(testingCustomer.email) } returns true

        // Act And Assert
        val error = assertThrows<OperationNotAllowed> {
            createCustomer.createCustomer(
                CreateCustomer.Input(
                    testingCustomer.name,
                    testingCustomer.email,
                    testingCustomer.password
                )
            )
        }
        assertEquals(Errors.ML204.formatErrorCode(), error.code)
    }
}