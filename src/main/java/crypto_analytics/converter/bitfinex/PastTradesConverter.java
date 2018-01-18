package crypto_analytics.converter.bitfinex;

import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonReader;
import crypto_analytics.client.bitfinex.AccountPermissionsClient;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceHistoryDto;
import crypto_analytics.domain.bitfinex.pasttrades.PastTradesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class PastTradesConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionsClient.class);

    public ArrayList<PastTradesDto> readPastTrades(InputStream inputStream) throws Exception {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        try {
            jsonReader.beginArray();
            LOGGER.info("Past trades downloaded!");
            return getPastTrades(jsonReader);
        }catch(Exception e) {
            LOGGER.error("Past trades downloading process failed!", e);
            throw new Exception("Past trades downloading process failed!",e);
        }
        finally {
            jsonReader.close();
        }
    }

    private ArrayList<PastTradesDto> getPastTrades(JsonReader reader) throws IOException {
        ArrayList<PastTradesDto> pastTradesList = new ArrayList<>();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                reader.nextName();
                BigDecimal price = new BigDecimal(reader.nextString());
                reader.nextName();
                BigDecimal amount = new BigDecimal(reader.nextString());
                reader.nextName();
                Long timestamp = checkTimestamp(reader.nextString());
                reader.nextName();
                String exchange = reader.nextString();
                reader.nextName();
                String type = reader.nextString();
                reader.nextName();
                String feeCurrency = reader.nextString();
                reader.nextName();
                BigDecimal feeAmount = new BigDecimal(reader.nextString());
                reader.nextName();
                Long tid = new Long(reader.nextString());
                reader.nextName();
                Long orderIrd = new Long(reader.nextString());
                pastTradesList.add(new PastTradesDto(price,amount,timestamp,exchange,type,feeCurrency,feeAmount,tid,orderIrd));
            }
            pastTradesList.stream().forEach(System.out::println);
        } catch (IllegalStateException e) {
            LOGGER.error("Past trades history is empty");
        }
        return pastTradesList;
    }

    private Long checkTimestamp(String timestamp) {
        String[] checkedTimestamp = timestamp.split("\\.");
        return new Long(checkedTimestamp[0]);
    }

}
