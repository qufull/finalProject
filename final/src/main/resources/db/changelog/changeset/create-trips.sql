CREATE TABLE trips (
		id BIGSERIAL PRIMARY KEY,
		reservation_id BIGINT REFERENCES reservations(id),
		car_id BIGINT REFERENCES cars(id),
		distance_km DECIMAL(5,2) DEFAULT 0.0
	);
