package crypto_analytics.domain.bitfinex.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class ParamsModerator {

    private String paramType;
    private ParamsToSearch accountBalanceSearcher;
}
