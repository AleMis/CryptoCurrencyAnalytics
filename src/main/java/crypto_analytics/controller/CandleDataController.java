package crypto_analytics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CandleDataController {

    public HashMap<String, List<Object[][]>> downloadData(RestTemplate restTemplate, HashMap<String, List<String>> requestMap) throws InterruptedException {
        String[][] requestTwoDArray = null;
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

    public HashMap<String, Object[][]> updateData(RestTemplate restTemplate, HashMap<String, String> requestMap) throws InterruptedException {
        String[][] requestTwoDArray = null;
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
