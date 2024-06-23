CREATE TABLE users
(
    username VARCHAR(128) PRIMARY KEY,
    firstname VARCHAR(128),
    lastname VARCHAR(128),
    birthdate DATE,
    age INT,
    role VARCHAR(32)
);