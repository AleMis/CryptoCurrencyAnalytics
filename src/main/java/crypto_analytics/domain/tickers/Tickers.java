package crypto_analytics.domain.tickers;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamedNativeQuery(
        name = "Tickers.deleteAllTickersData",
        query = "DELETE FROM tickers",
        resultClass = Tickers.class
)

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tickers")
public class Tickers {

    @Id
    @NotNull
    @GeneratedValue
    @Column(name="id")
    private long id;

    @Column(name="currency_pair")
    private String currencyPair;

    @Column(name="bid")
    private Double bid;

    @Column(name="bid_size")
    private Double bidSize;

    @Column(name="ask")
    private Double ask;

    @Column(name="ask_size")
    private Double askSize;

    @Column(name="daily_change")
    private Double dailyChange;

    @Column(name="daily_change_perc")
    private Double dailyChangePerc;

    @Column(name="last_price")
    private Double lastPrice;

    @Column(name="volume")
    private Double volume;

    @Column(name="high")
    private Double high;

    @Column(name="low")
    private Double low;

}
