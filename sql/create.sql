CREATE TABLE movies
(
    id              INT NOT NULL AUTO_INCREMENT UNIQUE,
    title           VARCHAR(255),
    runtime         INT,
    synopsis        TEXT,
    genre           VARCHAR(255),
    language        VARCHAR(255),
    subtitles       VARCHAR(255),
    projection_type VARCHAR(255),
    trailer_link    VARCHAR(255),
    release_date    DATE,
    cast            VARCHAR(255),
    director        VARCHAR(255),
    age_restriction VARCHAR(255),
    poster          VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE theaters
(
    id            INT NOT NULL AUTO_INCREMENT UNIQUE,
    name          VARCHAR(255),
    no_of_rows    INT,
    seats_per_row INT,
    PRIMARY KEY (id)
);

CREATE TABLE screenings
(
    id         INT NOT NULL AUTO_INCREMENT UNIQUE,
    movie_id   INT,
    theater_id INT,
    start_time DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (movie_id) REFERENCES movies (id),
    FOREIGN KEY (theater_id) REFERENCES theaters (id)
);

CREATE TABLE bookings
(
    id           INT NOT NULL AUTO_INCREMENT UNIQUE,
    phone_no     VARCHAR(255),
    code         VARCHAR(255),
    screening_id INT,
    PRIMARY KEY (id),
    FOREIGN KEY (screening_id) REFERENCES screenings (id)
);

CREATE TABLE booked_seats
(
    booking_id INT,
    row_no     INT,
    seat_no    INT,
    PRIMARY KEY (booking_id, row_no, seat_no)
);

CREATE TABLE employees
(
    id         INT NOT NULL AUTO_INCREMENT UNIQUE,
    first_name VARCHAR(255),
    last_name  VARCHAR(255)
);

CREATE TABLE roles
(
    id   INT NOT NULL AUTO_INCREMENT UNIQUE,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE users
(
    id          INT NOT NULL AUTO_INCREMENT UNIQUE,
    username    VARCHAR(255) UNIQUE,
    password    VARCHAR(255),
    role        INT,
    employee_id INT,
    PRIMARY KEY (id),
    FOREIGN KEY (role) REFERENCES roles (id),
    FOREIGN KEY (employee_id) REFERENCES employees (id)
);

CREATE TABLE tickets
(
    id           INT NOT NULL AUTO_INCREMENT UNIQUE,
    screening_id INT,
    row_no       INT NOT NULL,
    seat_no      INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (screening_id) REFERENCES screenings (id)
);

CREATE TABLE upcoming_movies
(
    movie_id       INT UNIQUE NOT NULL,
    estimated_date DATE NULL,
    PRIMARY KEY (movie_id),
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);

CREATE TABLE technologies
(
    id INT UNIQUE AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE technologies_to_movies
(
    technology_id INT NOT NULL,
    movie_id INT NOT NULL,
    PRIMARY KEY (technology_id, movie_id),
    FOREIGN KEY (technology_id) REFERENCES technologies(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE technologies_to_theaters
(
    technology_id INT NOT NULL,
    theater_id INT NOT NULL,
    PRIMARY KEY (technology_id, theater_id),
    FOREIGN KEY (technology_id) REFERENCES technologies(id),
    FOREIGN KEY (theater_id) REFERENCES theaters(id)
);

INSERT INTO roles (name) VALUES ('User'), ('Manager'), ('Admin');

INSERT INTO technologies (name) VALUES ('3D'), ('Dolby Atmos');

-- update table tickets for cascade deleting
ALTER TABLE `biotrio`.`tickets`
DROP FOREIGN KEY `tickets_ibfk_1`;
ALTER TABLE `biotrio`.`tickets`
ADD CONSTRAINT `tickets_ibfk_1`
  FOREIGN KEY (`screening_id`)
  REFERENCES `biotrio`.`screenings` (`id`)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

-- update table bookings for cascade deleting
ALTER TABLE `biotrio`.`bookings`
DROP FOREIGN KEY `bookings_ibfk_1`;
ALTER TABLE `biotrio`.`bookings`
ADD CONSTRAINT `bookings_ibfk_1`
  FOREIGN KEY (`screening_id`)
  REFERENCES `biotrio`.`screenings` (`id`)
  ON DELETE CASCADE;
