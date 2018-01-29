package crypto_analytics.domain.candle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@AllArgsConstructor
@Getter
@Setter
public class CandleKeyParameters {

    private String currencyPair;
    private String timeFrame;
}
