package finance.controllers

import finance.services.AccountService
import finance.services.TransactionService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
//import sun.security.x509.OIDMap.addAttribute
//import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

//@CrossOrigin(origins = arrayOf("http://localhost:3000"))
//Thymeleaf and RestController will output JSON and not HTML
@Controller
class ThymeleafController {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    internal var transactionService: TransactionService? = null

    @Autowired
    internal var accountService: AccountService? = null

    //localhost:8080/
    @RequestMapping("/")
    fun index(): String {
        //var model: Model
        return "index"
    }

    //localhost:8080/transactionCreate
    @GetMapping(path = arrayOf("/transactionCreate"))
    fun transactionCreate(): String {
        return "transactionCreate"
    }

    @GetMapping(path = arrayOf("/transactionView"))
    fun transactionView(model: Model): String {
        val accounts = accountService!!.findAllAcitveAccounts()
        model.addAttribute("accounts", accounts)
        return "transactionView"
    }

    //http://localhost:8080/transactionView/amex_brian
    //TODO: ResponseEntity code fix
    @GetMapping(path = arrayOf("/transactionView/{accountNameOwner}"))
    fun transactionView(@PathVariable accountNameOwner: String, model: Model): String {
        val accounts = accountService!!.findAllAcitveAccounts()
        val transactions = transactionService!!.findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner)
        model.addAttribute("transactions", transactions)
        model.addAttribute("accounts", accounts)
        return "transactionView"
    }
}
