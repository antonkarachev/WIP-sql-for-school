package ru.karachev.sqlforschool.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.karachev.sqlforschool.creator.CourseCreator;
import ru.karachev.sqlforschool.creator.DataSourceCreator;
import ru.karachev.sqlforschool.creator.GroupCreator;
import ru.karachev.sqlforschool.creator.StudentCreator;
import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.entity.Student;
import ru.karachev.sqlforschool.service.FileReader;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSourceCreatorTest {

    @InjectMocks
    private DataSourceCreator dataSourceCreator;

    @Mock
    private FileReader mockedFileReader;

    @Mock
    private StudentCreator mockedStudentCreator;

    @Mock
    private GroupCreator mockedGroupCreator;

    @Mock
    private CourseCreator mockedCourseCreator;

    @Test
    void createDataShouldReturnDataObjectWhenGettingThreeFilePaths() {

        List<String> studentsNames = new ArrayList<>();
        studentsNames.add("Anton");
        studentsNames.add("Yuliya");
        List<String> studentsLastNames = new ArrayList<>();
        studentsLastNames.add("Karachev");
        List<String> courseNamesAndDescription = new ArrayList<>();
        courseNamesAndDescription.add("first course_first description");
        courseNamesAndDescription.add("second course_second description");

        List<Student> students = new ArrayList<>();
        students.add(Student.builder()
                .withStudentId(1)
                .withGroupId(1)
                .withName("Anton")
                .withLastName("Karachev")
                .build());
        students.add(Student.builder()
                .withStudentId(2)
                .withGroupId(1)
                .withName("Yuliya")
                .withLastName("Karachev")
                .build());

        List<Group> groups = new ArrayList<>();
        groups.add(Group.builder()
                .withGroupId(1)
                .withGroupName("first")
                .build());
        groups.add(Group.builder()
                .withGroupId(2)
                .withGroupName("second")
                .build());

        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder()
                .withCourseId(1)
                .withCourseName("first course")
                .withDescription("first description")
                .build());
        courses.add(Course.builder()
                .withCourseId(2)
                .withCourseName("second course")
                .withDescription("second description")
                .build());
        DataSource expected = new DataSource(students, groups, courses);

       when(mockedFileReader.readFile(anyString()))
                .thenReturn(studentsNames)
                .thenReturn(studentsLastNames)
                .thenReturn(courseNamesAndDescription);

        when(mockedCourseCreator.createCourses(anyInt(), anyList())).thenReturn(courses);
        when(mockedGroupCreator.createGroups(anyInt(), anyInt())).thenReturn(groups);
        when(mockedStudentCreator.createStudents(anyList(), anyList(), anyInt(),
                anyInt())).thenReturn(students);

        DataSource actual = dataSourceCreator.createDataSource();
        assertThat(expected).isEqualTo(actual);
    }

}
