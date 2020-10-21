package ru.karachev.sqlforschool.creator;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.datasource.DataSource;
import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.entity.Student;
import ru.karachev.sqlforschool.service.FileReader;

import java.util.List;

public class DataSourceCreator {
    
    private static final Logger LOGGER = Logger.getLogger(DataSourceCreator.class);
    
    private static final String STUDENTS_NAME_FILE_PATH = "src/main/resources/names.txt";
    private static final String STUDENTS_LAST_NAME_FILE_PATH = "src/main/resources/lastnames.txt";
    private static final String COURSES_FILE_PATH = "src/main/resources/courses.txt";
    
    private static final int MAX_STUDENTS = 200;
    private static final int MAX_GROUPS = 10;
    private static final int MAX_ID_FOR_GROUP = 99;
    private static final int MAX_COURSES = 10;
    
    private final FileReader fileReader;
    private final CourseCreator courseCreator;
    private final GroupCreator groupCreator;
    private final StudentCreator studentCreator;
    
    public DataSourceCreator(FileReader fileReader, CourseCreator courseCreator, GroupCreator groupCreator,
                             StudentCreator studentCreator) {
        this.fileReader = fileReader;
        this.courseCreator = courseCreator;
        this.groupCreator = groupCreator;
        this.studentCreator = studentCreator;
    }
    
    public DataSource createDataSource() {
        List<String> studentsNames = fileReader.readFile(STUDENTS_NAME_FILE_PATH);
        List<String> studentsLastNames = fileReader.readFile(STUDENTS_LAST_NAME_FILE_PATH);
        List<String> courseNamesAndDescription = fileReader.readFile(COURSES_FILE_PATH);
        List<Group> groups = groupCreator.createGroups(MAX_ID_FOR_GROUP, MAX_GROUPS);
        List<Course> courses = courseCreator.createCourses(MAX_COURSES, courseNamesAndDescription);
        List<Student> students = studentCreator.createStudents(studentsNames, studentsLastNames,
                MAX_STUDENTS, MAX_GROUPS);
        LOGGER.info("Data creation complete");
        return new DataSource(students, groups, courses);
    }
    
}
