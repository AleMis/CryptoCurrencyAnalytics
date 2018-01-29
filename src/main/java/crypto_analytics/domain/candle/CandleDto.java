package crypto_analytics.domain.candle;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CandleDto {

    private Long timeStamp;
    private String currencyPair;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double volume;
}