CREATE TYPE reservation_status AS ENUM ('ACTIVE', 'COMPLETED', 'CANCELLED');

CREATE TABLE reservations (
	id BIGSERIAL PRIMARY KEY,
	user_id BIGINT REFERENCES users(id),
	car_id BIGINT REFERENCES cars(id),
	start_time TIMESTAMP DEFAULT NOW(),
	end_time TIMESTAMP NOT NULL,
	status reservation_status NOT NULL,
	total_cost DECIMAL(15,2) NOT NULL
);