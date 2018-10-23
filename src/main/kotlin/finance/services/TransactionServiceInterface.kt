package finance.services

import finance.models.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

//https://github.com/chathurangat/spring-boot-jpa-pagination-example

interface TransactionServiceInterface {
    fun create(transaction: Transaction): Transaction
    fun findTransactions(pageable: Pageable): Page<Transaction>
}
