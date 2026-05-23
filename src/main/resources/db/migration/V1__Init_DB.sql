CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

CREATE TABLE groups (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE disciplines (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE teachers (
                          id BIGSERIAL PRIMARY KEY,
                          full_name VARCHAR(255) NOT NULL UNIQUE,
                          user_id BIGINT UNIQUE,
                          CONSTRAINT fk_teacher_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          full_name VARCHAR(255) NOT NULL UNIQUE,
                          group_id BIGINT NOT NULL,
                          user_id BIGINT UNIQUE,
                          CONSTRAINT fk_student_group FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE RESTRICT,
                          CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE classes (
                         id BIGSERIAL PRIMARY KEY,
                         discipline_id BIGINT NOT NULL,
                         group_id BIGINT NOT NULL,
                         teacher_id BIGINT NOT NULL,
                         class_date DATE NOT NULL,
                         class_number INT NOT NULL,
                         CONSTRAINT fk_class_discipline FOREIGN KEY (discipline_id) REFERENCES disciplines(id) ON DELETE RESTRICT,
                         CONSTRAINT fk_class_group FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE RESTRICT,
                         CONSTRAINT fk_class_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE RESTRICT
);

CREATE TABLE attendances (
                             id BIGSERIAL PRIMARY KEY,
                             class_id BIGINT NOT NULL,
                             student_id BIGINT NOT NULL,
                             is_present BOOLEAN NOT NULL,
                             CONSTRAINT fk_attendance_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE RESTRICT,
                             CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE RESTRICT,
                             CONSTRAINT uq_class_student UNIQUE (class_id, student_id)
);