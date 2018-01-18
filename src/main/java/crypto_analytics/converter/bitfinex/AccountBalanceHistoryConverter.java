package crypto_analytics.converter.bitfinex;

import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonReader;
import crypto_analytics.client.bitfinex.AccountPermissionsClient;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceHistoryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class AccountBalanceHistoryConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionsClient.class);

    public ArrayList<AccountBalanceHistoryDto> readAccountsBalanceHistory(InputStream inputStream) throws Exception {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        try {
            jsonReader.beginArray();
            LOGGER.info("Accounts balance history downloaded!");
            return getAccountsBalances(jsonReader);
        }catch(Exception e) {
            LOGGER.error("Accounts balance history downloading process failed!", e);
            throw new Exception("Accounts balances history downloading process failed!",e);
        }
        finally {
            jsonReader.close();
        }
    }

    private ArrayList<AccountBalanceHistoryDto> getAccountsBalances(JsonReader reader) throws IOException {
        ArrayList<AccountBalanceHistoryDto> accountBalanceHistoryList = new ArrayList<>();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                reader.nextName();
                String currency = reader.nextString();
                reader.nextName();
                BigDecimal amount = new BigDecimal(reader.nextString());
                reader.nextName();
                BigDecimal balance = new BigDecimal(reader.nextString());
                reader.nextName();
                String description = reader.nextString();
                reader.nextName();
                Long timestamp = checkTimestamp(reader.nextString());
                accountBalanceHistoryList.add(new AccountBalanceHistoryDto(currency, amount, balance, description, timestamp));
            }
        } catch (JsonIOException e) {
            LOGGER.error("Account balance history is empty");
        }
        return accountBalanceHistoryList;
    }

    private Long checkTimestamp(String timestamp) {
        String[] checkedTimestamp = timestamp.split("\\.");
        return new Long(checkedTimestamp[0]);
    }
}
