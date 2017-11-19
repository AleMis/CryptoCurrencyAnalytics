CREATE TABLE symbols (id INT NOT NULL AUTO_INCREMENT, symbol VARCHAR(20), t_symbol VARCHAR(20), PRIMARY KEY (id));
INSERT INTO symbols (symbol, t_symbol) values ('BTCUSD', 'tBTCUSD'), ('ETHUSD','tETHUSD'), ('BCHUSD','tBCHUSD'), ('ETCUSD', 'tETCUSD'), ('LTCUSD', 'tLTCUSD'), ('XMRUSD','tXMRUSD'), ('XRPUSD','tXRPUSD'), ('ETHBTC','tETHBTC'), ('LTCBTC','tLTCBTC'), ('XRPBTC','tXRPBTC'), ('XMRBTC','tXMRBTC'), ('BCHBTC','tBCHBTC'), ('ETCBTC','tETCBTC');

CREATE TABLE candles(id INT NOT NULL AUTO_INCREMENT, currency_pair VARCHAR(10), time_stamp BIGINT, date VARCHAR(20), time VARCHAR(20), open DECIMAL(12,6), close DECIMAL(12,6), high DECIMAL(12,6), low DECIMAL(12,6), volume DECIMAL(20,12), time_frame VARCHAR(10), PRIMARY KEY (id));

CREATE TABLE db_updater(id INT NOT NULL AUTO_INCREMENT, currency_pair VARCHAR(10), time_frame VARCHAR(10), is_download BOOLEAN, start_timestamp_for_first_download BIGINT, end_timestamp_for_first_download BIGINT, date_of_last_update VARCHAR(20), time_of_last_update VARCHAR(20), timestamp_of_last_update BIGINT, PRIMARY KEY (id));


insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tBTCUSD', '1D', false, 1364860800000, 1510704000000, null, null, null);
insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tBTCUSD', '1h', false, 1504224000000, 1510963200000, null, null, null);
insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tETHUSD', '1D', false, 1457568000000, 1510704000000, null, null, null);
insert into db_updater(currency_pair, time_frame, is_download, start_timestamp_for_first_download, end_timestamp_for_first_download, date_of_last_update, time_of_last_update, timestamp_of_last_update) values('tETHUSD', '1h', false, 1504224000000, 1510963200000, null, null, null);
