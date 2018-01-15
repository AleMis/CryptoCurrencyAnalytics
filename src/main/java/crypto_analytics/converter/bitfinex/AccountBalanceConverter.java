package crypto_analytics.converter.bitfinex;

import com.google.gson.stream.JsonReader;
import crypto_analytics.client.bitfinex.AccountInformationClient;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class AccountBalanceConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountInformationClient.class);

    public ArrayList<AccountBalanceDto> readAccountsBalances(InputStream inputStream) throws Exception {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        try {
            jsonReader.beginArray();
            LOGGER.info("Accounts balances was downloaded!");
            return getAccountsBalances(jsonReader);
        }catch(Exception e) {
            LOGGER.error("Accounts balances downloading process failed!", e);
            throw new Exception("Accounts balances downloading process failed!",e);
        }
        finally {
            jsonReader.close();
        }
    }

    private ArrayList<AccountBalanceDto> getAccountsBalances(JsonReader reader) throws IOException {
        ArrayList<AccountBalanceDto> accountBalanceList = new ArrayList<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            String type = "";
            String currency = "";
            BigDecimal amount = null;
            BigDecimal available = null;

            if (name.equals("type")) {
                type = reader.nextString();
            }else if(name.equals("currency")) {
                currency = reader.nextString();
            }else if(name.equals("amount")) {
                amount = new BigDecimal(reader.nextDouble());
            }else if(name.equals("available")) {
                available = new BigDecimal(reader.nextDouble());
            }
            accountBalanceList.add(new AccountBalanceDto(type, currency, amount, available));
        }
        accountBalanceList.stream().forEach(System.out::println);
        return accountBalanceList;
    }
}
