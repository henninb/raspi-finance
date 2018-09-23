package finance.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import finance.model.Transaction;

import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;

public class TransactionDeserializer extends StdDeserializer<Transaction> {

        public TransactionDeserializer() {
            this(null);
        }

        public TransactionDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Transaction deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException  {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            String guid = node.get("guid").asText();
            String sha256 = node.get("sha256").asText();
            String accountType = node.get("accountType").asText();
            String accountNameOwner = node.get("accountNameOwner").asText();
            String description = node.get("description").asText();
            String category = node.get("category").asText();
            String notes = node.get("notes").asText();

            Double amount = new Double(new DecimalFormat("#.##").format(node.get("amount").asDouble()));
            int cleared = node.get("cleared").asInt();
            Date transactionDate = new Date(node.get("transactionDate").asLong() * 1000 );
            Date dateUpdated = new Date(node.get("dateUpdated").asLong() * 1000 );
            Date dateAdded = new Date(node.get("dateAdded").asLong() * 1000 );

            return new Transaction(guid,accountType,accountNameOwner,transactionDate,description, category,amount, cleared, notes, dateUpdated, dateAdded, sha256);
        }
}
