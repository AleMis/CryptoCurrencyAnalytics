package crypto_analytics.client.bitfinex;

import crypto_analytics.config.CoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CandleDataClient {



    @Autowired
    private RestTemplate restTemplate;

    public HashMap<String, List<Object[][]>> downloadData(HashMap<String, List<String>> requestMap) throws InterruptedException {
        String[][] requestTwoDArray;
        List<Object[][]> requestTwoDList = new ArrayList<>();
        HashMap<String, List<Object[][]>> requestTwoDArrayMap = new HashMap<>();
        for (Map.Entry<String, List<String>> requests : requestMap.entrySet()) {
            for (String request : requests.getValue()) {
                ResponseEntity<String[][]> responseEntity = restTemplate.getForEntity(request, String[][].class);
                requestTwoDArray = responseEntity.getBody();
                requestTwoDList.add(requestTwoDArray);
                requestTwoDArrayMap.put(requests.getKey(), requestTwoDList);
                Thread.sleep(6000);
            }
        }
        return requestTwoDArrayMap;
    }

    public HashMap<String, Object[][]> updateData(HashMap<String, String> requestMap) throws InterruptedException {
        String[][] requestTwoDArray;
        HashMap<String, Object[][]> map = new HashMap<>();
        for(Map.Entry<String, String> requests : requestMap.entrySet()) {
            ResponseEntity<String[][]> responseEntity = restTemplate.getForEntity(requests.getValue(), String[][].class);
            requestTwoDArray = responseEntity.getBody();
            map.put(requests.getKey(), requestTwoDArray);
            Thread.sleep(6000);
        }
        return map;
    }
}