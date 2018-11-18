package finance.services


import finance.models.Transaction
import finance.repositories.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.function.Consumer
import org.apache.catalina.Manager
import org.springframework.util.ClassUtils.isPresent
import com.sun.deploy.util.SearchPath.findOne
import java.util.*


@Service
open class TransactionService {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit private var transactionRepository: TransactionRepository<Transaction>

    //@Autowired(required=false)
    //internal var mongoTransactionRepository: MongoTransactionRepository? = null

    fun findAllTransactions(pageable: Pageable) : Page<Transaction> {
        //val pageable :Pageable = PageRequest(pageNumber, pageSize, Sort.unsorted())
        val transactions : Page<Transaction> = transactionRepository.findAll(pageable)
        return transactions

    }

    fun findAll(): List<Transaction> {
        val transactions = ArrayList<Transaction>()

        this.transactionRepository.findAll().forEach(Consumer<Transaction> { transactions.add(it) })
        if (transactions.isEmpty() == true ) {
            //TODO: failure
        }
        return transactions
    }

    fun findByAccountNameOwnerAndCleared( accountNameOwner: String, cleared: Int) : List<Transaction> {
        return this.transactionRepository.findByAccountNameOwnerAndClearedOrderByTransactionDateDesc(accountNameOwner, cleared)
    }

    //fun fetchAccoutTotals(accountNameOwner: String): Double {
    //    return transactionRepository!!.fetchAccoutTotals(accountNameOwner)
    //}

    fun deleteByGuid(guid: String) {
        try {
            transactionRepository.deleteByGuid(guid)
        } catch (ex: Exception) {
            LOGGER.info(ex.message)
        }
    }

//    //TODO: Debating on how to pass the updated fields to this method
//    fun updateTransaction(guid: String, list: Map<String, String>) {
//        val transaction: Optional<Transaction> = findByGuid(guid);
//
//        transaction.cleared = list["cleared"]!!.toInt()
//        transactionRepository.saveAndFlush(transaction)
//    }

    fun insertTransaction(transaction: Transaction) {
        val transactionId: Long = transaction.transactionId;
        val guid: String? = transaction.guid;
        if ( guid != null && guid != "" ) {
            var transactionFound = Transaction()
            try {
                transactionFound = transactionRepository.findByTransactionId(transactionId)
            } catch (ex: EmptyResultDataAccessException) {
                LOGGER.warn("Empty result set returned from query.")
            }
            if( transactionFound.transactionId == 0L ) {
                transactionRepository.saveAndFlush(transaction)
                LOGGER.info("transaction data inserted.")
            } else {
                LOGGER.info("duplicate found, no transaction data inserted.")
            }
        } else {
            LOGGER.warn("guid issues, no transaction data inserted.")
        }
    }

    fun findByTransactionId(transactionId : Long): Transaction {
        val transaction: Transaction = transactionRepository.findByTransactionId(transactionId)
        if( transaction.transactionId == 0L ) {
            //TODO: failure
        }
        return transaction
    }

    fun findByGuid(guid: String): Optional<Transaction> {
        val transaction: Optional<Transaction> = transactionRepository.findByGuid(guid)
        if( transaction.isPresent ) {
            //TODO: failure
        }
        return transaction
    }

    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner: String): List<Transaction> {
        val transactions: List<Transaction> = transactionRepository.findByAccountNameOwnerIgnoreCaseOrderByTransactionDateDesc(accountNameOwner)
        if( transactions.isEmpty() ) {
            LOGGER.error("an empty list of AccountNameOwner.")
            //TODO: failure
        }
        return transactions;
        //return transactionRepository.findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner)
    }

    fun patchTransaction( transaction: Transaction) {
        //val optionalManager = transactionRepository.findOne(transaction.guid)
        val optionalTransaction = transactionRepository.findByGuid(transaction.guid)
        if (optionalTransaction.isPresent()) {
            val fromDb = optionalTransaction.get()
            LOGGER.info("patchTransaction()=" + fromDb.guid)
            // bean utils will copy non null values from toBePatched to fromDb manager.
            //beanUtils.copyProperties(fromDb, toBePatched)
            //updateManager(fromDb)
        }
    }
}
