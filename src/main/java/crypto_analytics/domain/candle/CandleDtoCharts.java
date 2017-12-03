package crypto_analytics.domain.candle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CandleDtoCharts {

    private String date;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double volume;

}
