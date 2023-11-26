DROP TABLE IF EXISTS gym_user;
create table gym_user
(
    id         bigint       not null primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    username   varchar(255) not null,
    password   varchar(255) not null,
    is_active  boolean      not null
);

DROP TABLE IF EXISTS training_type;
create table training_type
(
    id            bigint not null primary key,
    training_type varchar(255)
);

DROP TABLE IF EXISTS trainee;
create table trainee
(
    id            bigint not null primary key,
    user_id       bigint not null references gym_user (id) on delete cascade,
    date_of_birth date,
    address       varchar(255)
);



DROP TABLE IF EXISTS trainer;
create table trainer
(
    id             bigint not null primary key,
    specialization bigint references training_type (id) on delete cascade,
    user_id        bigint not null references gym_user (id) on delete cascade
);


DROP TABLE IF EXISTS training;
create table training
(
    id               bigint           not null primary key,
    trainee_id       bigint           not null references trainee (id) on delete cascade,
    trainer_id       bigint           not null references trainer (id) on delete cascade,
    training_name    varchar(255)     not null,
    training_type_id bigint references training_type (id) on delete cascade,
    training_date    date             not null,
    duration         double precision not null
);

CREATE TABLE IF NOT EXISTS trainee_trainer_relation
(
    id         bigint PRIMARY KEY,
    trainee_id bigint REFERENCES trainee (id) ON DELETE CASCADE,
    trainer_id bigint REFERENCES trainer (id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS token;
create table token
(
    id        bigint       not null primary key,
    token     varchar(255) not null,
    token_type varchar(255) not null,
    revoked   boolean      not null,
    expired   boolean      not null,
    user_id   bigint       not null references gym_user (id) on delete cascade
);

CREATE SEQUENCE _user_seq START 1;
CREATE SEQUENCE trainee_seq START 1;
CREATE SEQUENCE trainer_seq START 1;
CREATE SEQUENCE training_type_seq START 1;
CREATE SEQUENCE training_seq START 1;
CREATE SEQUENCE token_seq START 1;

-- Populate the gym_user table
INSERT INTO gym_user (id, first_name, last_name, username, password, is_active)
VALUES (1, 'John', 'Doe', 'johndoe', 'password123', true),
       (2, 'Alice', 'Smith', 'alice.smith', 'securepass', true),
       (3, 'Michael', 'Johnson', 'michaelj', 'pass456', true),
       (4, 'Emily', 'Williams', 'emilyw', '1234pass', true),
       (5, 'David', 'Brown', 'davidb', 'davypass', true),
       (6, 'Olivia', 'Miller', 'oliviam', 'mypass321', true),
       (7, 'James', 'Taylor', 'jamest', 'taylorpass', true),
       (8, 'Sophia', 'Anderson', 'sophiaa', 'anderson123', true),
       (9, 'Daniel', 'Martin', 'danielm', 'martinpass', true),
       (10, 'Emma', 'Johnson', 'emmaj', 'johnsonpass', true);

-- Populate the training_type table
INSERT INTO training_type (id, training_type)
VALUES (1, 'Cardio'),
       (2, 'Strength'),
       (3, 'Yoga'),
       (4, 'Pilates'),
       (5, 'CrossFit');

-- Populate the trainee table
INSERT INTO trainee (id, user_id, date_of_birth, address)
VALUES (1, 1, '1995-08-15', '123 Main St'),
       (2, 2, '1990-03-22', '456 Elm St'),
       (3, 3, '1988-11-10', '789 Oak St');

-- Populate the trainer table
INSERT INTO trainer (id, specialization, user_id)
VALUES (1, 1, 4),
       (2, 2, 5),
       (3, 3, 6);

-- Populate the training table
INSERT INTO training (id, trainee_id, trainer_id, training_name, training_type_id, training_date,
                      duration)
VALUES (1, 1, 1, 'Cardio Blast', 1, '2023-08-15', 60),
       (2, 2, 2, 'Strength Training', 2, '2023-08-16', 75),
       (3, 3, 3, 'Yoga Flow', 3, '2023-08-17', 90);

-- Populate trainee_trainer_relation table
INSERT INTO trainee_trainer_relation (id, trainee_id, trainer_id)
VALUES (1, 1, 1),
       (2, 2, 2),
       (3, 3, 3);
