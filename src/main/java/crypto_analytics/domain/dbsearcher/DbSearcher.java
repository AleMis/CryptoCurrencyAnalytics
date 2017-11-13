package crypto_analytics.domain.dbsearcher;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DbSearcher {

    private Long timestamp;
    private String currencyPair;
    private String timeFrame;
}
