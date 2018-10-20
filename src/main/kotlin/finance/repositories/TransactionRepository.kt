package finance.repositories


import finance.models.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model


//interface TransactionRepository<T : Transaction> : JpaRepository<T, Long>
interface TransactionRepository : JpaRepository<Transaction, Long> {

    //function name must match name on database table
    fun findByDescriptionIgnoreCase(description: String): Transaction

    fun findByTransactionId(transactionId: Long?): Transaction

    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner: String): List<Transaction>

    fun findByGuid(guid: String): Transaction

    // Using SpEL expression
    @Query("SELECT SUM(amount) FROM #{#entityName} WHERE isCleared = 1 AND accountNameOwner=?1")
    fun fetchAccoutTotals(accountNameOwner: String): Double

    // Using SpEL expression
    @Query("SELECT SUM(amount) AS accountTotal FROM #{#entityName} WHERE isCleared=1 AND accountNameOwner=?1")
    fun fetchAccoutClearedTotals(accountNameOwner: String): Double


    @Modifying
    @Transactional
    @Query(value = "DELETE from t_transaction WHERE guid = ?1", nativeQuery = true)
    fun deleteByGuid(guid: String)
}
