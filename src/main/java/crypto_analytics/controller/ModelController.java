package crypto_analytics.controller;

import crypto_analytics.domain.candle.CandleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelController {

    public List<Object[][]> downloadedData(RestTemplate restTemplate, List<String> requestList) throws InterruptedException {
        String[][] requestTwoDArray = null;
        List<Object[][]> requestTwoDArrayList = new ArrayList<>();
        for(String request : requestList) {
            ResponseEntity<String[][]> responseEntity = restTemplate.getForEntity(request, String[][].class);
            requestTwoDArray = responseEntity.getBody();
            pring(requestTwoDArray);
            requestTwoDArrayList.add(requestTwoDArray);
            Thread.sleep(6000);
        }
        return requestTwoDArrayList;
    }

    private void pring(Object[][] objects) {
        for(int i = 0; i<objects.length; i++) {
            for(int j = 0; j<objects[i].length; j++) {
                System.out.println(objects[i][j]);
            }
        }
    }
}
