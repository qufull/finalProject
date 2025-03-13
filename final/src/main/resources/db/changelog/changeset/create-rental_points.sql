CREATE TABLE rental_points
(
    id              BIGSERIAL PRIMARY KEY,
    point_name      VARCHAR(50)     NOT NULL,
    location        VARCHAR(64)     NOT NULL
);