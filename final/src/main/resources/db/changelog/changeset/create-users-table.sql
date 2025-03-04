CREATE TYPE user_status AS ENUM ('ACTIVE', 'BLOCKED');
CREATE TABLE users (
	id BIGSERIAL PRIMARY KEY ,
	first_name VARCHAR(100),
	last_name VARCHAR(100),
	phone_number VARCHAR(15),
	registration_date TIMESTAMP DEFAULT NOW()	,
	status user_status DEFAULT 'ACTIVE',
	balance DECIMAL(15,2) DEFAULT 0.0,
    currency VARCHAR(10) DEFAULT 'BYN'
);