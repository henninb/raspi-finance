package finance.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import finance.model.Transaction;

import java.io.IOException;

public class TransactionSerializer extends StdSerializer<Transaction> {

    public TransactionSerializer() {
        this(null);
    }

    public TransactionSerializer(Class<Transaction> t) {
        super(t);
    }

    @Override
    public void serialize(Transaction transaction, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeStringField("guid", transaction.getGuid());
        jgen.writeStringField("sha256", transaction.getSha256());
        jgen.writeStringField("accountType", transaction.getAccountType());
        jgen.writeStringField("accountNameOwner", transaction.getAccountNameOwner());
        jgen.writeStringField("description", transaction.getDescription());
        jgen.writeStringField("category", transaction.getCategory());
        jgen.writeStringField("notes", transaction.getNotes());
        jgen.writeNumberField("cleared", transaction.getCleared());
        jgen.writeStringField("amount", Double.toString(transaction.getAmount()));
        jgen.writeNumberField("transactionDate", transaction.getTransactionDate().getTime() / 1000);
        jgen.writeNumberField("dateUpdated", transaction.getDateUpdated().getTime() / 1000);
        jgen.writeNumberField("dateAdded", transaction.getDateAdded().getTime() / 1000);
        jgen.writeEndObject();
    }
}
