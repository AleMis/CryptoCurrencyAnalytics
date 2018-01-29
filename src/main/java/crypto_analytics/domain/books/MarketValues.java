package crypto_analytics.domain.books;


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
