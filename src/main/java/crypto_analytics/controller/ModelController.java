package crypto_analytics.controller;

import crypto_analytics.domain.dbupdater.DbUpdaterContainer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelController {

    public List<Object[][]> downloadedData(RestTemplate restTemplate, DbUpdaterContainer dbUpdaterList) throws InterruptedException {
        List<Object[][]> requestTwoDArrayList = new ArrayList<>();
        requestTwoDArrayList.addAll(returnTwoArrayObjectList(restTemplate, dbUpdaterList.getDownloadList()));
        requestTwoDArrayList.addAll(returnTwoArrayObjectList(restTemplate, dbUpdaterList.getUpdateList()));
        return requestTwoDArrayList;
    }

    private void pring(Object[][] objects) {
        for(int i = 0; i<objects.length; i++) {
            for(int j = 0; j<objects[i].length; j++) {
                System.out.println(objects[i][j]);
            }
        }
    }

    private List<Object[][]> returnTwoArrayObjectList(RestTemplate restTemplate, List<String> list) throws InterruptedException {
        String[][] requestTwoDArray = null;
        List<Object[][]> requestTwoDArrayList = new ArrayList<>();
        for(String request : list) {
            ResponseEntity<String[][]> responseEntity = restTemplate.getForEntity(request, String[][].class);
            requestTwoDArray = responseEntity.getBody();
            pring(requestTwoDArray);
            requestTwoDArrayList.add(requestTwoDArray);
            Thread.sleep(6000);
        }
        return requestTwoDArrayList;
    }
}
