package crypto_analytics.domain.symbol;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity(name="symbols")
public class Symbol {
    @Id
    @Column(name="id")
    private Long id;

    @Column(name="symbol")
    private String symbol;

    @Column(name="t_symbol")
    private String tSymbol;

}
