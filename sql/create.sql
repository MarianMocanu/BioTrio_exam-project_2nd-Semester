CREATE DATABASE IF NOT EXISTS biotrio_teamtwo;

CREATE USER IF NOT EXISTS 'biotrio_teamtwo'@'localhost' IDENTIFIED BY '12345678';

GRANT ALL PRIVILEGES ON biotrio_teamtwo.* TO 'biotrio_teamtwo'@'localhost';

USE biotrio_teamtwo;

CREATE TABLE movies
(
    id              INT NOT NULL AUTO_INCREMENT UNIQUE,
    title           VARCHAR(140) NOT NULL,
    runtime         INT,
    synopsis        TEXT,
    genre           VARCHAR(140),
    language        VARCHAR(140),
    subtitles       VARCHAR(140),
    trailer_link    VARCHAR(140),
    release_date    DATE,
    cast            VARCHAR(140),
    director        VARCHAR(140),
    age_restriction VARCHAR(140),
    poster          VARCHAR(140),
    PRIMARY KEY (id)
);

CREATE TABLE theaters
(
    id            INT NOT NULL AUTO_INCREMENT UNIQUE,
    name          VARCHAR(140) NOT NULL,
    no_of_rows    INT NOT NULL,
    seats_per_row INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE screenings
(
    id         INT NOT NULL AUTO_INCREMENT UNIQUE,
    movie_id   INT NOT NULL,
    theater_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (movie_id) REFERENCES movies (id),
    FOREIGN KEY (theater_id) REFERENCES theaters (id)
);

CREATE TABLE bookings
(
    id           INT NOT NULL AUTO_INCREMENT UNIQUE,
    phone_no     VARCHAR(140) NOT NULL,
    code         VARCHAR(140) NOT NULL,
    screening_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (screening_id) REFERENCES screenings (id) ON DELETE CASCADE
);

CREATE TABLE booked_seats
(
    booking_id INT NOT NULL,
    row_no     INT NOT NULL,
    seat_no    INT NOT NULL,
    PRIMARY KEY (booking_id, row_no, seat_no),
    FOREIGN KEY (booking_id) REFERENCES bookings (id) ON DELETE CASCADE
);

CREATE TABLE employees
(
    id         INT NOT NULL AUTO_INCREMENT UNIQUE,
    first_name VARCHAR(140) NOT NULL,
    last_name  VARCHAR(140) NOT NULL
);

CREATE TABLE roles
(
    id   INT NOT NULL AUTO_INCREMENT UNIQUE,
    name VARCHAR(140) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users
(
    id          INT NOT NULL AUTO_INCREMENT UNIQUE,
    username    VARCHAR(140) UNIQUE NOT NULL,
    password    VARCHAR(140),
    role        INT NOT NULL,
    employee_id INT,
    PRIMARY KEY (id),
    FOREIGN KEY (role) REFERENCES roles (id),
    FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE SET NULL
);

CREATE TABLE tickets
(
    id           INT NOT NULL AUTO_INCREMENT UNIQUE,
    screening_id INT NOT NULL,
    row_no       INT NOT NULL,
    seat_no      INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (screening_id) REFERENCES screenings (id) ON DELETE CASCADE
);

CREATE TABLE upcoming_movies
(
    movie_id       INT UNIQUE NOT NULL,
    estimated_date DATE NULL,
    PRIMARY KEY (movie_id),
    FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE
);

CREATE TABLE technologies
(
    id INT UNIQUE AUTO_INCREMENT,
    name VARCHAR(140) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE technologies_to_movies
(
    technology_id INT NOT NULL,
    movie_id INT NOT NULL,
    PRIMARY KEY (technology_id, movie_id),
    FOREIGN KEY (technology_id) REFERENCES technologies(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE technologies_to_theaters
(
    technology_id INT NOT NULL,
    theater_id INT NOT NULL,
    PRIMARY KEY (technology_id, theater_id),
    FOREIGN KEY (technology_id) REFERENCES technologies(id) ON DELETE CASCADE,
    FOREIGN KEY (theater_id) REFERENCES theaters(id) ON DELETE CASCADE
);

INSERT INTO roles VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_MANAGER'), (3, 'ROLE_USER');


