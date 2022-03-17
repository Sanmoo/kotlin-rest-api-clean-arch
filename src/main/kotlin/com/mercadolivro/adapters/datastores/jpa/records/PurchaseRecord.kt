package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.Purchase
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "purchase")
data class PurchaseRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private var customer: CustomerRecord? = null,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "purchase_book",
        joinColumns = [JoinColumn(name = "purchase_id")],
        inverseJoinColumns = [JoinColumn(name = "book_id")]
    )
    private var books: List<BookRecord> = listOf(),

    @Column
    private var nfe: String? = null,

    @Column
    private var price: BigDecimal? = null,

    @Column(name = "created_at")
    private var createdAt: LocalDateTime? = null
) : JPARecord<Purchase> {
    companion object : EntityRecordMapper<Purchase, PurchaseRecord> {
        override fun fromEntity(input: Purchase): PurchaseRecord {
            return PurchaseRecord(
                id = input.id,
                createdAt = input.createdAt,
                price = input.price,
                customer = CustomerRecord(id = input.customerId),
                books = input.bookIds.map { BookRecord(id = it) },
                nfe = input.nfe
            )
        }
    }

    override fun toEntity(): Purchase {
        return Purchase(
            id = id,
            bookIds = books.map { it.id }.toSet(),
            customerId = customer!!.id!!,
            price = price!!,
            createdAt = createdAt!!,
            nfe = nfe
        )
    }
}
