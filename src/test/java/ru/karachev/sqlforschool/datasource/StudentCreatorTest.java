package ru.karachev.sqlforschool.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.karachev.sqlforschool.creator.StudentCreator;
import ru.karachev.sqlforschool.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentCreatorTest {

    @InjectMocks
    StudentCreator studentCreator;

    @Mock
    Random mockedRandom;

    @Test
    void createStudentsShouldReturnListOfStudentsWhenGettingData() {
        List<String> names= new ArrayList<>();
        names.add("Anton");
        names.add("Julia");
        List<String> lastNames= new ArrayList<>();
        lastNames.add("Karachev");
        lastNames.add("Busikova");

        List<Student> expected = new ArrayList<>();
        expected.add(Student.builder()
                .withStudentId(1)
                .withGroupId(1)
                .withName("Anton")
                .withLastName("Karachev")
                .build());
        expected.add(Student.builder()
                .withStudentId(2)
                .withGroupId(2)
                .withName("Julia")
                .withLastName("Busikova")
                .build());

        when(mockedRandom.nextInt(anyInt()))
                .thenReturn(0)
                .thenReturn(0)
                .thenReturn(0)
                .thenReturn(1)
                .thenReturn(1)
                .thenReturn(1);

        List<Student> actual = studentCreator.createStudents(names, lastNames, 2,2);

        assertThat(expected).isEqualTo(actual);
    }

}
