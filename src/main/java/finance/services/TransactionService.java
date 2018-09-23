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

/*
 Annotation | Meaning                                             |
 +------------+-----------------------------------------------------+
 | @Component | generic stereotype for any Spring-managed component |
 | @Repository| stereotype for persistence layer                    |
 | @Service   | stereotype for service layer                        |
 | @Controller| stereotype for presentation layer (spring-mvc)      |
 +------------+-----------------------------------------------------+
*/
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
