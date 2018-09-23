package finance.services;


import finance.model.Transaction;
import finance.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        this.transactionRepository.findAll().forEach(transactions::add); //fun with Java 8
        return transactions;
    }

    public double fetchAccoutTotals(String accountNameOwner) {
        return transactionRepository.fetchAccoutTotals(accountNameOwner);
    }

    public void deleteByGuid(String guid) {
        try {
            transactionRepository.deleteByGuid(guid);
        }
        catch( Exception ex ) {
            System.out.println(ex);
        }
    }

    public void insertTransaction(Transaction transaction) {
        Transaction result = transactionRepository.saveAndFlush(transaction);
        if( transaction.getGuid().equals(result.getGuid()) ) {
            LOGGER.info("INFO: transactionRepository.saveAndFlush success.");
        } else {
            LOGGER.info("WARN: transactionRepository.saveAndFlush failure.");
        }
    }

    public Transaction findByGuid( String guid ) {
        return transactionRepository.findByGuid(guid);
    }

    public List<Transaction> findByAccountNameOwnerIgnoreCaseOrderByTransactionDate( String accountNameOwner ) {
        return transactionRepository.findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner);
    }

    @Transactional
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
