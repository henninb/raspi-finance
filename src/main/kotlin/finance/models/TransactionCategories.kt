package finance.models

import javax.persistence.*

@Entity(name = "TransactionCategoriesEntity")
@Table(name = "t_transaction_categories")
class TransactionCategories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var transactionCategoriesId: Long = 0L
    var categoryId: Long = 0L
    var transactionId: Long = 0L
}
