package finance.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import finance.repositories.AccountRepository;
import finance.model.ExcelTransaction;
import finance.model.Transaction;
import finance.repositories.TransactionRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.*;
import java.util.Arrays;
import java.util.List;

@Component
public class JsonProcessor implements Processor {
    private static ObjectMapper mapper = new ObjectMapper();
    //private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private String preString;
    private MessageDigest digest;

    @Autowired
    private TransactionRepository transactionRepository;

    //@Autowired
    //private AccountRepository accountRepository;

    @Override
    public void process(Exchange exchange) throws Exception {
        String bodyStr = exchange.getIn().getBody(String.class);

        bodyStr = bodyStr.replaceAll("\\n", "");
        bodyStr = bodyStr.replaceAll("\\r", "");
        bodyStr = bodyStr.replaceAll("\0", "");

        //byte[] body = bodyStr.getBytes();

        //List<Transaction> transactionList = mapper.readValue(exchange.getIn().getBody(String.class), new TypeReference<List<Transaction>>() { });
        //TypeReference<HashMap<ExcelTransaction,String>> typeRef = new TypeReference<HashMap<ExcelTransaction, String>>() {};

        //POJO = Plain Old Java Object
        try {
            ExcelTransaction[] excelTransactions = mapper.readValue(bodyStr, ExcelTransaction[].class);
            List<ExcelTransaction> transactionList = Arrays.asList(excelTransactions);
            digest = MessageDigest.getInstance("SHA-256");

            //accounts.
            System.out.println("size: " + transactionList.size());

            for (ExcelTransaction t : transactionList) {
                //Transaction transaction = new Transaction();
                Transaction transaction = transactionRepository.findByGuid(t.getGuid());
                //transaction.setGuid(t.getGuid());

                if(transaction == null) {
                    transaction = new Transaction();
                    transaction.setGuid(t.getGuid());
                    //System.out.println("we have a problem with nulls.");
                }

                transaction.setTransactionDate(toEpoch(t.getTransactionDate()));
                transaction.setDescription(t.getDescription());
                transaction.setCategory(t.getCategory());
                transaction.setAmount(Double.parseDouble(t.getAmount()));
                transaction.setNotes(t.getNotes());
                transaction.setCleared(Integer.parseInt(t.getCleared()));
                transaction.setDateAdded(toEpoch(t.getDateAdded()));
                transaction.setDateUpdated(toEpoch(t.getDateUpdated()));
                transaction.setAccountType(t.getAccountType());
                transaction.setAccountNameOwner(t.getAccountNameOwner());
                transaction.setSha256(t.getSha256());
                System.out.println(t.getSha256());

                preString = cleanField(t.getTransactionDate().toString()) + "||" +  cleanField(t.getDescription().toString()) + "||" + cleanField(t.getCategory().toString()) + "||" + toFormatedDouble(cleanField(t.getAmount().toString())) + "||" + t.getCleared() + "||" + cleanField(t.getNotes().toString() + "||" + t.getAccountNameOwner());
                System.out.println("INFO:<" + preString + ">");
                String sha256hex = Hashing.sha256().hashString(preString, StandardCharsets.UTF_8).toString();
                System.out.println("sha256hex:" + sha256hex);
                try {
                    transactionRepository.save(transaction);
                }
                catch( JpaSystemException pae) {
                    pae.printStackTrace();
                    System.out.println("WARN: issue with account_id with primative type");
                    //continue;
                }
                catch (DataIntegrityViolationException ex ) {
                    ex.printStackTrace();
                    System.out.println("WARN: duplicate key.");
                    //continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            exchange.setException(e);
        }

        //exchange.getIn().setBody(transactionList);
    }

    private Date toEpoch( String epochString) {

        try {
            long epoch = Long.parseLong( epochString );
            return new Date( epoch * 1000 );
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return new Date(0L);
        //return new SimpleDateFormat("YYYY-MM-dd-HH:mm:ss").format(new Date(0"0"));
    }

    private String toFormatedDouble( String field ) {
        NumberFormat numberFormat = new DecimalFormat("#0.00");
        return numberFormat.format(Double.parseDouble(field));
    }

    private String cleanField( String field ) {
        return field.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
    }
}