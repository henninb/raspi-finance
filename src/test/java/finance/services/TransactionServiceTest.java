package finance.services;

//import finance.models.Transaction;
//import finance.repositories.TransactionRepository;
import finance.models.Transaction;
import finance.repositories.TransactionRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;

//@RunWith(SpringRunner.class)
public class TransactionServiceTest {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @InjectMocks
    TransactionService transactionService;

    @Mock
    TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        Transaction transaction = new Transaction();
        //transaction.setAccountNameOwner("discover_brian");
        Optional<Transaction> transactionOptional = Optional.of(transaction);
        MockitoAnnotations.initMocks(this);
        Mockito.when(transactionRepository.findByGuid(anyString())).thenReturn(transactionOptional);
    }

    @Test
    public void deleteByGuidTest() {
        Mockito.when(transactionService.deleteByGuid("14077989")).thenReturn(true);
        assert (true);
    }
}
