package crypto_analytics.domain.bitfinex.dbupdater;

import lombok.*;

import javax.persistence.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="db_updater")
public class DbUpdater {

    @Id
    @Column(name="id")
    private Long id;

    @Column(name="currency_pair")
    private String currencyPair;

    @Column(name="time_frame")
    private String timeFrame;

    @Column(name="is_download")
    private Boolean isDownload;

    @Column(name="start_timestamp_for_first_download")
    private Long startTimestampForFirstDownload;

    @Column(name="end_timestamp_for_first_download")
    private Long endTimestampForFirstDownload;

    @Column(name="date_of_last_update")
    private String updateDate;

    @Column(name="time_of_last_update")
    private String updateTime;

    @Column(name="timestamp_of_last_update")
    private Long updateTimestamp;
}
