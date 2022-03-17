package com.mercadolivro.controller

import com.mercadolivro.controller.dto.BookDTO
import com.mercadolivro.core.use_cases.CreateBook
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("books")
class BookController(
    private val createBook: CreateBook,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody book: BookDTO): BookDTO {
        val output = createBook.create(CreateBook.Input(name = book.name, price = book.price, customerId = book.customerId))
        return BookDTO(
            id = output.id,
            status = output.status,
            customerId = book.customerId,
            name = book.name,
            price = book.price
        )
    }
}
