package crypto_analytics.domain.bitfinex.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class ParamsToSearch {

    private String currency;
    private Long sinceTimestamp;
}
