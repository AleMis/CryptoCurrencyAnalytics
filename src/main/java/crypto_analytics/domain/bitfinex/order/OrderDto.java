package crypto_analytics.domain.bitfinex.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDto {

    private String symbol;
    private BigDecimal amount;
    private BigDecimal price;
    private String side;
    private String type;
    private String exchange;
    private boolean isHidden;
    private boolean isPostonly;
    private boolean ocoOrder;
    private BigDecimal ocoStopOrder;

}
