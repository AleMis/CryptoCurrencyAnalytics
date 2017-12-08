package crypto_analytics.domain.bitfinex.candle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CandleChartDto {

    private Long date;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double volume;

}