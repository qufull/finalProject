
CREATE TABLE driver_licenses (
	id BIGSERIAL PRIMARY KEY,
	user_id BIGINT REFERENCES users(id) UNIQUE,
	number VARCHAR(100) NOT NULL,
	data_of_issue TIMESTAMP NOT NULL,
	data_of_expity TIMESTAMP NOT NULL,
	have_b BOOL NOT NULL
);