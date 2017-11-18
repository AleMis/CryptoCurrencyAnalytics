CREATE TABLE symbols (id INT NOT NULL AUTO_INCREMENT, symbol VARCHAR(20), t_symbol VARCHAR(20), PRIMARY KEY (id));
INSERT INTO symbols (symbol, t_symbol) values ('BTCUSD', 'tBTCUSD'), ('ETHUSD','tETHUSD'), ('BCHUSD','tBCHUSD'), ('ETCUSD', 'tETCUSD'), ('LTCUSD', 'tLTCUSD'), ('XMRUSD','tXMRUSD'), ('XRPUSD','tXRPUSD'), ('ETHBTC','tETHBTC'), ('LTCBTC','tLTCBTC'), ('XRPBTC','tXRPBTC'), ('XMRBTC','tXMRBTC'), ('BCHBTC','tBCHBTC'), ('ETCBTC','tETCBTC');

CREATE TABLE candles(id INT NOT NULL AUTO_INCREMENT, currency_pair VARCHAR(10), time_stamp BIGINT, date VARCHAR(20), time VARCHAR(20), open DECIMAL(12,6), close DECIMAL(12,6), high DECIMAL(12,6), low DECIMAL(12,6), volume DECIMAL(20,12), time_frame VARCHAR(10), PRIMARY KEY (id));

CREATE TABLE db_updater(id INT NOT NULL AUTO_INCREMENT, currency_pair VARCHAR(10), time_frame VARCHAR(10), is_download BOOLEAN, start_timestamp_for_first_download BIGINT, end_timestamp_for_first_download BIGINT, date_of_last_update VARCHAR(20), time_of_last_update VARCHAR(20), timestamp_of_last_update BIGINT, PRIMARY KEY (id));


update db_updater set timestamp_of_last_update = 1510704000000 where id = 3;