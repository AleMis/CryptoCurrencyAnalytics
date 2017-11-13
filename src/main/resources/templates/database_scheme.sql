CREATE TABLE symbols (id INT NOT NULL AUTO_INCREMENT, symbol VARCHAR(20), t_symbol VARCHAR(20), PRIMARY KEY (id));
INSERT INTO symbols (symbol, t_symbol) values ('BTCUSD', 'tBTCUSD'), ('ETHUSD','tETHUSD'), ('BCHUSD','tBCHUSD'), ('ETCUSD', 'tETCUSD'), ('LTCUSD', 'tLTCUSD'), ('XMRUSD','tXMRUSD'), ('XRPUSD','tXRPUSD'), ('ETHBTC','tETHBTC'), ('LTCBTC','tLTCBTC'), ('XRPBTC','tXRPBTC'), ('XMRBTC','tXMRBTC'), ('BCHBTC','tBCHBTC'), ('ETCBTC','tETCBTC');

CREATE TABLE candles(id INT NOT NULL AUTO_INCREMENT, currency_pair VARCHAR(10), time_stamp BIGINT, date VARCHAR(20), time VARCHAR(20), open DECIMAL(12,6), close DECIMAL(12,6), high DECIMAL(12,6), low DECIMAL(12,6), volume DECIMAL(20,12), PRIMARY KEY (id));