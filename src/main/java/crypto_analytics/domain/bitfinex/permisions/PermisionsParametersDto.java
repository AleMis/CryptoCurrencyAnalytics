package crypto_analytics.domain.bitfinex.permisions;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PermisionsParametersDto {

    private boolean read;
    private boolean write;
}
