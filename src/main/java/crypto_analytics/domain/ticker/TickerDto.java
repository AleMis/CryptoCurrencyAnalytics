package crypto_analytics.domain.ticker;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class TickerDto {

    private Long id;
    private String mid;
    private String bid;
    private String ask;
    private String last_price;
    private String low;
    private String high;
    private String volume;
    private String timestamp;
}
