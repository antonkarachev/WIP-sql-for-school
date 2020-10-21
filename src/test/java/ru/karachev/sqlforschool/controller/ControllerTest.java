package ru.karachev.sqlforschool.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.karachev.sqlforschool.creator.FlowCreator;
import ru.karachev.sqlforschool.dao.CourseDao;
import ru.karachev.sqlforschool.dao.GroupDao;
import ru.karachev.sqlforschool.dao.StudentDao;
import ru.karachev.sqlforschool.datasource.DataSource;
import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.service.DataBaseGenerator;
import ru.karachev.sqlforschool.service.FileReader;
import ru.karachev.sqlforschool.view.ViewProvider;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerTest {
    
    @Mock
    private DataBaseGenerator dataBaseGenerator;
    
    @Mock
    private FileReader fileReader;
    
    @Mock
    private DataSource dataSource;
    
    @Mock
    private FlowCreator flowCreator;
    
    @Mock
    private GroupDao groupDao;
    
    @Mock
    private CourseDao courseDao;
    
    @Mock
    private StudentDao studentDao;
    
    @Mock
    private ViewProvider viewProvider;
    
    @InjectMocks
    private Controller controller;
    
    @Test
    void loadShouldInsertAllFromDataSourceToDataBaseAndAssignStudentsToCourses() {
        
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
        
        when(dataSource.getCourses()).thenReturn(courses);
        
        controller.load();
        
        verify(dataBaseGenerator).generateDataBase(anyString());
        verify(groupDao).saveAll(anyList());
        verify(courseDao).saveAll(anyList());
        verify(studentDao).saveAll(anyList());
        verify(flowCreator).createFlow(anyList(), anyInt());
        verify(viewProvider).print(anyString());
    }
    
    @Test
    void runShouldInvokeFindAllByStudentCountOfGroupDaoAndPrintOfViewProvider5TimesWhenGetting1AsCommand() {
        when(viewProvider.readInt())
                .thenReturn(1)
                .thenReturn(anyInt())
                .thenReturn(0);
        controller.run();
        verify(viewProvider, times(5)).print(anyString());
        verify(groupDao).findAllByStudentCount(anyInt());
    }
    
    @Test
    void runShouldInvokeFindAllOfCourseDaoAndFindAllByCourseNameOfStudentDaoAndPrintOfViewProvider6TimesWhenGetting2AsCommand() {
        when(viewProvider.readInt())
                .thenReturn(2)
                .thenReturn(0);
        when(viewProvider.readString()).thenReturn(anyString());
        controller.run();
        verify(viewProvider, times(6)).print(anyString());
        verify(courseDao).findAll();
        verify(studentDao).findAllByCourseName(anyString());
    }
    
    @Test
    void runShouldInvokeSaveOfStudentDaoAndPrintOfViewProvider6TimesWhenGetting3AsCommand() {
        when(viewProvider.readInt())
                .thenReturn(3)
                .thenReturn(201)
                .thenReturn(8)
                .thenReturn(0);
        when(viewProvider.readString())
                .thenReturn("name")
                .thenReturn("last name");
        controller.run();
        verify(viewProvider, times(8)).print(anyString());
        verify(studentDao).save(any());
    }
    
    @Test
    void runShouldInvokeDeleteByIdOfStudentDaoWhenGetting4AsCommand() {
        when(viewProvider.readInt())
                .thenReturn(4)
                .thenReturn(1)
                .thenReturn(0);
        controller.run();
        verify(viewProvider, times(5)).print(anyString());
        verify(studentDao).deleteById(anyInt());
    }
    
    @Test
    void runShouldInvokeAssignToCourseOfStudentDaoWhenGetting5AsCommand() {
        when(viewProvider.readInt())
                .thenReturn(5)
                .thenReturn(1)
                .thenReturn(2)
                .thenReturn(0);
        controller.run();
        verify(viewProvider, times(6)).print(anyString());
        verify(studentDao).assignToCourse(anyInt(), anyInt());
    }
    
    @Test
    void runShouldInvokeRemoveStudentFromCourseOfCourseDaoWhenGetting6AsCommand() {
        when(viewProvider.readInt())
                .thenReturn(6)
                .thenReturn(1)
                .thenReturn(2)
                .thenReturn(0);
        controller.run();
        verify(viewProvider, times(6)).print(anyString());
        verify(courseDao).removeStudentFromCourse(anyInt(), anyInt());
    }
    
    @Test
    void runShouldInvokePrintWhenGettingWrongCommand() {
        when(viewProvider.readInt())
                .thenReturn(9)
                .thenReturn(1)
                .thenReturn(2)
                .thenReturn(0);
        controller.run();
        verify(viewProvider).printError();
    }
    
}
