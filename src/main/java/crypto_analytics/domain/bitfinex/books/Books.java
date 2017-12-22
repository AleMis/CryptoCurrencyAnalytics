package crypto_analytics.domain.bitfinex.books;

import lombok.*;
import org.springframework.data.jpa.repository.Modifying;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@NamedNativeQuery(
        name="Books.deleteBooksByCurrencyPair",
        query="DELETE FROM books WHERE currency_pair=:CURRENCY_PAIR",
        resultClass = Books.class
)


@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name="books")
public class Books {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @Column(name="currency_pair")
    private String currencyPair;

    @Column(name="price")
    private Double price;

    @Column(name="count")
    private Integer count;

    @Column(name="amount")
    private Double amount;
}
