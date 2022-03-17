package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import java.math.BigDecimal
import javax.persistence.*

@Entity(name = "books")
data class BookRecord  (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Int? = null,

    @Column
    var name: String? = null,

    @Column
    var price: BigDecimal? = null,

    @Column
    var status: BookStatus? = null,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerRecord? = null,
) : JPARecord<Book> {
    companion object : EntityRecordMapper<Book, BookRecord> {
        override fun fromEntity(input: Book): BookRecord {
            return BookRecord(id = input.id, name = input.name, price = input.price, status = input.status)
        }
    }

    override fun toEntity(): Book {
        return Book(
            id = id!!,
            price = price!!,
            status = status!!,
            customer = customer!!.toEntity(),
            name = name!!
        )
    }
}
