package finance.repositories


import finance.models.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model


interface TransactionRepository<T : Transaction> : JpaRepository<T, Long> {
//interface TransactionRepository : JpaRepository<Transaction, Long> {

    //function name must match name on database table
    fun findByDescriptionIgnoreCase(description: String): Transaction

    override fun findAll(pageable: Pageable): Page<T>

    //@Query("SELECT * FROM #{#entityName} WHERE cleared = 1")
    fun findAllOrderByTransactionId(pageable : Pageable) : Page<Transaction>
    //fun getAllTransactionsCleared(pageable : Pageable) : Page<Transaction>

    //TODO: add LIMIT 1
    fun findByTransactionId(transactionId: Long?): Transaction
    //fun findByTransactionId(transactionId: Long?): Pagable<Transaction>

    fun findByAccountNameOwnerAndClearedOrderByTransactionDate(accountNameOwner: String, cleared: Int) : List<Transaction>
    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner: String): List<Transaction>

    //TODO: add LIMIT 1
    fun findByGuid(guid: String): Transaction

    // Using SpEL expression
    @Query("SELECT SUM(amount) FROM #{#entityName} WHERE cleared = 1 AND accountNameOwner=?1")
    fun getTotalsByAccountNameOwner(accountNameOwner: String): Double

    // Using SpEL expression
    @Query("SELECT SUM(amount) AS accountTotal FROM #{#entityName} WHERE cleared=1 AND accountNameOwner=?1")
    fun fetchAccoutClearedTotals(accountNameOwner: String): Double

    @Modifying
    @Transactional
    @Query(value = "DELETE from t_transaction WHERE guid = ?1", nativeQuery = true)
    fun deleteByGuid(guid: String)
}
