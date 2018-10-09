package finance.services;

import finance.models.Transaction;
import finance.repositories.TransactionRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService;

    @Before
    public void setUp() throws Exception {
    }

    //mokito
    @Test
    public void findAll() throws Exception {

    }

}