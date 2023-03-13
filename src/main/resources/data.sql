INSERT INTO events (id, title, date) VALUES
    (1,'Music Event','2023-01-01'),
    (2,'IT Event','2023-04-13'),
    (3,'Culinary Event','2023-04-13');

INSERT INTO users (id, name, email) VALUES
    (1,'Jozef Malolepszy','jozef.malolepszy@gmail.com'),
    (2,'Grzegorz Brzeczyszczykiewicz','grzegorz.b@gmail.com'),
    (3,'Stefan Batory','victor.wiedenski@gmail.com'),
    (4,'Jan Nowak','j.nowak@gmail.com'),
    (5,'Adam Kowalski','adam.kowalski@gmail.com'),
    (6,'Janusz Nosacz','janusz.n@gmail.com');

INSERT INTO tickets (id, eventId, userId, category, place) VALUES
    (1,1,1,'STANDARD',1),
    (2,2,1,'PREMIUM',1),
    (3,1,2,'STANDARD',2),
    (4,3,2,'BAR',1),
    (5,1,3,'PREMIUM',3),
    (6,3,3,'STANDARD',2),
    (7,1,4,'STANDARD',4),
    (8,2,5,'STANDARD',2),
    (9,3,6,'PREMIUM',3);

