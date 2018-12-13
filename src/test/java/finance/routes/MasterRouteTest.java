package finance.routes;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(locations = "classpath:app-context.xml")
//@ComponentScan("finance")
//@PropertySource(value = {"classpath:application.properties"})
@RunWith(SpringRunner.class)
public class MasterRouteTest extends CamelTestSupport {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    //@MockBean
    //StringTransactionProcessor stringTransactionProcessor;

    //@MockBean
    //JsonTransactionProcessor jsonTransactionProcessor;

    //@MockBean
    //InsertTransactionProcessor insertTransactionProcessor;

    //private Transaction transactionDummy;
    String json_string;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                        .autoStartup(true)
                        //.process(jsonTransactionProcessor)
                        .to("mock:result");
            }
        };
    }

    @Before
    public void setUp() {
        json_string = "{\"guid\":\"83e30475-fcfc-4f30-8395-0bb40e89b568\",\"sha256\":\"b154020e5cd52d086d1650fa01a82b82f823e67cd59b7b346aeeb781c7dafa44\",\"accountType\":\"credit\",\"accountNameOwner\":\"chase_brian\",\"description\":\"First Transaction\",\"category\":\"\",\"notes\":\"Something\",\"cleared\":1,\"reoccurring\":false,\"amount\":\"0.1\",\"transactionDate\":1353456000,\"dateUpdated\":1538351596,\"dateAdded\":1538351596}";
        //transactionDummy = mapper.readValue(json_string, Transaction.class);
    }

    @Test
    public void dummy() throws InterruptedException {
        //resultEndpoint.expectedMessageCount(1);
        //if(template != null ) {
        //    template.sendBody(json_string);
        //}
        //resultEndpoint.assertIsSatisfied();
        assert (true);
    }
}
