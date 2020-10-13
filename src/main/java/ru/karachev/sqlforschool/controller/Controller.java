package ru.karachev.sqlforschool.controller;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.dao.CourseDao;
import ru.karachev.sqlforschool.dao.GroupDao;
import ru.karachev.sqlforschool.dao.StudentDao;
import ru.karachev.sqlforschool.datasource.DataSource;
import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.entity.Student;
import ru.karachev.sqlforschool.service.DataBaseGenerator;
import ru.karachev.sqlforschool.service.FileReader;
import ru.karachev.sqlforschool.view.ViewProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Controller {

    private static final Logger LOGGER = Logger.getLogger(Controller.class);

    private static final int FIND_GROUPS_WITH_STUDENTS_COUNT_COMMAND = 1;
    private static final int FIND_STUDENTS_TO_COURSE_NAME_COMMAND = 2;
    private static final int ADD_NEW_STUDENT_COMMAND = 3;
    private static final int DELETE_STUDENT_BY_ID_COMMAND = 4;
    private static final int ADD_STUDENT_TO_COURSE = 5;
    private static final int REMOVE_STUDENT_FROM_COURSE_COMMAND = 6;
    private static final int EXIT_COMMAND = 0;

    private static final String NEW_LINE = "\n";
    private static final int MAX_COURSES_PER_STUDENT = 3;

    private static final String TABLES_FILE_PATH = "tables.sql";
    private static final String MENU_FILE_PATH = "src/main/resources/menu.txt";

    private final DataBaseGenerator dataBaseGenerator;
    private final DataSource dataSource;
    private final FileReader fileReader;
    private final Random random;
    private final GroupDao groupDao;
    private final CourseDao courseDao;
    private final StudentDao studentDao;
    private final ViewProvider viewProvider;

    public Controller(DataBaseGenerator dataBaseGenerator, DataSource dataSource, FileReader fileReader,
                      Random random, GroupDao groupDao, CourseDao courseDao, StudentDao studentDao,
                      ViewProvider viewProvider) {
        this.dataBaseGenerator = dataBaseGenerator;
        this.dataSource = dataSource;
        this.fileReader = fileReader;
        this.random = random;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.studentDao = studentDao;
        this.viewProvider = viewProvider;
    }

    public void load(){
        LOGGER.info("Loading start");
        dataBaseGenerator.generateDataBase(TABLES_FILE_PATH);

        groupDao.saveAll(dataSource.getGroups());
        courseDao.saveAll(dataSource.getCourses());
        studentDao.saveAll(dataSource.getStudents());
        assignStudentToCourses(dataSource.getStudents(), dataSource.getCourses().size());
        LOGGER.info("Insertion complete");

        String menu = String.join(NEW_LINE, fileReader.readFile(MENU_FILE_PATH));
        viewProvider.print(menu);

        LOGGER.info("Loading complete");
    }

    public void run() {
        boolean keepGoing = true;
        int command;
        while (keepGoing) {
            viewProvider.print("Enter command: ");
            command = viewProvider.readInt();

            switch (command) {
                case FIND_GROUPS_WITH_STUDENTS_COUNT_COMMAND:
                    findGroupsWithStudentCount();
                    break;
                case FIND_STUDENTS_TO_COURSE_NAME_COMMAND:
                    findAllStudentsToCourseName();
                    break;
                case ADD_NEW_STUDENT_COMMAND:
                    addNewStudent();
                    break;
                case ADD_STUDENT_TO_COURSE:
                    addStudentToCourse();
                    break;
                case DELETE_STUDENT_BY_ID_COMMAND:
                    deleteStudentById();
                    break;
                case REMOVE_STUDENT_FROM_COURSE_COMMAND:
                    removeStudentFromCourse();
                    break;
                case EXIT_COMMAND:
                    exit();
                    keepGoing = false;
                    break;
                default:
                    viewProvider.printError();
            }
        }

    }

    private void assignStudentToCourses(List<Student> students, int maxCourses) {
        Set<Integer> courseIds = new HashSet<>();
        for(Student student: students){
            for (int i = 0; i < MAX_COURSES_PER_STUDENT; i++) {
                courseIds.add(random.nextInt(maxCourses) + 1);
            }
            courseIds.forEach(id -> studentDao.assignToCourse(student.getId(), id));
        }
        LOGGER.info("Assigning students to courses complete");
    }

    private void findGroupsWithStudentCount() {
        viewProvider.print("Enter count: ");
        int count = viewProvider.readInt();
        viewProvider.print(groupDao.findAllByStudentCount(count).stream()
                .map(Group::toString)
                .collect(Collectors.joining(NEW_LINE)));
    }

    private void findAllStudentsToCourseName() {
        viewProvider.print(courseDao.findAll().stream()
                .map(Course::getName)
                .collect(Collectors.joining(NEW_LINE)));
        viewProvider.print("Enter course name:");
        String courseName = viewProvider.readString();
        viewProvider.print(studentDao.findAllByCourseName(courseName).stream()
                .map(Student::toString)
                .collect(Collectors.joining(NEW_LINE)));
    }

    private void addNewStudent() {
        viewProvider.print("Enter student id: ");
        int studentId = viewProvider.readInt();
        viewProvider.print("Enter group id: ");
        int groupId = viewProvider.readInt();
        viewProvider.print("Enter student name: ");
        String studentName = viewProvider.readString();
        viewProvider.print("Enter student last name: ");
        String studentLastName = viewProvider.readString();
        Student student = Student.builder()
                .withStudentId(studentId)
                .withGroupId(groupId)
                .withName(studentName)
                .withLastName(studentLastName)
                .build();
        studentDao.save(student);
        viewProvider.print("Complete");
    }

    private void deleteStudentById() {
        viewProvider.print("Enter student id: ");
        int studentId = viewProvider.readInt();
        studentDao.deleteById(studentId);
        viewProvider.print("Complete");
    }

    private void addStudentToCourse() {
        viewProvider.print("Enter student id: ");
        int studentId = viewProvider.readInt();
        viewProvider.print("Enter course id: ");
        int courseId = viewProvider.readInt();
        studentDao.assignToCourse(studentId, courseId);
        viewProvider.print("Complete");
    }

    private void removeStudentFromCourse() {
        viewProvider.print("Enter student id: ");
        int studentId = viewProvider.readInt();
        viewProvider.print("Enter course id: ");
        int courseId = viewProvider.readInt();
        courseDao.removeStudentFromCourse(studentId, courseId);
        viewProvider.print("Complete");
    }

    private void exit() {
        viewProvider.print("Bye see you next time");
    }

}
