package crypto_analytics.authentication;

import com.google.gson.Gson;
import crypto_analytics.domain.bitfinex.params.ParamsModerator;
import crypto_analytics.domain.bitfinex.apikey.ApiKeys;
import crypto_analytics.domain.bitfinex.apikey.ApiKeysDto;
import crypto_analytics.mapper.bitfinex.ApiKeysMapper;
import crypto_analytics.service.bitfinex.DbService;
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
@Component
public class ExchangeAuthentication {

    @Value("${bitfinex.main.url}")
    private String bitfinexMainUrl;

    @Value("${algorithm.hmac}")
    private String algorithmHMACSHA384;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeAuthentication.class);

    private long nonce = System.currentTimeMillis();
    private int connectionTimeout;

    private Gson gson = new Gson();
    private Mac mac;

    private Map<String, Object> params;

    @Autowired
    private DbService dbService;

    @Autowired
    private ApiKeysMapper apiKeysMapper;

    @Autowired
    private RequestParamsModifier responseParamsModifier;

    //Actually, api operates on 1 api key for 1 user.
    //Further development of the application will create
    // necessity for developing of functionalities for many users.
    private ApiKeysDto getUserApiKeys() {
        ApiKeys apiKeys = dbService.getApiKeysById(1L);
        return apiKeysMapper.mapApiKeysToApiKeysDto(apiKeys);
    }

    public ExchangeHttpResponse sendExchangeRequest(String urlPath, String httpMethod, ParamsModerator accountBalanceModerator)  throws IOException {
        ApiKeysDto apiKeysDto = getUserApiKeys();
        String errorMSG= "";
        HttpURLConnection conn = null;

        final StringBuilder exchangeResponse = new StringBuilder();

        try {
            URL url = new URL(bitfinexMainUrl + urlPath);

            LOGGER.debug("Using following URL for API call: " + url);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(httpMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            if (isAccessPublicOnly()) {
                LOGGER.error(ExchangeConnectionExceptions.AUTHENTICATED_ACCESS_NOT_POSSIBLE.getException());
            }

            if (params == null) {
                params = responseParamsModifier.modifyRequestParamMap(accountBalanceModerator);
            }

            params.put("request", urlPath);
            params.put("nonce", Long.toString(getNonce()));


            String payload = gson.toJson(params);
            String payloadBase64 = Base64.getEncoder().encodeToString(payload.getBytes("UTF-8"));
            String payloadSha384hmac = hmacDigest(payloadBase64, apiKeysDto.getApiSecretKey(), algorithmHMACSHA384);

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.addRequestProperty("X-BFX-APIKEY", apiKeysDto.getApiKey());
            conn.addRequestProperty("X-BFX-PAYLOAD", payloadBase64);
            conn.addRequestProperty("X-BFX-SIGNATURE", payloadSha384hmac);

            final int timeoutInMillis = connectionTimeout * 1000;
            conn.setConnectTimeout(timeoutInMillis);
            conn.setReadTimeout(timeoutInMillis);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while((n = conn.getInputStream().read(buf)) >= 0) {
                baos.write(buf, 0, n);
            }
            byte[] content = baos.toByteArray();

            final BufferedReader responseInputStream = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String responseLine;
            while ((responseLine = responseInputStream.readLine()) != null) {
                exchangeResponse.append(responseLine);
            }

            System.out.println(exchangeResponse);
            System.out.println("conn to String " + conn.toString());
            responseInputStream.close();

            return new ExchangeHttpResponse(conn.getResponseCode(), conn.getResponseMessage(), content);

        }catch(MalformedURLException e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        }catch(SocketTimeoutException e) {
            LOGGER.error(ExchangeConnectionExceptions.IO_SOCKET_TIMEOUT_ERROR_MSG.getException(), e);
        }catch(FileNotFoundException | UnknownHostException e) {
            LOGGER.error(ExchangeConnectionExceptions.CONNECTION_ERROR.getException(), e);
        }catch(IOException e) {
            try{
                if(e.getMessage() != null) {
                    LOGGER.error(ExchangeConnectionExceptions.SSL_CONNECTION_REFUSED.getException(), e);
                }else if (conn != null) {
                    LOGGER.error(ExchangeConnectionExceptions.IO_5XX_TIMEOUT_ERROR_MSG.getException(), e);
                }else {
                    LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);

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
                        errorMSG = ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException() + " ErrorStream Response: " + errorResponse;
                    }
                }
                LOGGER.error(errorMSG, e);
                }
            }catch (IOException io) {
                LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            }

        }finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return new ExchangeHttpResponse(0, null, null);
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
            LOGGER.error(ExchangeConnectionExceptions.ENCODING_ERROR.getException(), e);
        } catch (InvalidKeyException e) {
            LOGGER.error(ExchangeConnectionExceptions.INVALID_KEY_ERROR.getException(), e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(ExchangeConnectionExceptions.LACK_OF_ALGORITHM_ERROR.getException(), e);
        }
        return digest;
    }
}

