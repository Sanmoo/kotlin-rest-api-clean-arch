package com.mercadolivro.controller

import au.com.origin.snapshots.Expect
import au.com.origin.snapshots.junit5.SnapshotExtension
import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.controller.dto.PostPurchaseRequest
import com.mercadolivro.core.entities.TestFactories.Companion.makeBook
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.entities.TestFactories.Companion.makePurchase
import com.mercadolivro.core.use_cases.Purchase
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
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
internal class PurchaseControllerTest {
    @MockkBean
    private lateinit var purchase: Purchase

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var expect: Expect

    @Test
    fun `it purchases a book`() {
        // Arrange
        val customer = makeCustomer(1)
        val book1 = makeBook(1)
        val book2 = makeBook(2)
        val bookIds = setOf(book1.id, book2.id)
        val purchaseEntity = makePurchase(1).copy(customerId = customer.id, bookIds = bookIds)
        every {
            purchase.purchase(
                Purchase.Input(customerId = customer.id, bookIds = bookIds)
            )
        } returns Purchase.Output.fromEntities(purchaseEntity, customer, listOf(book1, book2))
        val purchaseRequest = PostPurchaseRequest(
            customerId = customer.id,
            bookIds = bookIds
        )
        val request = MockMvcRequestBuilders.post("/purchases").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(purchaseRequest))

        // Act
        val result = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }
}