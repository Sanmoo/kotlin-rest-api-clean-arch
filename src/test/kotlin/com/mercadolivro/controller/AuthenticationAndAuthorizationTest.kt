package com.mercadolivro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.controller.dto.LoginRequest
import com.mercadolivro.controller.support.JWTUtil
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.use_cases.GetCustomerDetails
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
internal class AuthenticationAndAuthorizationTest {
    private lateinit var customer2: Customer
    private lateinit var customer: Customer

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var getCustomerDetails: GetCustomerDetails

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var jwtUtil: JWTUtil

    @BeforeEach
    fun setup() {
        val encodedPassword = passwordEncoder.encode("1234")
        customer = makeCustomer(1).copy(password = encodedPassword)
        customer2 = makeCustomer(2).copy(password = encodedPassword)
        every { getCustomerDetails.detailByEmail(customer.email) } returns GetCustomerDetails.Output.fromCustomerEntity(
            customer
        )
        every { getCustomerDetails.detailById(customer.id) } returns GetCustomerDetails.Output.fromCustomerEntity(
            customer
        )
        every { getCustomerDetails.detailByEmail(customer2.email) } returns GetCustomerDetails.Output.fromCustomerEntity(
            customer2
        )
        every { getCustomerDetails.detailById(customer2.id) } returns GetCustomerDetails.Output.fromCustomerEntity(
            customer2
        )
    }

    @Test
    fun `authenticates a user`() {
        // Act
        val loginRequest = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(LoginRequest(email = customer.email, password = "1234")))

        // Act
        val result = mockMvc.perform(loginRequest)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertTrue(result.response.headerNames.contains("Authorization"))
        assertTrue(result.response.getHeader("Authorization")!!.startsWith("Bearer "))
    }

    @Test
    fun `validates a valid user token`() {
        // Arrange
        val token = jwtUtil.generateToken(customer.id.toString())

        // Act
        val customerRequest = MockMvcRequestBuilders.get("/customers/${customer.id}").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer $token")

        // Act
        mockMvc.perform(customerRequest)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }

    @Test
    fun `validates an invalid user token`() {
        // Arrange
        val token = jwtUtil.generateToken(customer2.id.toString());

        // Act
        val customerRequest = MockMvcRequestBuilders.get("/customers/${customer.id}").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer $token")

        // Act
        mockMvc.perform(customerRequest)
            .andExpect(MockMvcResultMatchers.status().isForbidden)
            .andReturn()
    }

    @Test
    fun `validates correct authentication header value`() {
        // Arrange
        val token = jwtUtil.generateToken(null);

        // Act
        val customerRequest = MockMvcRequestBuilders.get("/customers/${customer.id}").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "TBearer $token")

        // Act
        mockMvc.perform(customerRequest)
            .andExpect(MockMvcResultMatchers.status().isForbidden)
            .andReturn()
    }
}
