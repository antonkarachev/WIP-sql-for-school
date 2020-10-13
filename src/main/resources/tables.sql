DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students_to_courses CASCADE;

CREATE TABLE groups
(
    group_id   INT UNIQUE NOT NULL ,
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