package crypto_analytics.converter.bitfinex;

import com.google.gson.stream.JsonReader;
import crypto_analytics.client.bitfinex.AccountPermissionsClient;
import crypto_analytics.domain.bitfinex.order.CreatedOrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Component
public class OrderConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionsClient.class);

    public CreatedOrderDto readCreatedOrder(InputStream inputStream) throws Exception {
//        String read = getStringFromInputStream(inputStream);
//        System.out.println(read);

        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        try {
            LOGGER.info("New order created!");
            return getCreateOrder(jsonReader);
        } catch (Exception e) {
            LOGGER.error("Creation of new order object failed!", e);
            throw new Exception("Creation of new order object failed!", e);
        } finally {
            jsonReader.close();
        }
    }

    private CreatedOrderDto getCreateOrder(JsonReader reader) throws IOException {
        CreatedOrderDto createdOrderDto = new CreatedOrderDto();
        Long id = null;
        Long cid = null;
        String cid_date = null;
        String gid = null;
        String symbol = null;
        String exchange = null;
        BigDecimal price = null;
        BigDecimal avgExecutionPrice = null;
        String side = null;
        String type = null;
        BigDecimal timestamp = null;
        Boolean isLive = null;
        Boolean isCancelled = null;
        Boolean isHidden = null;
        String ocoOrder = null;
        Boolean wasForced = null;
        BigDecimal originalAmount = null;
        BigDecimal remainingAmount = null;
        BigDecimal executedAmount = null;
        String src = null;
        Long orderId = null;

        try {
            reader.beginObject();

            while (reader.hasNext()) {

                String name = reader.nextName();

                if (name.equals("id")) {
                    id = reader.nextLong();
                } else if (name.equals("cid")) {
                    cid = reader.nextLong();
                } else if (name.equals("cid_date")) {
                    cid_date = reader.nextString();
                } else if (name.equals("gid")) {
                    reader.nextNull();
                } else if (name.equals("symbol")) {
                    symbol = reader.nextString();
                } else if (name.equals("exchange")) {
                    exchange = reader.nextString();
                } else if (name.equals("price")) {
                    price = new BigDecimal(reader.nextDouble());
                } else if (name.equals("avg_execution_price")) {
                    avgExecutionPrice = new BigDecimal(reader.nextDouble());
                } else if (name.equals("side")) {
                    side = reader.nextString();
                } else if (name.equals("type")) {
                    type = reader.nextString();
                } else if (name.equals("timestamp")) {
                    timestamp = new BigDecimal(reader.nextDouble());
                } else if (name.equals("is_live")) {
                    isLive = reader.nextBoolean();
                } else if (name.equals("is_cancelled")) {
                    isCancelled = reader.nextBoolean();
                } else if (name.equals("is_hidden")) {
                    isHidden = reader.nextBoolean();
                }else if (name.equals("oco_order")) {
                    reader.nextNull();
                } else if (name.equals("was_forced")) {
                    wasForced = reader.nextBoolean();
                } else if (name.equals("original_amount")) {
                    originalAmount = new BigDecimal(reader.nextString());
                } else if (name.equals("remaining_amount")) {
                    remainingAmount = new BigDecimal(reader.nextString());
                } else if (name.equals("executed_amount")) {
                    executedAmount = new BigDecimal(reader.nextString());
                }  else if (name.equals("src")) {
                    src = reader.nextString();
                }else if (name.equals("order_id")) {
                    orderId = new Long(reader.nextString());
                }
            }
        } catch (IllegalStateException e) {
            LOGGER.error("New order was not placed!");
        }
        createdOrderDto = new CreatedOrderDto(id, cid, cid_date, gid, symbol, exchange, price, avgExecutionPrice, side, type, timestamp, isLive, isCancelled, isHidden,ocoOrder, wasForced, originalAmount, remainingAmount, executedAmount, src,orderId);
        System.out.println(createdOrderDto);

        return createdOrderDto;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
