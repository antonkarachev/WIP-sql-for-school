package ru.karachev.sqlforschool.creator;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.entity.Student;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class StudentCreator {

    private static final Logger LOGGER = Logger.getLogger(StudentCreator.class);
    private static final int MAX_STUDENTS = 200;
    private static final int MAX_QUANTITY_OF_COURSES_FOR_ONE_STUDENT = 3;
    private static final int QUANTITY_OF_COURSES = GroupCreator.getMaxGroups();

    public List<Student> createStudents(List<String> names, List<String> lastNames,
                                        List<Group> groups, Random random) {
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < MAX_STUDENTS; i++) {
            students.add(Student.builder()
                    .withStudentId(i + 1)
                    .withGroupId(random.nextInt(groups.size()) + 1)
                    .withName(names.get(random.nextInt(names.size())))
                    .withLastName(lastNames.get(random.nextInt(lastNames.size())))
                    .withCourseIds(createStudentCourses())
                    .build());
        }

        LOGGER.info("Creation of students complete");
        return students;
    }

    private Set<Integer> createStudentCourses() {
        Set<Integer> courseIds = new HashSet<>();
        Random random = new SecureRandom();
        for (int i = 0; i < MAX_QUANTITY_OF_COURSES_FOR_ONE_STUDENT; i++) {
            courseIds.add(random.nextInt(QUANTITY_OF_COURSES )+1);
        }
        return courseIds;
    }

}
