CREATE TABLE candles(id INT NOT NULL AUTO_INCREMENT, currency_pair VARCHAR(10), time_stamp BIGINT, date VARCHAR(20), time VARCHAR(20), open DECIMAL(12,6), close DECIMAL(12,6), high DECIMAL(12,6), low DECIMAL(12,6), volume DECIMAL(20,12), time_frame VARCHAR(10), PRIMARY KEY (id));

CREATE TABLE db_updater(id INT NOT NULL AUTO_INCREMENT, currency_pair VARCHAR(10), time_frame VARCHAR(10), is_download BOOLEAN, start_timestamp_for_first_download BIGINT, end_timestamp_for_first_download BIGINT, date_of_last_update VARCHAR(20), time_of_last_update VARCHAR(20), timestamp_of_last_update BIGINT, PRIMARY KEY (id));

CREATE TABLE books(id INT NOT NULL AUTO_INCREMENT, currency_pair VARCHAR(10), price DECIMAL(12,6), count DECIMAL(12,6), amount DECIMAL(12,6), PRIMARY KEY (id));

CREATE TABLE tickers(id INT NOT NULL AUTO_INCREMENT, currency_pair VARCHAR(10), bid DECIMAL(14,6), bid_size DECIMAL(14,6), ask DECIMAL(14,6), ask_size DECIMAL(14,6), daily_change DECIMAL(14,6), daily_change_perc DECIMAL(14,6), last_price DECIMAL(14,6), volume DECIMAL(14,6), high DECIMAL(14,6), low DECIMAL(14,6), PRIMARY KEY (id));

insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tBTCUSD', '1D', false, 1364860800000, 1512777600000, null, null, null);
insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tBTCUSD', '1h', false, 1508284800000, 1512777600000, null, null, null);

insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tETHUSD', '1D', false, 1457568000000, 1512777600000, null, null, null);
insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tETHUSD', '1h', false, 1508284800000, 1512777600000, null, null, null);

insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tBCHUSD', '1D', false, 1501718400000, 1512777600000, null, null, null);
insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tBCHUSD', '1h', false, 1508284800000, 1512777600000, null, null, null);

insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tLTCUSD', '1D', false, 1369008000000, 1512777600000, null, null, null);
insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tLTCUSD', '1h', false, 1508284800000, 1512777600000, null, null, null);

insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tETCUSD', '1D', false, 1469577600000, 1512777600000, null, null, null);
insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tETCUSD', '1h', false, 1508284800000, 1512777600000, null, null, null);

insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tZECUSD', '1D', false, 1477785600000, 1512777600000, null, null, null);
insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tZECUSD', '1h', false, 1508284800000, 1512777600000, null, null, null);

insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tXMRUSD', '1D', false, 1480550400000, 1512777600000, null, null, null);
insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tXMRUSD', '1h', false, 1480550400000, 1512777600000, null, null, null);

