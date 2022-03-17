package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.Purchase
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "purchase")
data class PurchaseRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: CustomerRecord? = null,

    @ManyToMany
    @JoinTable(
        name = "purchase_book",
        joinColumns = [JoinColumn(name = "purchase_id")],
        inverseJoinColumns = [JoinColumn(name = "book_id")]
    )
    val books: List<BookRecord>? = null,

    @Column
    val nfe: String? = null,

    @Column
    val price: BigDecimal? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null
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
            id = id ?: -1,
            bookIds = books?.map { it.id!! }?.toSet() ?: setOf(),
            customerId = customer!!.id!!,
            price = price!!,
            createdAt = createdAt!!,
            nfe = nfe
        )
    }
}
