package finance.dao

import finance.models.TransactionMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import javax.transaction.Transaction
import javax.transaction.Transactional


@Component
class TransactionDAO {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate;

    private val SQL_FIND_TRANSACTION = "select * from t_transaction where transaction_id = ?"
    private val SQL_FIND_TRANSACTION_BY_ACCOUNT_NAME_OWNER = "SELECT * FROM t_transaction WHERE account_name_owner = ?"

    //fun getTraansactionById(transaction_id: Long?): Transaction {
        //val transaction: Transaction? = jdbcTemplate.queryForObject(SQL_FIND_TRANSACTION, arrayOf<Any>(transaction_id), TransactionMapper());

        //jdbcTemplate.
        //return Transaction()
    //}

    //fun getTransactionByAccountNameOwner(accountNameOwner: String): List<Transaction> {
        //return jdbcTemplate.query(SQL_FIND_TRANSACTION_BY_ACCOUNT_NAME_OWNER, arrayOf<Any>(accountNameOwner), TransactionMapper())
    //}

    fun deleteTransactionByGuid(guid: String): Boolean? {
        //jdbcTemplate.query(SQL_DELETE_TRANSACTION);

        return true
    }

//https://hackernoon.com/spring-5-jdbc-support-for-kotlin-7cc31f4db4a5

    val splitUpNames = listOf("John Woo", "Jeff Dean", "Josh Block", "Josh Long")
            .map { name -> name.split(" ") }
            .map { split -> split[0] to split[1] }



    //@Transactional
    //fun findAll(): List<Transaction> {
        //return jdbcTemplate.query("select * from t_transaction", TransactionMapper())
    //}
}
