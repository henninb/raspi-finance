package finance.controllers


import finance.models.Account
import finance.services.AccountService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = arrayOf("http://localhost:3000"))
@RestController
//@RequestMapping("/account")
class AccountController {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    internal var accountService: AccountService? = null

    @GetMapping(path = arrayOf("/select_accounts"))
    fun selectAllActiveAccounts(): List<Account> {
        val accounts : List<Account> = accountService!!.findAllAcitveAccounts()
        if( accounts.isEmpty() ) {
            LOGGER.info("selectAllActiveAccounts() problem.")
        } else {
            LOGGER.info("selectAllActiveAccounts() good.")
        }
        return accountService!!.findAllAcitveAccounts()
    }

    @GetMapping(path = arrayOf("/select_all"))
    fun select_all_accounts(): List<Account> {
        return accountService!!.findAllOrderByAccountNameOwner()
    }

    @GetMapping(path = arrayOf("/select_account/{accountNameOwner}"))
    fun select_account(@PathVariable accountNameOwner: String): Account {
        val account: Account
        LOGGER.info(accountNameOwner)
        account = accountService!!.findByAccountNameOwner(accountNameOwner)
        return account
    }
}
