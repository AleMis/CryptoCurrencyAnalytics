package crypto_analytics.mapper;

import crypto_analytics.domain.candle.CandleDto;
import org.springframework.stereotype.Component;

@Component
public class CandleMapper {


    public CandleDto[][] mapToCandletDtoArrays(Object[][] objectArray) {
        CandleDto[][] candleArray = new CandleDto[objectArray.length][];

        for(int i = 0; i<objectArray.length; i++ ) {
            for (int j = 0; j<objectArray[i].length; j++) {
                    candleArray[i][j] = (CandleDto) objectArray[i][j];
                }
            }

        return candleArray;
    }

}
