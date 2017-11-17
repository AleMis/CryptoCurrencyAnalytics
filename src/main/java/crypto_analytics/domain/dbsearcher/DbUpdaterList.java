package crypto_analytics.domain.dbsearcher;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Component
public class DbUpdaterList {

    private List<String> downloadList;
    private List<String> updateList;
}
