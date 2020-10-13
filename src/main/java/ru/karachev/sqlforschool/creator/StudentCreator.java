package ru.karachev.sqlforschool.creator;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentCreator {

    private static final Logger LOGGER = Logger.getLogger(StudentCreator.class);

    private final Random random;

    public StudentCreator(Random random) {
        this.random = random;
    }

    public List<Student> createStudents(List<String> names, List<String> lastNames,
                                        int maxStudents, int maxGroups) {
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < maxStudents; i++) {
            students.add(Student.builder()
                    .withStudentId(i + 1)
                    .withGroupId(random.nextInt(maxGroups) + 1)
                    .withName(names.get(random.nextInt(names.size())))
                    .withLastName(lastNames.get(random.nextInt(lastNames.size())))
                    .build());
        }

        LOGGER.info("Creation of students complete");
        return students;
    }

}
