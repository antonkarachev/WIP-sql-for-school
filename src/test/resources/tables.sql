DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students_to_courses CASCADE;

CREATE TABLE groups
(
    group_id   INT UNIQUE NOT NULL,
    group_name VARCHAR    NOT NULL,
    PRIMARY KEY (group_id)
);

CREATE TABLE students
(
    student_id        INT UNIQUE NOT NULL,
    group_id          INT,
    student_name      VARCHAR    NOT NULL,
    student_last_name VARCHAR    NOT NULL,
    PRIMARY KEY (student_id),
    FOREIGN KEY (group_id) REFERENCES groups ON DELETE SET NULL
);

CREATE TABLE courses
(
    course_id   INT UNIQUE NOT NULL,
    course_name VARCHAR    NOT NULL,
    description VARCHAR    NOT NULL,
    PRIMARY KEY (course_id)
);

CREATE TABLE students_to_courses
(
    student_id INT,
    course_id  INT,
    FOREIGN KEY (student_id) REFERENCES students ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses ON DELETE CASCADE
);

INSERT INTO courses VALUES (1,'biology','biological things');
INSERT INTO courses VALUES (2,'history','historical stuff');
INSERT INTO courses VALUES (3,'mathematics','interesting things');

INSERT INTO groups VALUES (1,'first group');
INSERT INTO groups VALUES (2,'second group');

INSERT INTO students VALUES (1, 2,'Anton','Karachev' );
INSERT INTO students VALUES (2, 2,'Yuliya','Krasava' );
INSERT INTO students VALUES (3, 2,'Alexander','Bublikov');
INSERT INTO students VALUES (4, 1,'Sofia','Morkovkina');

INSERT INTO students_to_courses VALUES (1,1);
INSERT INTO students_to_courses VALUES (1,2);
INSERT INTO students_to_courses VALUES (2,2);
INSERT INTO students_to_courses VALUES (2,3);
INSERT INTO students_to_courses VALUES (3,1);
INSERT INTO students_to_courses VALUES (3,2);
INSERT INTO students_to_courses VALUES (3,3);
INSERT INTO students_to_courses VALUES (4,1);
INSERT INTO students_to_courses VALUES (4,2);
INSERT INTO students_to_courses VALUES (4,3);