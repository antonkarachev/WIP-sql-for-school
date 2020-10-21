package ru.karachev.sqlforschool.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.karachev.sqlforschool.dao.CourseDao;
import ru.karachev.sqlforschool.dao.StudentDao;
import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.entity.Student;
import ru.karachev.sqlforschool.service.DBConnector;
import ru.karachev.sqlforschool.service.DataBaseGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StudentDaoImplTest {
    
    private final DBConnector connector = new DBConnector("src/test/resources/database.properties");
    private final DataBaseGenerator dataBaseGenerator = new DataBaseGenerator(connector);
    private final StudentDao studentDao = new StudentDaoImpl(connector);
    private final CourseDao courseDao = new CourseDaoImpl(connector);
    
    @BeforeEach
    void createDataBase() {
        dataBaseGenerator.generateDataBase("tables.sql");
    }
    
    @Test
    void saveShouldSaveStudentIntoDataBase() {
        Student expected = Student.builder()
                .withStudentId(5)
                .withGroupId(2)
                .withName("Ton")
                .withLastName("Tons")
                .build();
        studentDao.save(expected);
        Student actual = studentDao.findById(5).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void saveAllShouldSaveStudentsWhenGettingListOfStudents() {
        List<Student> studentsForSave = new ArrayList<>();
        Student student1 = Student.builder()
                .withStudentId(5)
                .withGroupId(2)
                .withName("Ton")
                .withLastName("Tons")
                .build();
        Student student2 = Student.builder()
                .withStudentId(6)
                .withGroupId(1)
                .withName("Son")
                .withLastName("Sons")
                .build();
        Student student3 = Student.builder()
                .withStudentId(7)
                .withGroupId(2)
                .withName("Yon")
                .withLastName("Yons")
                .build();
        studentsForSave.add(student1);
        studentsForSave.add(student2);
        studentsForSave.add(student3);
        studentDao.saveAll(studentsForSave);
        List<Student> actual = studentDao.findAll();
        assertThat(actual)
                .contains(student1)
                .contains(student2)
                .contains(student3);
    }
    
    @Test
    void findByIdShouldReturnStudentWithDesiredId() {
        Student expected = Student.builder()
                .withStudentId(1)
                .withGroupId(2)
                .withName("Anton")
                .withLastName("Karachev")
                .build();
        Student actual = studentDao.findById(1).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void findAllShouldReturnListOfStudentsWhenGettingPaginationParameters() {
        List<Student> expected = new ArrayList<>();
        expected.add(studentDao.findById(3).orElse(null));
        expected.add(studentDao.findById(4).orElse(null));
        List<Student> actual = studentDao.findAll(2, 2);
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void findAllShouldReturnListOfAllStudentsWhenNotGettingPaginationParameters() {
        List<Student> expected = new ArrayList<>();
        expected.add(studentDao.findById(1).orElse(null));
        expected.add(studentDao.findById(2).orElse(null));
        expected.add(studentDao.findById(3).orElse(null));
        expected.add(studentDao.findById(4).orElse(null));
        List<Student> actual = studentDao.findAll();
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void deleteShouldDeleteStudentFromDataBaseWhenGettingStudentID() {
        studentDao.deleteById(1);
        Student verifiable = studentDao.findById(1).orElse(null);
        assertThat(verifiable).isNull();
    }
    
    @Test
    void updateShouldUpdateStudentWhenGettingStudent() {
        Student expected = Student.builder()
                .withStudentId(1)
                .withGroupId(1)
                .withName("updated name")
                .withLastName("updated lastname")
                .build();
        studentDao.update(expected);
        Student actual = studentDao.findById(1).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void findAllRelatedToCourseByNameShouldReturnListOfStudentsRelatedToCourseName() {
        List<Student> expected = new ArrayList<>();
        expected.add(studentDao.findById(1).orElse(null));
        expected.add(studentDao.findById(3).orElse(null));
        expected.add(studentDao.findById(4).orElse(null));
        List<Student> actual = studentDao.findAllByCourseName("biology");
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void assignStudentToCourseShouldAddRecordToTableStudentsToCourses() {
        Course expected = Course.builder()
                .withCourseId(3)
                .withCourseName("mathematics")
                .withDescription("interesting things")
                .build();
        studentDao.assignToCourse(1, 3);
        List<Course> courses= courseDao.findAllByStudentId(1);
        assertThat(courses).contains(expected);
    }
    
}
