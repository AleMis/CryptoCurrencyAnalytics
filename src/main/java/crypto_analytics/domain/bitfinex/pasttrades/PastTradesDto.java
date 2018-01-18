package crypto_analytics.domain.bitfinex.pasttrades;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@ToString
public class PastTradesDto {

    private BigDecimal price;
    private BigDecimal amount;
    private Long timestamp;
    private String exchange;
    private String type;
    private String feeCurrency;
    private BigDecimal feeAmount;
    private Long tid;
    private Long orderId;

}
