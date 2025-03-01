CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

CREATE TABLE roles (
	id BIGSERIAL PRIMARY KEY,
	role user_role NOT NULL
);