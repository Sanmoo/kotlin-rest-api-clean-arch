package com.mercadolivro.controller

import com.mercadolivro.controller.dto.BookDTO
import com.mercadolivro.controller.dto.PartialBookDTO
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.use_cases.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

fun ListBooks.Output.listToEntities(): List<BookDTO> {
    return list.map {
        BookDTO(id = it.id, name = it.name, price = it.price, status = it.status, customerId = it.customerId)
    }
}

@RestController
@RequestMapping("books")
class BookController(
    private val createBook: CreateBook,
    private val getBookDetails: GetBookDetails,
    private val listBooks: ListBooks,
    private val updateBook: UpdateBook,
    private val destroyBook: DestroyBook,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody book: BookDTO) =
        createBook.create(CreateBook.Input(name = book.name, price = book.price, customerId = book.customerId)).let {
            BookDTO(
                id = it.id,
                status = it.status,
                customerId = book.customerId,
                name = book.name,
                price = book.price
            )
        }

    @GetMapping("{id}")
    fun get(@PathVariable id: Int): BookDTO {
        val book = getBookDetails.detail(GetBookDetails.Input(id)) ?: throw Exception("Not found")
        return BookDTO(
            id = id,
            price = book.price,
            name = book.name,
            status = book.status,
            customerId = book.customerId
        )
    }

    @GetMapping
    fun listAll(@RequestParam name: String?): List<BookDTO> =
        listBooks.list(ListBooks.Input(name)).listToEntities()

    @GetMapping("active")
    fun listAllActive(): List<BookDTO> =
        listBooks.list(ListBooks.Input(status = BookStatus.ACTIVE)).listToEntities()

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @Valid @RequestBody bookDto: PartialBookDTO) {
        updateBook.update(UpdateBook.Input(
            id = id,
            price = bookDto.price,
            status = bookDto.status,
            customerId = bookDto.customerId,
            name = bookDto.name
        ))
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun destroy(@PathVariable id: Int) = destroyBook.destroy(DestroyBook.Input(id))
}