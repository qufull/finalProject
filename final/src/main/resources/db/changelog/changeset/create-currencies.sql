CREATE TABLE currencies (
	id BIGSERIAL PRIMARY KEY,
	currency_code VARCHAR(5) DEFAULT 'BYN',
	exchange_rate DECIMAL(15,2) NOT NULL
);