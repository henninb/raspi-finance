package finance.services

import finance.models.Account
import finance.models.Category
import finance.models.Transaction
import finance.pojos.Totals
import finance.repositories.AccountRepository
import finance.repositories.CategoryRepository
import finance.repositories.TransactionJdbcTemplateRepository
import finance.repositories.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import java.util.function.Consumer

@Service
open class TransactionService {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var transactionRepository: TransactionRepository<Transaction>

    @Autowired
    lateinit var accountRepository: AccountRepository<Account>

    @Autowired
    lateinit var categoryRepository: CategoryRepository<Category>

    @Autowired
    lateinit var transactionJdbcTemplateRepository: TransactionJdbcTemplateRepository;

    fun findAllTransactions(pageable: Pageable) : Page<Transaction> {
        val transactions : Page<Transaction> = transactionRepository.findAll(pageable)

        return transactions
    }

    fun findTransactionsByAccountNameOwnerPageable(pageable: Pageable, accountNameOwner: String) : Page<Transaction> {
        val transactions : Page<Transaction> = transactionRepository.findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(pageable, accountNameOwner)

        return transactions
    }

    fun findAll(): List<Transaction> {
        val transactions = ArrayList<Transaction>()

        this.transactionRepository.findAll().forEach(
            Consumer<Transaction> {
            transactions.add(it)
            }
        )
        if (transactions.isEmpty() == true ) {
            //TODO: this failure should already be handled.
        }
        return transactions
    }

    fun findByAccountNameOwnerAndCleared( accountNameOwner: String, cleared: Int) : List<Transaction> {
        val transactions = ArrayList<Transaction>()

        this.transactionRepository.findByAccountNameOwnerAndClearedOrderByTransactionDateDesc(accountNameOwner, cleared).forEach {
            Consumer<Transaction> {
                transactions.add(it)
            }
        }
        return transactions;
        //return this.transactionRepository.findByAccountNameOwnerAndClearedOrderByTransactionDateDesc(accountNameOwner, cleared)
    }

    fun deleteByGuid(guid: String): Int {
        val transactionOptional: Optional<Transaction> = transactionRepository.findByGuid(guid)
        if( transactionOptional.isPresent) {
            transactionRepository.deleteByGuid(guid)
            return 0
        }
        return 1
    }

    fun insertTransaction(transaction: Transaction): Boolean {
        val transactionOptional: Optional<Transaction> = transactionRepository.findByGuid(transaction.guid)
        if( transactionOptional.isPresent ) {
            LOGGER.info("duplicate found, no transaction data inserted.")
            return false
        }
        val accountOptional: Optional<Account> = accountRepository.findByAccountNameOwner(transaction.accountNameOwner.toString())
        if (accountOptional.isPresent) {
            val account = accountOptional.get()
            transaction.accountId = account.accountId
            LOGGER.info("accountOptional isPresent.")
            val optionalCategory: Optional<Category> = categoryRepository.findByCategory(transaction.category.toString())
            if (optionalCategory.isPresent) {
                val category = optionalCategory.get()
                transaction.categries.add(category)
            }
        } else {
            LOGGER.info("cannot find the accountNameOwner record " + transaction.accountNameOwner.toString())
            return false
        }

        transactionRepository.saveAndFlush(transaction)
        return true
    }

    fun findByGuid(guid: String): Optional<Transaction> {
        val transaction: Optional<Transaction> = transactionRepository.findByGuid(guid)
        if( transaction.isPresent ) {
            return transaction
        } else {
            return Optional.empty()
        }
    }

    fun getTotalsByAccountNameOwner( accountNameOwner: String) : Totals {
        try {
            val totalsCleared: Double = transactionRepository.getTotalsByAccountNameOwnerCleared(accountNameOwner)
            val totals: Double = transactionRepository.getTotalsByAccountNameOwner(accountNameOwner)
            val t: Totals = Totals()

            t.totals = BigDecimal(totals)
            t.totalsCleared = BigDecimal(totalsCleared)

            return t
        } catch (ex: EmptyResultDataAccessException) {
            LOGGER.info(ex.message)
        } catch (e:Exception) {
            LOGGER.info(e.message)
        }
        return Totals()
    }

    //fun fetchAccoutTotals(accountNameOwner: String): Double {
    //    return transactionRepository!!.fetchAccoutTotals(accountNameOwner)
    //}

    fun findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner: String): List<Transaction> {
        val transactions: List<Transaction> = transactionRepository.findByAccountNameOwnerIgnoreCaseOrderByTransactionDateDesc(accountNameOwner)
        if( transactions.isEmpty() ) {
            LOGGER.error("an empty list of AccountNameOwner.")
            //return something
        }
        return transactions;
    }

    fun patchTransaction( transaction: Transaction ): Boolean {
        val optionalTransaction = transactionRepository.findByGuid(transaction.guid)
        if (optionalTransaction.isPresent()) {
            var updateFlag = false
            val fromDb = optionalTransaction.get()

            if( fromDb.accountNameOwner != transaction.accountNameOwner && transaction.accountNameOwner != "" ) {
                fromDb.accountNameOwner = transaction.accountNameOwner
                val accountOptional: Optional<Account> = accountRepository.findByAccountNameOwner(transaction.accountNameOwner.toString())
                if (accountOptional.isPresent) {
                    val account = accountOptional.get()
                    LOGGER.info("updates work with the new code")
                    fromDb.accountId = account.accountId
                }
                updateFlag = true
            }
            if( fromDb.accountType != transaction.accountType && transaction.accountType != "" ) {
                fromDb.accountType = transaction.accountType
                updateFlag = true
            }
            if( fromDb.description != transaction.description && transaction.description != "" ) {
                fromDb.description = transaction.description;
                updateFlag = true
            }
            if( fromDb.category != transaction.category && transaction.category != "" ) {
                fromDb.category = transaction.category
                updateFlag = true
            }
            if( transaction.notes != "" && fromDb.notes != transaction.notes && transaction.notes != "" ) {
                fromDb.notes = transaction.notes
                updateFlag = true
            }
            if( fromDb.cleared != transaction.cleared && transaction.cleared != 2 ) {
                fromDb.cleared = transaction.cleared
                updateFlag = true
            }
            if( transaction.amount != fromDb.amount && transaction.amount != 0.0 ) {
                fromDb.amount = transaction.amount
                updateFlag = true
            }
            if( transaction.transactionDate != Date(0) && transaction.transactionDate != fromDb.transactionDate ) {
                fromDb.transactionDate = transaction.transactionDate
                updateFlag = true
            }
            if( updateFlag ) {
                LOGGER.info("Saved transaction as the data has changed")
                transactionRepository.save(fromDb)
            }
            return true
        } else {
            LOGGER.warn("guid not found=" + transaction.guid)
            return false
        }
    }


}
