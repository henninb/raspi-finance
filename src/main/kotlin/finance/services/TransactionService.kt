package finance.services


import finance.models.Transaction
import finance.repositories.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.ArrayList
import java.util.function.Consumer

@Service
class TransactionService {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    internal var transactionRepository: TransactionRepository? = null

    fun findAll(): List<Transaction> {
        val transactions = ArrayList<Transaction>()
        this.transactionRepository!!.findAll().forEach(Consumer<Transaction> { transactions.add(it) }) //fun with Java 8
        return transactions
    }

    fun fetchAccoutTotals(accountNameOwner: String): Double {
        return transactionRepository!!.fetchAccoutTotals(accountNameOwner)
    }

    fun deleteByGuid(guid: String) {
        try {
            transactionRepository!!.deleteByGuid(guid)
        } catch (ex: Exception) {
            println(ex)
        }
    }

    fun insertTransaction(transaction: Transaction) {
        val result = transactionRepository!!.saveAndFlush(transaction)
        if (transaction.guid == result.guid) {
            LOGGER.info("INFO: transactionRepository.saveAndFlush success.")
        } else {
            LOGGER.info("WARN: transactionRepository.saveAndFlush failure.")
        }
    }

    fun findByGuid(guid: String): Transaction {
        return transactionRepository!!.findByGuid(guid)
    }

    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner: String): List<Transaction> {
        return transactionRepository!!.findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner)
    }

    //TODO: fixme
    //@Transactional
    //fun save(transaction: Transaction): Transaction {
    //    return transactionRepository!!.save(transaction)
    //}
}
