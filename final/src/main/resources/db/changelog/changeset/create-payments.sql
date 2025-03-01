CREATE TYPE payment_type AS ENUM ('PENALTY', 'REPLENISHMENT');
CREATE TYPE payment_status AS ENUM ('APPROVED', 'REJECTED');

CREATE TABLE payments (
	id BIGSERIAL PRIMARY KEY,
	user_id BIGINT REFERENCES users(id),
	currency_id BIGINT REFERENCES currencies(id),
	amount DECIMAL(15,2) NOT NULL,
	payment_date TIMESTAMP DEFAULT NOW(),
	payment_type payment_type NOT NULL,
	status payment_status NOT NULL
);