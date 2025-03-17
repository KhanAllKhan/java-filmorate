INSERT INTO users (email, login, name, birthday) VALUES
('john.doe@example.com', 'johndoe', 'John Doe', '1990-01-01'),
('jane.doe@example.com', 'janedoe', 'Jane Doe', '1992-02-02');

INSERT INTO mpa (id, name) VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');

INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES
('Inception', 'Mind-bending thriller', '2010-07-16', 148, 3), -- PG-13
('The Matrix', 'A hacker discovers reality', '1999-03-31', 136, 4); -- R
