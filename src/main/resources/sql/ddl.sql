CREATE TABLE company
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(128) UNIQUE,
    firstname  VARCHAR(128),
    lastname   VARCHAR(128),
    birthdate  DATE,
    role       VARCHAR(32),
    info       JSONB,
    company_id INT references company (id) ON DELETE CASCADE NOT NULL
);