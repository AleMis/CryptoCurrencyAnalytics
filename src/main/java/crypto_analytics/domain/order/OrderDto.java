package crypto_analytics.domain.order;

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
    private boolean isOcoOrder;
    private BigDecimal ocoStopOrder;

}
