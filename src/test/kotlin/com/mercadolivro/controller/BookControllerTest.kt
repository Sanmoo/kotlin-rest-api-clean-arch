package com.mercadolivro.controller

import au.com.origin.snapshots.Expect
import au.com.origin.snapshots.junit5.SnapshotExtension
import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.controller.dto.PostBookRequest
import com.mercadolivro.controller.dto.PutBookRequest
import com.mercadolivro.controller.support.CustomerSpringUserDetails
import com.mercadolivro.core.entities.*
import com.mercadolivro.core.entities.TestFactories.Companion.makeBook
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.ports.TestFactories.Companion.makePaginatedResult
import com.mercadolivro.core.use_cases.*
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.exceptions.OperationNotAllowed
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
internal class BookControllerTest {
    private lateinit var testingBook2: Book
    private lateinit var testingBook1: Book
    private lateinit var adminDetails: CustomerSpringUserDetails
    private lateinit var userDetails: CustomerSpringUserDetails
    private lateinit var testingCustomer2: Customer
    private lateinit var testingCustomer1: Customer
    private lateinit var paginationData: PaginationData

    @MockkBean
    private lateinit var createBook: CreateBook

    @MockkBean
    private lateinit var getBookDetails: GetBookDetails

    @MockkBean
    private lateinit var listBooks: ListBooks

    @MockkBean
    private lateinit var updateBook: UpdateBook

    @MockkBean
    private lateinit var destroyBook: DestroyBook

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var expect: Expect

    @BeforeEach
    private fun setup() {
        paginationData = PaginationData(10, 0)
        testingCustomer1 = makeCustomer(1)
        testingCustomer2 = makeCustomer(2)
        testingBook1 = makeBook(1).copy(customer = testingCustomer1)
        testingBook2 = makeBook(2)
        val admin = makeCustomer(3).copy(roles = setOf(Role.ADMIN))
        userDetails = CustomerSpringUserDetails(GetCustomerDetails.Output.fromCustomerEntity(testingCustomer1))
        adminDetails = CustomerSpringUserDetails(GetCustomerDetails.Output.fromCustomerEntity(admin))
    }

    @Test
    fun `creates a book with an associated customer`() {
        // Arrange
        every {
            createBook.create(
                CreateBook.Input(
                    name = testingBook1.name,
                    price = testingBook1.price,
                    customerId = testingBook1.customer!!.id
                )
            )
        } returns CreateBook.Output.fromEntity(testingBook1)
        val createRequest = PostBookRequest(
            name = testingBook1.name,
            customerId = testingBook1.customer!!.id,
            price = testingBook1.price
        )
        val request = MockMvcRequestBuilders.post("/books").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest))

        // Act
        val result = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    @Test
    fun `creates a book without an associated customer`() {
        // Arrange
        every {
            createBook.create(
                CreateBook.Input(
                    name = testingBook1.name,
                    price = testingBook1.price,
                    customerId = null
                )
            )
        } returns CreateBook.Output.fromEntity(testingBook1)
        val createRequest = PostBookRequest(
            name = testingBook1.name,
            price = testingBook1.price,
            customerId = null
        )
        val request = MockMvcRequestBuilders.post("/books").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest))

        // Act
        val result = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    @Test
    fun `gets a book`() {
        // Arrange
        every { getBookDetails.detail(GetBookDetails.Input(testingBook1.id)) } returns GetBookDetails.Output.fromBookEntity(
            testingBook1
        )
        val request = MockMvcRequestBuilders.get("/books/${testingBook1.id}")

        // Act & Assert
        val result = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    @Test
    fun `lists all books`() {
        // Arrange
        every { listBooks.list(ListBooks.Input(paginationData = paginationData)) } returns ListBooks.Output(
            result = makePaginatedResult(
                paginationData,
                CreateBook.Output.fromEntity(testingBook1),
                CreateBook.Output.fromEntity(testingBook2)
            )
        )
        val request = MockMvcRequestBuilders.get("/books")

        // Act & Assert
        val result = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    @Test
    fun `lists all books for a given name`() {
        // Arrange
        every {
            listBooks.list(
                ListBooks.Input(
                    paginationData = paginationData,
                    name = testingBook2.name
                )
            )
        } returns ListBooks.Output(
            result = makePaginatedResult(
                paginationData,
                CreateBook.Output.fromEntity(testingBook2)
            )
        )
        val request = MockMvcRequestBuilders.get("/books?name=${testingBook2.name}")

        // Act & Assert
        val result = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    @Test
    fun `lists all active books`() {
        // Arrange
        every {
            listBooks.list(
                ListBooks.Input(
                    paginationData = paginationData,
                    status = BookStatus.ACTIVE
                )
            )
        } returns ListBooks.Output(
            result = makePaginatedResult(
                paginationData,
                CreateBook.Output.fromEntity(testingBook1),
                CreateBook.Output.fromEntity(testingBook2)
            )
        )
        val request = MockMvcRequestBuilders.get("/books/active")

        // Act & Assert
        val result = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // Assert
        expect.toMatchSnapshot(result.response.contentAsString)
    }

    @Test
    fun `update a book properties`() {
        // Arrange
        val expectedUpdateInput = UpdateBook.Input(
            id = testingBook1.id,
            name = testingBook2.name,
            price = testingBook2.price,
            customerId = testingBook1.customer!!.id,
            status = BookStatus.ACTIVE
        )
        every { updateBook.update(expectedUpdateInput) } just Runs
        val updateRequest = PutBookRequest(
            name = testingBook1.name,
            customerId = testingBook1.customer!!.id,
            price = testingBook1.price,
            status = BookStatus.ACTIVE
        )
        val request = MockMvcRequestBuilders.patch("/books/${testingBook1.id}").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest))

        // Act && Assert
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andReturn()

        // Assert
        verify(exactly = 1) { updateBook.update(expectedUpdateInput) }
    }

    @Test
    fun `update a book related customer`() {
        // Arrange
        val expectedUpdateInput = UpdateBook.Input(
            id = testingBook1.id,
            name = null,
            price = null,
            customerId = null,
            status = null
        )
        every { updateBook.update(expectedUpdateInput) } just Runs
        val updateRequest = PutBookRequest(
            name = null,
            customerId = null,
            price = null,
            status = null
        )
        val request = MockMvcRequestBuilders.patch("/books/${testingBook1.id}").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest))

        // Act && Assert
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andReturn()

        // Assert
        verify(exactly = 1) { updateBook.update(expectedUpdateInput) }
    }

    @Test
    fun `destroys a book`() {
        // Arrange
        every { destroyBook.destroy(DestroyBook.Input(1)) } just Runs
        val request = MockMvcRequestBuilders.delete("/books/1")

        // Act && Assert
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andReturn()
    }

    @Test
    fun `returns unprocessable entity if destroy use case throws specific exception`() {
        // Arrange
        every { destroyBook.destroy(DestroyBook.Input(1)) } throws Errors.ML001.toOperationNotAllowed()
        val request = MockMvcRequestBuilders.delete("/books/1")

        // Act && Assert
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
            .andReturn()
    }

    @Test
    fun `returns not found if destroy use case throws specific exception`() {
        // Arrange
        every { destroyBook.destroy(DestroyBook.Input(1)) } throws Errors.ML001.toResourceNotFoundException()
        val request = MockMvcRequestBuilders.delete("/books/1")

        // Act && Assert
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn()
    }
}