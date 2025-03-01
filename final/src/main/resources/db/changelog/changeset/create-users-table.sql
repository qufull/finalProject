CREATE TYPE user_status AS ENUM ('ACTIVE', 'BLOCKED');
CREATE TABLE users (
	id BIGSERIAL PRIMARY KEY ,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,	
	phone_number VARCHAR(15) NOT NULL,
	registration_date TIMESTAMP DEFAULT NOW()	,
	status user_status DEFAULT 'ACTIVE',
	balance DECIMAL(15,2) DEFAULT 0.0
	
);