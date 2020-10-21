package ru.karachev.sqlforschool.creator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.karachev.sqlforschool.dao.CourseDao;
import ru.karachev.sqlforschool.dao.StudentDao;
import ru.karachev.sqlforschool.dao.impl.CourseDaoImpl;
import ru.karachev.sqlforschool.dao.impl.StudentDaoImpl;
import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.entity.Student;
import ru.karachev.sqlforschool.service.DBConnector;
import ru.karachev.sqlforschool.service.DataBaseGenerator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FlowCreatorTest {
    
    private final Random random = new SecureRandom();
    private final DBConnector connector = new DBConnector("src/test/resources/database.properties");
    private final DataBaseGenerator dataBaseGenerator = new DataBaseGenerator(connector);
    private final StudentDao studentDao = new StudentDaoImpl(connector);
    private final CourseDao courseDao = new CourseDaoImpl(connector);
    private FlowCreator flowCreator = new FlowCreator(random, studentDao);
    
    @BeforeEach
    void createDataBase() {
        dataBaseGenerator.generateDataBase("tables.sql");
    }
    
    @Test
    void createProgramShouldReturnSetOfCourseIdsWhenGettingCountOfCourses() {
        
        List<Student> students = new ArrayList<>();
        Student student1 = Student.builder()
                .withStudentId(5)
                .withGroupId(1)
                .withName("student1")
                .withLastName("id5")
                .build();
        Student student2 = Student.builder()
                .withStudentId(6)
                .withGroupId(1)
                .withName("student2")
                .withLastName("id6")
                .build();
        students.add(student1);
        students.add(student2);
        
        studentDao.saveAll(students);
        
        flowCreator.createFlow(students, 2);
        List<Course> coursesForStudent1 = courseDao.findAllByStudentId(5);
        List<Course> coursesForStudent2 = courseDao.findAllByStudentId(6);
        
        assertThat(coursesForStudent1).isNotEmpty();
        assertThat(coursesForStudent2).isNotEmpty();
    }
}
