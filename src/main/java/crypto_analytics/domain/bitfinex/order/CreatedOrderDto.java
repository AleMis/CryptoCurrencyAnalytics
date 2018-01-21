package crypto_analytics.domain.bitfinex.order;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreatedOrderDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("cid")
    private Long cid;

    @JsonProperty("cid_date")
    private String cid_date;

    @JsonProperty("gid")
    private String gid;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("exchange")
    private String exchange;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("avg_execution_price")
    private BigDecimal avgExecutionPrice;

    @JsonProperty("side")
    private String side;

    @JsonProperty("type")
    private String type;

    @JsonProperty("timestamp")
    private BigDecimal timestamp;

    @JsonProperty("is_live")
    private Boolean isLive;

    @JsonProperty("is_cancelled")
    private Boolean isCancelled;

    @JsonProperty("is_hidden")
    private Boolean isHidden;

    @JsonProperty("oco_order")
    private String ocoOrder;

    @JsonProperty("was_forced")
    private Boolean wasForced;

    @JsonProperty("original_amount")
    private BigDecimal originalAmount;

    @JsonProperty("remaining_amount")
    private BigDecimal remainingAmount;

    @JsonProperty("executed_amount")
    private BigDecimal executedAmount;

    @JsonProperty("src")
    private String src;

    @JsonProperty("order_id")
    private Long orderId;

}
