package com.mercadolivro.controller

import au.com.origin.snapshots.junit5.SnapshotExtension
import com.mercadolivro.controller.support.CustomerSpringUserDetails
import com.mercadolivro.core.entities.Role
import com.mercadolivro.core.entities.TestFactories
import com.mercadolivro.core.use_cases.GetCustomerDetails
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@WithMockUser
@ExtendWith(SnapshotExtension::class)
internal class AdminControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `extracts a report`() {
        // Assert
        val admin = TestFactories.makeCustomer(3).copy(roles = setOf(Role.ADMIN))
        val adminDetails = CustomerSpringUserDetails(GetCustomerDetails.Output.fromCustomerEntity(admin))
        val request = MockMvcRequestBuilders.get("/admin/report").with(SecurityMockMvcRequestPostProcessors.user(adminDetails))

        // Act
        val result = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // Assert
        assertEquals("This is a Report. Only Admin can see it!", result.response.contentAsString)
    }

    @Test
    fun `does not generate a report if user is not admin`() {
        // Assert
        val request = MockMvcRequestBuilders.get("/admin/report")

        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isForbidden)
            .andReturn()
    }
}