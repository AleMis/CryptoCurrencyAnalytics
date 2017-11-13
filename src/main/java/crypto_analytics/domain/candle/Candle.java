package crypto_analytics.domain.candle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;


@NamedNativeQueries({
        @NamedNativeQuery(
            name = "Candle.getLastDateForCurrency",
            query =  "SELECT MAX(time_stamp) FROM candles WHERE currency_pair = :CURRENCYPAIR AND time_frame = :TIMEFRAME"
        )})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name="candles")
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
