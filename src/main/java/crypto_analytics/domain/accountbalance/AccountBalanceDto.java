package crypto_analytics.domain.accountbalance;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountBalanceDto {

    private String type;
    private String currency;
    private BigDecimal amount;
    private BigDecimal available;
}
