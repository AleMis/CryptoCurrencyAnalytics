package crypto_analytics.domain.bitfinex.books;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MarketValues {

    private String[][] asks;
    private String[][] bids;
}
