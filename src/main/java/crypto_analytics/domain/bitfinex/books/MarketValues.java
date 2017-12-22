package crypto_analytics.domain.bitfinex.books;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MarketValues {

    private String[][] asks;
    private String[][] bids;
}
