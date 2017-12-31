package crypto_analytics.domain.bitfinex.accountbalance;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountBalanceDto {

    public String type;
    public String currency;
    public BigDecimal amount;
    public BigDecimal available;
}
