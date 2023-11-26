-- gym_user table
CREATE TABLE IF NOT EXISTS gym_user (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL
    );

-- training_type table
CREATE TABLE IF NOT EXISTS training_type (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             training_type VARCHAR(255)
    );

-- trainee table
CREATE TABLE IF NOT EXISTS trainee (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       user_id BIGINT NOT NULL,
                                       date_of_birth DATE,
                                       address VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES gym_user(id)
    );


-- trainer table
CREATE TABLE IF NOT EXISTS trainer (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       specialization BIGINT,
                                       user_id BIGINT NOT NULL,
                                       FOREIGN KEY (specialization) REFERENCES training_type(id),
    FOREIGN KEY (user_id) REFERENCES gym_user(id)
    );


-- training table
CREATE TABLE IF NOT EXISTS training (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        trainee_id BIGINT NOT NULL,
                                        trainer_id BIGINT NOT NULL,
                                        training_name VARCHAR(255) NOT NULL,
    training_type_id BIGINT,
    training_date DATE NOT NULL,
    duration DOUBLE NOT NULL,
    FOREIGN KEY (trainee_id) REFERENCES trainee(id),
    FOREIGN KEY (trainer_id) REFERENCES trainer(id),
    FOREIGN KEY (training_type_id) REFERENCES training_type(id)
    );

-- token table
CREATE TABLE IF NOT EXISTS token (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     token VARCHAR(255) NOT NULL,
    token_type VARCHAR(255) NOT NULL,
    revoked BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES gym_user(id)
    );

CREATE SEQUENCE _user_seq START WITH 1;
CREATE SEQUENCE trainee_seq START WITH 1;
CREATE SEQUENCE trainer_seq START WITH 1;
CREATE SEQUENCE training_type_seq START WITH 1;
CREATE SEQUENCE training_seq START WITH 1;
CREATE SEQUENCE token_seq START WITH 1;

-- Insert test data for gym_user
INSERT INTO gym_user (id, first_name, last_name, username, password, is_active)
VALUES (1, 'Maks', 'Khimii', 'Maks_Khimii422', '$2a$10$WUFlEgVjxpeM5xhGeKkouOFraEuKS3UUrHViWBIB6FaMiiX9bmaZi', TRUE);

INSERT INTO gym_user (id, first_name, last_name, username, password, is_active)
VALUES (2, 'Dmytro', 'Yurikof', 'Dmytro_Yurikof607', '$2a$10$WUFlEgVjxpeM5xhGeKkouOFraEuKS3UUrHViWBIB6FaMiiX9bmaZi', TRUE);


INSERT INTO trainee (id, user_id, date_of_birth, address)
VALUES (1, 1, '1995-08-15', 'lalal, 171');

INSERT INTO training_type (id, training_type)
VALUES (1, 'Cardio'),
       (2, 'Strength'),
       (3, 'Yoga'),
       (4, 'Pilates'),
       (5, 'crossfit');

INSERT INTO trainer (id, specialization, user_id)
VALUES (2, 5, 2);