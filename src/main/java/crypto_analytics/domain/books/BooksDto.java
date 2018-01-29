package crypto_analytics.domain.books;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class BooksDto {

    private Double price;
    private Integer count;
    private Double amount;
}
