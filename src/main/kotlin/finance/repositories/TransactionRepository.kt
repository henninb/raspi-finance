package finance.repositories


import finance.models.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.util.*


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

    fun findByAccountNameOwnerAndClearedOrderByTransactionDateDesc(accountNameOwner: String, cleared: Int) : List<Transaction>
    fun findByAccountNameOwnerAndClearedOrderByTransactionDateAsc(accountNameOwner: String, cleared: Int) : List<Transaction>
    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDateDesc(accountNameOwner: String): List<Transaction>
    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDateAsc(accountNameOwner: String): List<Transaction>
    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(pageable : Pageable, accountNameOwner: String) : Page<Transaction>

    //TODO: add LIMIT 1
    fun findByGuid(guid: String): Optional<Transaction>

    // Using SpEL expression
    @Query("SELECT SUM(amount) as totalsCleared FROM #{#entityName} WHERE cleared = 1 AND accountNameOwner=?1")
    //@Query(value = "SELECT SUM(amount) AS totals t_transaction WHERE cleared = 1 AND account_name_owner=?1", nativeQuery = true)
    fun getTotalsByAccountNameOwnerCleared(accountNameOwner: String): Double

    // Using SpEL expression
    @Query("SELECT SUM(amount) as totals FROM #{#entityName} WHERE accountNameOwner=?1")
    fun getTotalsByAccountNameOwner(accountNameOwner: String): Double

    @Modifying
    @Transactional
    @Query(value = "DELETE from t_transaction WHERE guid = ?1", nativeQuery = true)
    fun deleteByGuid(guid: String)
}
