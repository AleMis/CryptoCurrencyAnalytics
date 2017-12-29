package crypto_analytics.authentication;

import com.google.gson.Gson;
import crypto_analytics.domain.bitfinex.apikey.ApiKeys;
import crypto_analytics.domain.bitfinex.apikey.ApiKeysDto;
import crypto_analytics.mapper.bitfinex.ApiKeysMapper;
import crypto_analytics.service.bitfinex.DbService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@NoArgsConstructor
@Getter
@Component
public class ExchangeAuthentication {

    @Value("${bitfinex.main.url}")
    private String bitfinexMainUrl;

    @Value("${algorithm.hmac}")
    private String alhorithmHMACSHA384;

    private static final String UNEXPECTED_IO_ERROR_MSG = "Failed to connect to Exchange due to unexpected IO error.";
    private static final String IO_SOCKET_TIMEOUT_ERROR_MSG = "Failed to connect to Exchange due to socket timeout.";
    private static final String CONNECTION_ERROR = "Failed to connect to Exchange. Something dead!";
    private static final String SSL_CONNECTION_REFUSED = "Failed to connect to Exchange. SSL Connection was refused or reset by the server.";
    private static final String IO_5XX_TIMEOUT_ERROR_MSG = "Failed to connect to Exchange due to 5xx timeout.";
    private static final String AUTHENTICATED_ACCESS_NOT_POSSIBLE = "Authenticated access not possible, because key and secret was not initialized: use right constructor.";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeAuthentication.class);

    private long nonce = System.currentTimeMillis();

    private Gson gson = new Gson();
    private Mac mac;

    private Map<String, Object> params;

    @Autowired
    private DbService dbService;

    @Autowired
    private ApiKeysMapper apiKeysMapper;

    //Actually, api operates on 1 api keys for 1 user.
    //Further development of the application will involved
    // creation of the functionalities for many users.
    private ApiKeysDto getUserApiKeys() {
        ApiKeys apiKeys = dbService.getApiKeysById(1L);
        return apiKeysMapper.mapApiKeysToApiKeysDto(apiKeys);
    }

    public String sendExchangeRequest(String urlPath, String httpMethod)  throws IOException {
        ApiKeysDto apiKeysDto = getUserApiKeys();
        String errorMSG= "";
        HttpURLConnection conn = null;

        final StringBuilder exchangeResponse = new StringBuilder();

        try {
            URL url = new URL(bitfinexMainUrl + urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(httpMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            if (isAccessPublicOnly()) {
                LOGGER.error(AUTHENTICATED_ACCESS_NOT_POSSIBLE);
                return AUTHENTICATED_ACCESS_NOT_POSSIBLE;
            }

            if (params == null) {
                params = createRequestParamMap();
            }

            params.put("request", urlPath);
            params.put("nonce", Long.toString(getNonce()));

            String payload = gson.toJson(params);
            String payload_base64 = Base64.getEncoder().encodeToString(payload.getBytes());
            String payload_sha384hmac = hmacDigest(payload_base64, apiKeysDto.getApiSecretKey(), alhorithmHMACSHA384);

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.addRequestProperty("X-BFX-APIKEY", apiKeysDto.getApiKey());
            conn.addRequestProperty("X-BFX-PAYLOAD", payload_base64);
            conn.addRequestProperty("X-BFX-SIGNATURE", payload_sha384hmac);

            final BufferedReader responseInputStream = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String responseLine;
            while ((responseLine = responseInputStream.readLine()) != null) {
                exchangeResponse.append(responseLine);
            }

            responseInputStream.close();

            return exchangeResponse.toString();

        }catch(MalformedURLException e) {
            LOGGER.error(UNEXPECTED_IO_ERROR_MSG, e);
        }catch(SocketTimeoutException e) {
            LOGGER.error(IO_SOCKET_TIMEOUT_ERROR_MSG, e);
        }catch(FileNotFoundException | UnknownHostException e) {
            LOGGER.error(CONNECTION_ERROR, e);
        }catch(IOException e) {
            try{
                if(e.getMessage() != null) {
                    LOGGER.error(SSL_CONNECTION_REFUSED, e);
                }else if (conn != null) {
                    LOGGER.error(IO_5XX_TIMEOUT_ERROR_MSG, e);
                }else {
                    LOGGER.error(UNEXPECTED_IO_ERROR_MSG, e);

                if(conn != null) {
                    final InputStream rawErrorStream = conn.getErrorStream();

                    if(rawErrorStream != null) {
                        final BufferedReader errorInputStream = new BufferedReader(new InputStreamReader(rawErrorStream, "UTF-8"));
                        final StringBuilder errorResponse = new StringBuilder();
                        String errorLine;
                        while((errorLine = errorInputStream.readLine()) != null) {
                            errorResponse.append(errorLine);
                        }
                        errorInputStream.close();
                        errorMSG = UNEXPECTED_IO_ERROR_MSG + " ErrorStream Response: " + errorResponse;
                    }
                }
                LOGGER.error(errorMSG, e);
                }
            }catch (IOException io) {
                LOGGER.error(UNEXPECTED_IO_ERROR_MSG, e);
            }

        }finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return errorMSG;
    }

    public long getNonce() {
        return ++nonce;
    }

    public boolean isAccessPublicOnly() {
        return getUserApiKeys() == null;
    }

    public String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Exception: " + e.getMessage());
        } catch (InvalidKeyException e) {
            LOGGER.error("Exception: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Exception: " + e.getMessage());
        }
        return digest;
    }

    private Map<String, Object> createRequestParamMap() {
        return new HashMap<>();
    }
}

