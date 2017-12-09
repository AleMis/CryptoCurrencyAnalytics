package crypto_analytics.domain.bitfinex;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@AllArgsConstructor
@Getter
@Setter
public class KeyParameters {

    private String currencyPair;
    private String timeFrame;
}
