package ru.karachev.sqlforschool.creator;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.entity.Student;
import ru.karachev.sqlforschool.service.Data;
import ru.karachev.sqlforschool.service.FileReader;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public final class DataCreator {

    private static final Logger LOGGER = Logger.getLogger(DataCreator.class);
    private static final FileReader FILE_READER = new FileReader();
    private static final StudentCreator STUDENTS_CREATOR = new StudentCreator();
    private static final GroupCreator GROUP_CREATOR = new GroupCreator();
    private static final CourseCreator COURSES_CREATOR = new CourseCreator();
    private final String studentsNameFilePath;
    private final String studentsLastNameFilePath;
    private final String coursesFilePath;

    public DataCreator(String studentsNameFilePath, String studentsLastNameFilePath, String coursesFilePath) {
        this.studentsNameFilePath = studentsNameFilePath;
        this.studentsLastNameFilePath = studentsLastNameFilePath;
        this.coursesFilePath = coursesFilePath;
    }

    public Data createData() {
        System.out.println("Generating data...");
        Random random = new SecureRandom();
        List<String> studentsNames = FILE_READER.readFile(studentsNameFilePath);
        List<String> studentsLastNames = FILE_READER.readFile(studentsLastNameFilePath);
        List<String> courseNamesAndDescription = FILE_READER.readFile(coursesFilePath);
        List<Group> groups = GROUP_CREATOR.createGroups(random);
        List<Course> courses = COURSES_CREATOR.createCourses(courseNamesAndDescription);
        List<Student> students = STUDENTS_CREATOR.createStudents(studentsNames, studentsLastNames,
                groups, random);
        LOGGER.info("Data creation complete");
        return new Data(groups, courses, students);
    }

}
