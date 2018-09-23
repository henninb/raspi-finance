package finance.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import finance.model.Transaction;
import finance.services.TransactionService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class InsertTransactionProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static ObjectMapper mapper = new ObjectMapper();
    final ByteArrayOutputStream jsonOutputBytes = new ByteArrayOutputStream();

    @Autowired
    private TransactionService transactionService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String jsonString = "";
        try {
            Transaction[] transactions = exchange.getIn().getBody(Transaction[].class);
            for (Transaction transaction : transactions) {
                transactionService.insertTransaction(transaction);
            }
            mapper.writeValue(jsonOutputBytes, transactions);
            jsonString = new String(jsonOutputBytes.toByteArray());
            exchange.getIn().setBody(jsonString);
        }

        catch( DataIntegrityViolationException dive) {
            if( dive.getMessage().contains("could not execute statement; SQL [n/a]; constraint [guid_idx]")) {
                LOGGER.info("guid_idx SQL violation.");
            }
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
