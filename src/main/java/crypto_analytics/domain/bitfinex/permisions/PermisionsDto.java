package crypto_analytics.domain.bitfinex.permisions;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PermisionsDto {

    private String resource;
    private PermisionsParametersDto parameters;
}
