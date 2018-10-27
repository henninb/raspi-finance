package finance.services

import finance.models.Account
import finance.repositories.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.ArrayList
import java.util.function.Consumer

@Service
open class AccountService {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit private var accountRepository: AccountRepository<Account>

    fun findAllOrderByAccountNameOwner(): List<Account> {
        val accounts = accountRepository.findAll()
        return accounts
    }

    fun findAllAcitveAccounts(): List<Account> {
        val accounts = accountRepository.findByActiveStatusOrderByAccountNameOwner("Y")
        if( accounts.isEmpty()) {
            LOGGER.warn("findAllAcitveAccounts() problem.")
        } else {
            LOGGER.info("findAllAcitveAccounts()")
        }
        return accounts
    }

    fun findByAccountNameOwner(accountNameOwner: String): Account {
        return accountRepository.findByAccountNameOwner(accountNameOwner)
    }

    //fun findByAccountNameOwnerOrderBy(accountNameOwner: String): Account {
    //    return accountRepository!!.findByAccountNameOwner(accountNameOwner)
    //}

    fun insertAccount(account: Account) {
        //TODO: Should saveAndFlush be in a try catch block?
        val result = accountRepository.saveAndFlush(account)
            LOGGER.info("INFO: transactionRepository.saveAndFlush success.")
    }
}