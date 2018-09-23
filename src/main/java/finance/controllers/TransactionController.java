package finance.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import finance.model.Account;
import finance.model.AccountTotal;
import finance.model.Summary;
import finance.repositories.SummaryRepository;
import finance.model.Transaction;
import finance.services.AccountService;
import finance.services.TransactionService;
import finance.pojo.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

//https://stackoverflow.com/questions/43533691/required-a-bean-of-type-org-hibernate-sessionfactory-that-could-not-be-found

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/transactions")
//@EnableTransactionManagement
//@RequestMapping("/transactions", method=RequestMethod.GET)
public class TransactionController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SummaryRepository summaryRepository;

    //insert into t_transaction(account_type, account_name_owner, transaction_date, description, category, amount, is_cleared, notes, date_updated, date_added) VALUES('credit', 'usbankcash_brian', '8/1/2017', 'Mario Kart', 'toys', '49.99', false, '', '8/1/2017', '8/1/2017')
    //http://localhost:8080/transactions/transactionFindAll
    @GetMapping(value = "/transactionFindAll")
    public List<Transaction> transactionFindAll() {
      return transactionService.findAll();
    }

    //http://localhost:8080/transactions/getTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc
    @GetMapping(value = "/getTransaction/{guid}")
    public Transaction getTransaction(@PathVariable String guid) {
        Transaction transaction;
        LOGGER.info(guid);
        //Transaction transaction = transactionRepository.findByGuid(guid);
        transaction = transactionService.findByGuid(guid);
      return transaction;
    }

    //http://localhost:8080/transactions/fetchAccoutTotals/chase_kari
    @GetMapping(value = "/fetchAccoutTotals/{accountNameOwner}")
    public AccountTotal fetchAccoutTotals(@PathVariable String accountNameOwner) {
        String results = "";
        Double totals = 0.0;
        AccountTotal accountTotal = new AccountTotal();
        totals = transactionService.fetchAccoutTotals(accountNameOwner);
        accountTotal.setAccountTotal(totals);
        return accountTotal;
    }

    @PostMapping(path = "/addTransactionPost", consumes = "application/json", produces = "application/json")
    public void addMember(@RequestBody String member) {
        //code
    }

    //http://localhost:8080/transactions/addTransaction?accountNameOwner=brian_chase&transactionDate=123&description=test&category=test&amount=0.01&cleared=0&notes=empty
    @GetMapping(value = "/addTransaction")
    public String addTransaction(@RequestParam String accountNameOwner, @RequestParam String transactionDate,
            @RequestParam String description, @RequestParam String category,@RequestParam String amount,
            @RequestParam String cleared, @RequestParam String notes ) {
        RestResult restResult = new RestResult();
        String resultString = "";
        Transaction transaction = new Transaction();

        try {
            transaction.setGuid(UUID.randomUUID().toString());
            //accountNameOwner, required
          transaction.setAccountNameOwner(accountNameOwner);

          //transactionDate, if empty then now
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
          //transaction.setTransactionDate(transactionDate);
          transaction.setTransactionDate(date);
          //description, required
          transaction.setDescription(description);
          //category, optional
          transaction.setCategory(category);
          //amount, required
          transaction.setAmount(Double.parseDouble(amount));
          //cleared, false unless set to true
          transaction.setCleared(0);
          //notes, optional
          transaction.setNotes(notes);
          transactionService.save(transaction);

          restResult.setMessage("Successfully processed add message.");
          restResult.setResultCode(0);
          restResult.setGuid(transaction.getGuid());
          restResult.setDate(ZonedDateTime.now());

          resultString = mapper.writeValueAsString(restResult);
        }
        catch( JsonProcessingException jpe) {
            restResult.setMessage("Failure to processed add message: " + "Exception: "+ jpe + " Exception message:" + jpe.getMessage());
            restResult.setResultCode(200);
        }

        catch ( Exception e) {
            restResult.setMessage("Failure to processed add message: " + "Exception: "+ e + " Exception message:" + e.getMessage());
            restResult.setResultCode(201);
        }
        return resultString;
    }

  //http://localhost:8080/transactions/deleteTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc
  @GetMapping(value = "/deleteTransaction/{guid}")
  public String deleteTransaction(@PathVariable String guid) {
      RestResult restResult = new RestResult();
      String resultString = "";
      restResult.setGuid(guid);

      LOGGER.info(guid);
      try {
          Transaction transaction = transactionService.findByGuid(guid);
          LOGGER.info(transaction.getDescription());
          LOGGER.info(transaction.getAccountNameOwner());
          LOGGER.info(transaction.getGuid());
          LOGGER.info("transactions.contains(transaction) is found.");

          transactionService.deleteByGuid(guid);
          restResult.setMessage("Successfully processed delete message.");
          restResult.setResultCode(0);
          restResult.setDate(ZonedDateTime.now());
          resultString = mapper.writeValueAsString(restResult);
          return resultString;
      } catch ( Exception ex ) {
          LOGGER.info(ex.getMessage());
          restResult.setMessage("Failure to processed delete message: " + "Exception: "+ ex + " Exception message:" + ex.getMessage());
          restResult.setResultCode(100);
          restResult.setDate(ZonedDateTime.now());
          resultString = mapper.writeValueAsString(restResult);
          return resultString;
      }
      finally {
          return resultString;
      }
  }

  //http://localhost:8080/transactions/getActiveAccounts
  @GetMapping(value = "/findActiveAccounts")
  public List<Account> findActiveAccounts() {
      return accountService.findActiveAccounts();
  }

    //http://localhost:8080/transactions/getAccounts
  @GetMapping(value = "/accountfindAll")
  public List<Account> accountfindAll() {
      return accountService.findAll();
  }

  //http://localhost:8080/transactions/getTransactionsByAccountNameOwner/chase_kari
  @GetMapping(value= "/getTransactionsByAccountNameOwner/{accountNameOwner}")
  public List<Transaction> getTransactionsByAccountNameOwner(@PathVariable String accountNameOwner) {
      //return transactionRepository.findByAccountNameOwner(accountNameOwner);
      //return transactionRepository.findByAccountNameOwnerIgnoreCase(accountNameOwner);
      return transactionService.findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner);
  }

  //http://localhost:8080/transactions/getSummaryByAccountNameOwner/chase_kari
  @GetMapping(value="/getSummaryByAccountNameOwner/{accountNameOwner}")
  public List<Summary> getSummaryByAccountNameOwner(@PathVariable String accountNameOwner) {
      String guid = summaryRepository.fetchGuid();
      return summaryRepository.fetchSummaryByAccountNameOwner(guid, accountNameOwner);
  }

  //http://localhost:8080/transactions/getSummary
  @GetMapping(value="/getSummary")
  public List<Summary> getSummary() {
      //return summaryRepository.findAll();
      String guid = summaryRepository.fetchGuid();
      return summaryRepository.fetchSummary(guid);
  }
}
