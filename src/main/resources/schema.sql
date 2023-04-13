DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_accounts;

CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    date DATETIME,
    ticket_price DECIMAL(5,2) NOT NULL
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL
);

CREATE TABLE tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    category ENUM('STANDARD', 'PREMIUM', 'BAR'),
    place INT NOT NULL
--    CONSTRAINT fk_event
--    FOREIGN KEY (eventId)
--        REFERENCES events(id),
--    CONSTRAINT fk_user
--    FOREIGN KEY (userId)
--        REFERENCES users(id)
);

CREATE TABLE user_accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    funds DECIMAL(6,2) NOT NULL
);