package ru.karachev.sqlforschool.creator;

import ru.karachev.sqlforschool.dao.StudentDao;
import ru.karachev.sqlforschool.entity.Student;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FlowCreator {
    
    private static final int MAX_COURSES_PER_STUDENT = 3;
    
    private final Random random;
    private final StudentDao studentDao;
    
    public FlowCreator(Random random, StudentDao studentDao) {
        this.random = random;
        this.studentDao = studentDao;
    }
    
    public void createFlow(List<Student> students, int maxCoursesCount) {
        students.forEach(student -> createProgram(maxCoursesCount)
                .forEach(courseId -> studentDao.assignToCourse(student.getId(), courseId)));
    }
    
    public Set<Integer> createProgram(int countOfCourses) {
        Set<Integer> courseIds = new HashSet<>();
        
        for (int i = 0; i < MAX_COURSES_PER_STUDENT; i++) {
            courseIds.add(random.nextInt(countOfCourses) + 1);
        }
        
        return courseIds;
    }
}
