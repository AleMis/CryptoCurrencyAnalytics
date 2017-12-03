package crypto_analytics.domain.candle;

import lombok.*;

import javax.persistence.*;



@NamedNativeQueries({
        @NamedNativeQuery(
            name = "Candle.getLastDateForCurrency",
            query =  "SELECT MAX(time_stamp) FROM candles WHERE currency_pair = :CURRENCY_PAIR AND time_frame = :TIME_FRAME"
        ),
        @NamedNativeQuery(
            name = "Candle.checkFirstDate",
            query = "SELECT time_stamp, currency_pair, time_frame FROM candles WHERE currency_pair = :CURRENCY_PAIR AND time_frame = :TIME_FRAME AND time_stamp = :TIME_STAMP"
        ),
        @NamedNativeQuery(
                name = "Candle. getCandlesByCurrencyPairAndTimeFrame",
                query = "SELECT * FROM candles WHERE currency_pair = :CURRENCY_PAIR AND time_frame = :TIME_FRAME"
        )
})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name="candles")
public class Candle {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @Column(name="currency_pair")
    private String currencyPair;

    @Column(name="time_stamp")
    private Long timeStamp;

    @Column(name="date")
    private String localDate;

    @Column(name="time")
    private String time;

    @Column(name="open")
    private Double open;

    @Column(name="close")
    private Double close;

    @Column(name="high")
    private Double high;

    @Column(name="low")
    private Double low;

    @Column(name="volume")
    private Double volume;

    @Column(name="time_frame")
    private String timeFrame;

}
