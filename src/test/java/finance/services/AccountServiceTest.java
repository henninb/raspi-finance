package finance.services;

import finance.models.Account;
import finance.repositories.AccountRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @Mock
    AccountRepository accountsRepository;

    @InjectMocks
    AccountService accountService;

    @Before
    public void setUp() throws Exception {
    }

    //mokito
    @Test
    public void findAll() throws Exception {

    }

}