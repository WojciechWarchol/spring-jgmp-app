DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;

CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    date DATETIME
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL
);

CREATE TABLE tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eventId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    category ENUM('STANDARD', 'PREMIUM', 'BAR'),
    place INT NOT NULL
--    CONSTRAINT fk_event
--    FOREIGN KEY (eventId)
--        REFERENCES events(id),
--    CONSTRAINT fk_user
--    FOREIGN KEY (userId)
--        REFERENCES users(id)
);
