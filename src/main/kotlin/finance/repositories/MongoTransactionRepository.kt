package finance.repositories

import finance.models.Transaction
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

//@ConditionalOnExpression("\${project.mongo.feature:true}")
interface MongoTransactionRepository<T : Transaction> : MongoRepository<T, Long> {
    fun findByDescriptionIgnoreCase(description: String): Optional<Transaction>
    override fun findAll(pageable: Pageable): Page<T>
    fun findAllOrderByTransactionId(pageable : Pageable) : Page<Transaction>
    fun findByTransactionId(transactionId: Long?): Transaction
    fun findByAccountNameOwnerAndClearedOrderByTransactionDateDesc(accountNameOwner: String, cleared: Int) : List<Transaction>
    fun findByAccountNameOwnerAndClearedOrderByTransactionDateAsc(accountNameOwner: String, cleared: Int) : List<Transaction>
    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDateDesc(accountNameOwner: String): List<Transaction>
    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDateAsc(accountNameOwner: String): List<Transaction>
    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(pageable : Pageable, accountNameOwner: String) : Page<Transaction>
    fun findByGuid(guid: String): Optional<Transaction>
    fun deleteByGuid(guid: String)
}
