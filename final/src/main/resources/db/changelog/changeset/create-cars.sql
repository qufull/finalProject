CREATE TYPE car_status AS ENUM('AVAILABLE', 'RENTED', 'UNAVAILABLE');
CREATE TYPE car_type AS ENUM('ECONOMY', 'STANDART', 'COMFORT', 'MINIVAN', 'ELECTRIC', 'PREMIUM', 'CABRIOLET', 'SPORT', 'SUV');

CREATE TABLE cars (
                      id BIGSERIAL PRIMARY KEY,
                      make VARCHAR(30) NOT NULL,
                      model VARCHAR(30) NOT NULL,
                      year VARCHAR(5) NOT NULL,
                      status car_status NOT NULL,
                      type car_type NOT NULL,
                      price_per_minute DECIMAL(5,2) NOT NULL,
                      rental_point_id BIGINT REFERENCES rental_points(id)
);