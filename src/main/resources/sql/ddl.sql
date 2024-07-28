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

CREATE TABLE chat
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE users_chat
(
    user_id BIGINT REFERENCES users(id),
    chat_id BIGINT REFERENCES chat(id),
    PRIMARY KEY (user_id, chat_id)
);

CREATE TABLE profile
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL REFERENCES users(id),
    street VARCHAR(128),
    language CHAR(2)
);