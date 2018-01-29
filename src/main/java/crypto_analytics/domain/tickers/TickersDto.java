package crypto_analytics.domain.tickers;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TickersDto {

    private String currencyPair;
    private Double bid;
    private Double bidSize;
    private Double ask;
    private Double askSize;
    private Double dailyChange;
    private Double dailyChangePerc;
    private Double lastPrice;
    private Double volume;
    private Double high;
    private Double low;

}
