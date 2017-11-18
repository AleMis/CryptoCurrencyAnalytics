package crypto_analytics.domain.dbupdater;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Component
public class DbUpdaterContainer {

    private List<String> downloadList;
    private List<String> updateList;
}
