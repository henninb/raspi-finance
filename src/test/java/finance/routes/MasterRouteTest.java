package finance.routes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MasterRouteTest {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Before
    public void setUp() {
    }

    @Test
    public void dummy()  {
        assert (true);
    }
}
