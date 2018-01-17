package crypto_analytics.domain.bitfinex.accountbalance;

import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountBalanceHistoryDto {

    private String currency;
    private BigDecimal amount;
    private BigDecimal balance;
    private String description;
    private Long timestamp;
}
