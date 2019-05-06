INSERT INTO roles(name) VALUE ('ADMIN');

INSERT INTO users(username, password, role, employee_id) VALUES
('admin', '1234', 1, NULL);

INSERT INTO theaters (name, no_of_rows, seats_per_row) VALUES
('Theater 1', 14, 20),('Theater 2', 8, 12),('Theater 3', 8, 6);

INSERT INTO movies (title, runtime, synopsis, genre) VALUES
('Glass', 129, 'Glass - synopsis', 'Drama, Sci-Fi, Thriller'),
('The Kid Who Would Be King', 120, 'TKWWBK - synopsis', 'Action, Adventure, Family'),
('What Men Want', 117, 'What Men Want - synopsis', 'Comedy, Fantasy, Romance');

INSERT INTO screenings (movie_id, theater_id, start_time)
VALUES (1, 1, '2019-06-20 18:30'), (2, 2, '2019-06-20 19:00'),(3, 3, '2019-06-20 18:45');