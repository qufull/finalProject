CREATE TABLE penalties (
	id BIGSERIAL PRIMARY KEY,
	reservation_id BIGINT REFERENCES reservations(id),
	car_id BIGINT REFERENCES cars(id),
	amount DECIMAL(15,2) NOT NULL,
	reason TEXT,
	date_issued TIMESTAMP NOT NULL
);