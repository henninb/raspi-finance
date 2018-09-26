package finance.repositories


import finance.models.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface TransactionRepository : JpaRepository<Transaction, Long> {

    //@NamedNativeQuery(name="showSummaryResults", query="SELECT  account_name_owner, SUM(amount) AS totals FROM t_transaction GROUP BY account_name_owner ORDER BY account_name_owner", resultSetMapping="showSummaryResults")
    //@get:Query(value = "SELECT  account_name_owner as accountNameOwner, SUM(amount) AS totals FROM t_transaction GROUP BY account_name_owner ORDER BY account_name_owner", nativeQuery = true)
    //val summary: List<Summary>

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
