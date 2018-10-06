package finance.services

import finance.models.Account
import finance.repositories.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.ArrayList
import java.util.function.Consumer

@Service
class AccountService {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    internal var accountRepository: AccountRepository? = null

    fun findAll(): List<Account> {
        val accounts = ArrayList<Account>()
        this.accountRepository!!.findAll().forEach(Consumer<Account> { accounts.add(it) })
        return accounts
    }

    fun findByAccountNameOwner(accountNameOwner: String): Account {
        return accountRepository!!.findByAccountNameOwner(accountNameOwner)
    }

    fun insertAccount(account: Account) {
        //TODO: Should saveAndFlush be in a try catch block?
        val result = accountRepository!!.saveAndFlush(account)
            LOGGER.info("INFO: transactionRepository.saveAndFlush success.")
    }
}