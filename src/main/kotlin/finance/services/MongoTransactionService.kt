package finance.services

import finance.models.Account
import finance.models.Category
import finance.models.Transaction
import finance.repositories.MongoAccountRepository
import finance.repositories.MongoCategoryRepository
import finance.repositories.MongoTransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class MongoTransactionService {
    //@Autowired(required=false)
    @Autowired
    lateinit var mongoTransactionRepository: MongoTransactionRepository<Transaction>

    @Autowired
    lateinit var mongoAccountRepository: MongoAccountRepository<Account>

    @Autowired
    lateinit var mongoCategoryRepository: MongoCategoryRepository<Category>

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    fun findByAccountNameOwnerAndCleared( accountNameOwner: String, cleared: Int) : List<Transaction> {
        return this.mongoTransactionRepository.findByAccountNameOwnerAndClearedOrderByTransactionDateDesc(accountNameOwner, cleared)
    }

    fun insertTransaction(transaction: Transaction): Boolean {
        val transactionOptional: Optional<Transaction> = mongoTransactionRepository.findByGuid(transaction.guid)
        if( transactionOptional.isPresent ) {
            LOGGER.info("duplicate found, no transaction data inserted.")
            return false
        }
        val accountOptional: Optional<Account> = mongoAccountRepository.findByAccountNameOwner(transaction.accountNameOwner.toString())
        if (accountOptional.isPresent) {
            val account = accountOptional.get()
            transaction.accountId = account.accountId
            LOGGER.info("accountOptional isPresent.")
            val optionalCategory: Optional<Category> = mongoCategoryRepository.findByCategory(transaction.category.toString())
            if (optionalCategory.isPresent) {
                val category = optionalCategory.get()
                transaction.categries.add(category)
            }
        } else {
            LOGGER.info("cannot find the accountNameOwner record " + transaction.accountNameOwner.toString())
            return false
        }

        mongoTransactionRepository.save(transaction)
        return true
    }
}