package finance.routes;

import finance.processors.InsertTransactionProcessor;
import finance.processors.JsonTransactionProcessor;
import finance.processors.StringTransactionProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MasterRouteTest {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @MockBean
    StringTransactionProcessor stringTransactionProcessor;

    @MockBean
    JsonTransactionProcessor jsonTransactionProcessor;

    @MockBean
    InsertTransactionProcessor insertTransactionProcessor;

    @Before
    public void setUp() {
    }

    @Test
    public void dummy()  {
        assert (true);
    }
}
