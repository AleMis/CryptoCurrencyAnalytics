package crypto_analytics.converter.bitfinex;

import com.google.gson.stream.JsonReader;
import crypto_analytics.client.bitfinex.AccountPermissionsClient;
import crypto_analytics.domain.bitfinex.order.CreatedOrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Component
public class OrderConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionsClient.class);

    public CreatedOrderDto readCreatedOrder(InputStream inputStream) throws Exception {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        try {
            jsonReader.beginArray();
            LOGGER.info("New order created!");
            return getCreateOrder(jsonReader);
        }catch(Exception e) {
            LOGGER.error("Placing the new order failed!", e);
            throw new Exception("Placing the new order failed!",e);
        }
        finally {
            jsonReader.close();
        }
    }

    private CreatedOrderDto getCreateOrder(JsonReader reader) throws IOException {
        CreatedOrderDto createdOrderDto = new CreatedOrderDto();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                reader.nextName();
                Long orderId = new Long(reader.nextString());
                reader.nextName();
                String symbol = reader.nextString();
                reader.nextName();
                String exchange = reader.nextString();
                reader.nextName();
                BigDecimal price = new BigDecimal(reader.nextString());
                reader.nextName();
                BigDecimal avgExecutionPrice = new BigDecimal(reader.nextString());
                reader.nextName();
                String side = reader.nextString();
                reader.nextName();
                String type = reader.nextString();
                reader.nextName();
                Long timestamp = new Long(reader.nextString());
                reader.nextName();
                Boolean isLive = new Boolean(reader.nextString());
                reader.nextName();
                Boolean isCancelled = new Boolean(reader.nextString());
                reader.nextName();
                Boolean isHidden = new Boolean(reader.nextString());
                reader.nextName();
                Boolean wasForced = new Boolean(reader.nextString());
                reader.nextName();
                BigDecimal originalAmount = new BigDecimal(reader.nextString());
                reader.nextName();
                BigDecimal remainingAmount = new BigDecimal(reader.nextString());
                reader.nextName();
                BigDecimal executedAmount = new BigDecimal(reader.nextString());
                createdOrderDto = new CreatedOrderDto(orderId, symbol, exchange, price, avgExecutionPrice, side, type, timestamp, isLive, isCancelled, isHidden, wasForced, originalAmount, remainingAmount, executedAmount);
            }
        } catch (IllegalStateException e) {
            LOGGER.error("New order was not placed!");
        }
        return createdOrderDto;
    }
}
