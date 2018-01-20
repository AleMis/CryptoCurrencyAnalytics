package crypto_analytics.domain.bitfinex.order;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatedOrderDto {

    @JsonProperty("id")
    private Long orderId;

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
    private Long timestamp;

    @JsonProperty("is_live")
    private Boolean isLive;

    @JsonProperty("is_cancelled")
    private Boolean isCancelled;

    @JsonProperty("is_hidden")
    private Boolean isHidden;

    @JsonProperty("was_forced")
    private Boolean wasForced;

    @JsonProperty("original_amount")
    private BigDecimal originalAmount;

    @JsonProperty("remaining_amount")
    private BigDecimal remainingAmount;

    @JsonProperty("executed_amount")
    private BigDecimal executedAmount;

}
