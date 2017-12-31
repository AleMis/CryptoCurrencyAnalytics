package crypto_analytics.converter.bitfinex;

import com.google.gson.stream.JsonReader;
import crypto_analytics.client.bitfinex.AccountInformationClient;
import crypto_analytics.domain.bitfinex.permisions.PermisionsDto;
import crypto_analytics.domain.bitfinex.permisions.PermisionsParametersDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Component
public class PermisionsConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountInformationClient.class);

    public ArrayList<PermisionsDto> readPermissionsList(InputStream inputStream) throws Exception {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        try {
            LOGGER.info("Account permisions was downloaded!");
            return getPermisionsListFromInputStream(jsonReader);
        }catch(Exception e) {
            LOGGER.error("Permisions downloading process failed!", e);
            throw new Exception("Permisions downloading process failed!",e);
        }
        finally {
            jsonReader.close();
        }
    }

    private ArrayList<PermisionsDto> getPermisionsListFromInputStream(JsonReader reader) throws IOException {
        ArrayList<PermisionsDto> permisionsList = new ArrayList<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("account")) {
                permisionsList.add(new PermisionsDto(name, getPermisionsParametersFromIntpuStream(reader)));
            }else if(name.equals("history")) {
                permisionsList.add(new PermisionsDto(name, getPermisionsParametersFromIntpuStream(reader)));
            }else if(name.equals("orders")) {
                permisionsList.add(new PermisionsDto(name, getPermisionsParametersFromIntpuStream(reader)));
            }else if(name.equals("positions")) {
                permisionsList.add(new PermisionsDto(name, getPermisionsParametersFromIntpuStream(reader)));
            }else if(name.equals("funding")) {
                permisionsList.add(new PermisionsDto(name, getPermisionsParametersFromIntpuStream(reader)));
            }else if(name.equals("wallets")) {
                permisionsList.add(new PermisionsDto(name, getPermisionsParametersFromIntpuStream(reader)));
            }else if(name.equals("withdraw")) {
                permisionsList.add(new PermisionsDto(name, getPermisionsParametersFromIntpuStream(reader)));
            }
        }
        return permisionsList;
    }

    private PermisionsParametersDto getPermisionsParametersFromIntpuStream(JsonReader reader) throws IOException {
        boolean read = false;
        boolean write = false;

        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("read")) {
                read = reader.nextBoolean();
            }else if(name.equals("write")) {
                write = reader.nextBoolean();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new PermisionsParametersDto(read,write);
    }
}
