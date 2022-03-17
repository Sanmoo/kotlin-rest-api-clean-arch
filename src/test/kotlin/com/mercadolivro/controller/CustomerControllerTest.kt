package com.mercadolivro.controller

import au.com.origin.snapshots.Expect
import au.com.origin.snapshots.junit5.SnapshotExtension
import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.controller.dto.PostCustomerRequest
import com.mercadolivro.controller.dto.UpdateCustomerRequest
import com.mercadolivro.controller.support.CustomerSpringUserDetails
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.Role
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.ports.TestFactories.Companion.makePaginatedResult
import com.mercadolivro.core.use_cases.*
import com.mercadolivro.core.use_cases.ports.PaginationData
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@WithMockUser
@ExtendWith(SnapshotExtension::class)
internal class CustomerControllerTest {
    private lateinit var adminDetails: CustomerSpringUserDetails
    private lateinit var userDetails: CustomerSpringUserDetails
    private lateinit var testingCustomer2: Customer
    private lateinit var testingCustomer1: Customer
    private lateinit var paginationData: PaginationData

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var createCustomer: CreateCustomer

    @MockkBean
    private lateinit var listCustomers: ListCustomers

    @MockkBean
    private lateinit var getCustomerDetails: GetCustomerDetails

    @MockkBean
    private lateinit var updateCustomer: UpdateCustomer

    @MockkBean
    private lateinit var destroyCustomer: DestroyCustomer

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var expect: Expect

    @BeforeEach
    private fun setup() {
        paginationData = PaginationData(10, 0)
        testingCustomer1 = makeCustomer(1)
        testingCustomer2 = makeCustomer(2)
        val admin = makeCustomer(3).copy(roles = setOf(Role.ADMIN))
        userDetails = CustomerSpringUserDetails(GetCustomerDetails.Output.fromCustomerEntity(testingCustomer1))
        adminDetails = CustomerSpringUserDetails(GetCustomerDetails.Output.fromCustomerEntity(admin))
    }

    @Test
    fun `returns all customers with a given name`() {
        // Arrange
        mockListCustomersCall("Fulano")

        // Act & Assert
        val result = mockMvc.perform(get("/customers?name=Fulano"))
            .andExpect(status().isOk)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    @Test
    fun `returns all customers`() {
        // Arrange
        mockListCustomersCall()

        // Act & Assert
        val result = mockMvc.perform(get("/customers"))
            .andExpect(status().isOk)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    private fun mockListCustomersCall(inputName: String? = null) {
        every {
            listCustomers.list(
                ListCustomers.Input(
                    inputName,
                    paginationData
                )
            )
        } returns ListCustomers.Output(
            list = makePaginatedResult(
                paginationData,
                CreateCustomer.Output.fromCustomer(testingCustomer1),
                CreateCustomer.Output.fromCustomer(testingCustomer2)
            )
        )
    }

    @Test
    fun `gets customer details when authorized`() {
        // Arrange
        mockGetCustomerDetails()

        // Act & Assert
        val result = mockMvc.perform(get("/customers/${testingCustomer1.id}").with(user(userDetails)))
            .andExpect(status().isOk)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    @Test
    fun `gets customer details when user is admin`() {
        // Arrange
        mockGetCustomerDetails()

        // Act & Assert
        val result = mockMvc.perform(get("/customers/${testingCustomer2.id}").with(user(adminDetails)))
            .andExpect(status().isOk)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    @Test
    fun `does not get customer details when not authorized`() {
        // Arrange
        mockGetCustomerDetails()

        // Act & Assert
        mockMvc.perform(get("/customers/${testingCustomer2.id}").with(user(userDetails)))
            .andExpect(status().isForbidden)
            .andReturn()
    }

    @Test
    fun `updates a user name successfully`() {
        // Arrange
        val expectedUpdateCustomerInput = UpdateCustomer.Input(
            id = testingCustomer1.id,
            name = "Novo Nome",
            null,
            null,
            null,
            null
        )
        every { updateCustomer.update(expectedUpdateCustomerInput) } just Runs
        val updateRequest = UpdateCustomerRequest(name = "Novo Nome")
        val request =
            put("/customers/${testingCustomer1.id}").with(user(userDetails)).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))

        // Act & Assert
        mockMvc.perform(request)
            .andExpect(status().isNoContent)
            .andReturn()

        verify(exactly = 1) { updateCustomer.update(expectedUpdateCustomerInput) }
    }

    @Test
    fun `updates a user e-mail successfully`() {
        // Arrange
        val expectedUpdateCustomerInput = UpdateCustomer.Input(
            id = testingCustomer1.id,
            name = null,
            "newemail@test.com",
            null,
            null,
            null
        )
        every { updateCustomer.update(expectedUpdateCustomerInput) } just Runs
        val updateRequest = UpdateCustomerRequest(email = "newemail@test.com")
        val request =
            put("/customers/${testingCustomer1.id}").with(user(userDetails)).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))

        // Act & Assert
        mockMvc.perform(request)
            .andExpect(status().isNoContent)
            .andReturn()

        verify(exactly = 1) { updateCustomer.update(expectedUpdateCustomerInput) }
    }

    @Test
    fun `destroys a customer`() {
        // Arrange
        val expectedInput = DestroyCustomer.Input(testingCustomer1.id)
        every { destroyCustomer.destroy(expectedInput) } just Runs
        val request = delete("/customers/${testingCustomer1.id}").with(user(userDetails))

        // Act & Assert
        mockMvc.perform(request)
            .andExpect(status().isNoContent)
            .andReturn()
        verify(exactly = 1) { destroyCustomer.destroy(expectedInput) }
    }

    @Test
    fun `creates a user if request is valid`() {
        // Arrange
        every {
            createCustomer.createCustomer(
                CreateCustomer.Input(
                    name = testingCustomer1.name,
                    email = testingCustomer1.email,
                    password = testingCustomer1.password
                )
            )
        } returns CreateCustomer.Output.fromCustomer(testingCustomer1)
        val createRequest = PostCustomerRequest(
            name = testingCustomer1.name,
            email = testingCustomer1.email,
            password = testingCustomer1.password
        )
        val request = post("/customers").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest))

        // Act
        val result = mockMvc.perform(request)
            .andExpect(status().isCreated)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    @Test
    fun `responds properly when asked to create a customer with invalid request`() {
        // Arrange
        val createRequest = PostCustomerRequest(
            name = "",
            email = testingCustomer1.email,
            password = testingCustomer1.password
        )
        val request = post("/customers").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest))

        // Act & Assert
        mockMvc.perform(request)
            .andExpect(status().isBadRequest)
            .andReturn()

        // Assert
        verify (exactly = 0) { createCustomer.createCustomer(any()) }
    }

    private fun mockGetCustomerDetails() {
        every {
            getCustomerDetails.detailById(testingCustomer1.id)
        } returns GetCustomerDetails.Output.fromCustomerEntity(testingCustomer1)
        every {
            getCustomerDetails.detailById(testingCustomer2.id)
        } returns GetCustomerDetails.Output.fromCustomerEntity(testingCustomer2)
    }
}