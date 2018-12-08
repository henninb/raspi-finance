package finance.services

import finance.models.Account
import finance.repositories.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

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

    fun findByAccountNameOwner(accountNameOwner: String): Optional<Account> {
        val optionalAccunt: Optional<Account> = accountRepository.findByAccountNameOwner(accountNameOwner)
        return optionalAccunt
    }

    //fun findByAccountNameOwnerOrderBy(accountNameOwner: String): Account {
    //    return accountRepository!!.findByAccountNameOwner(accountNameOwner)
    //}

    fun insertAccount(account: Account) : Boolean {
        //TODO: Should saveAndFlush be in a try catch block?
        accountRepository.saveAndFlush(account)
        LOGGER.info("INFO: transactionRepository.saveAndFlush success.")
        return true
    }

    fun deleteByAccountNameOwner(accountNameOwner: String) {
        accountRepository.deleteByAccountNameOwner(accountNameOwner)
    }

//    fun patchAccount(account: Account) : Boolean {
//        return false
//    }
}