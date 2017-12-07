package crypto_analytics.domain.bitfinex.ticker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Ticker {

    private Long id;
    private String mid;
    private String bid;
    private String ask;
    private String last_price;
    private String low;
    private String high;
    private String volume;
    private String timestamp;
}
