package finance.processors;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import finance.model.Transaction;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JsonTransactionProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        Transaction[] transactions;

        try {
            String jsonString = exchange.getIn().getBody(String.class);
            transactions = mapper.readValue(jsonString, Transaction[].class);
            exchange.getIn().setBody(transactions);
        }

        catch( UnrecognizedPropertyException upe) {
            LOGGER.info("UnrecognizedPropertyException");
            upe.printStackTrace();
            throw upe;
        }
        catch( IOException ioe ) {
            LOGGER.info("IOException");
            ioe.printStackTrace();
        }
        catch(Exception e) {
            LOGGER.info("UnrecognizedPropertyException");
            e.printStackTrace();
        }
    }
}
