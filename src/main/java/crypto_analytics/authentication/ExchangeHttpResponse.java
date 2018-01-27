package crypto_analytics.authentication;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExchangeHttpResponse {

    private int statusCode;
    private String responseMessage;
    private String content;
}
