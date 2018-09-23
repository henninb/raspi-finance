package finance.repositories;

import finance.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import finance.model.Transaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //function name must match name on database table
    Transaction findByDescriptionIgnoreCase(String description);
    public Transaction findByTransactionId(Long transactionId);

    //public List<Transaction> findByAccountNameOwner(String accountNameOwner);
    //public List<Transaction> findByAccountNameOwnerIgnoreCase(String accountNameOwner);
    public List<Transaction> findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(String accountNameOwner);

    public Transaction findByGuid(String guid);

    // Using SpEL expression
    @Query("SELECT SUM(amount) FROM #{#entityName} WHERE isCleared = 1 AND accountNameOwner=?1")
    //public double find(@Param("accountNameOwner") String accountNameOwner);
    public double fetchAccoutTotals(String accountNameOwner);

    // Using SpEL expression
    @Query("SELECT SUM(amount) AS accountTotal FROM #{#entityName} WHERE isCleared=1 AND accountNameOwner=?1")
    //public double find(@Param("accountNameOwner") String accountNameOwner);
    public double fetchAccoutClearedTotals(String accountNameOwner);

    //@NamedNativeQuery(name="showSummaryResults", query="SELECT  account_name_owner, SUM(amount) AS totals FROM t_transaction GROUP BY account_name_owner ORDER BY account_name_owner", resultSetMapping="showSummaryResults")
    @Query(value = "SELECT  account_name_owner as accountNameOwner, SUM(amount) AS totals FROM t_transaction GROUP BY account_name_owner ORDER BY account_name_owner", nativeQuery = true)
    List<Summary> getSummary();

    @Modifying
    @Transactional
    @Query(value = "DELETE from t_transaction WHERE guid = ?1", nativeQuery = true)
    void deleteByGuid(String guid);

    //@NamedNativeQuery(name="summaryResults", query="SELECT accountNameOwner, SUM(amount) AS totals FROM #{#entityName} GROUP BY accountNameOwner ORDER BY accountNameOwner", resultSetMapping="summaryResults")
    //public List<Summary> fetchSummary();

    // Using SpEL expressions
    //@Query("SELECT accountNameOwner, SUM(amount) AS totals FROM #{#entityName} GROUP BY accountNameOwner ORDER BY accountNameOwner")
    //public List<Summary> fetchSummary();

    //@NamedQuery(name="findWhatever", query="SELECT new path.to.dto.MyDto(e.id, e.otherProperty) FROM Student e WHERE e.id = ?1")
}
