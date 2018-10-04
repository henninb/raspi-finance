package finance.controllers


import finance.models.Account
import finance.services.AccountService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = arrayOf("http://localhost:3000"))
@RestController
@RequestMapping("/accounts")
class AccountController {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    internal var accountService: AccountService? = null
    @GetMapping(value = "/accountFindAll")
    fun transactionFindAll(): List<Account> {
        return accountService!!.findAll()
    }

    @GetMapping(value = "/getAccount/{accountNameOwner}")
    fun getTransaction(@PathVariable accountNameOwner: String): Account {
        val account: Account
        LOGGER.info(accountNameOwner)
        account = accountService!!.findByAccountNameOwner(accountNameOwner)
        return account
    }
}