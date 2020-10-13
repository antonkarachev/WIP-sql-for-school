package ru.karachev.sqlforschool.context;

import ru.karachev.sqlforschool.controller.Controller;
import ru.karachev.sqlforschool.creator.CourseCreator;
import ru.karachev.sqlforschool.creator.DataSourceCreator;
import ru.karachev.sqlforschool.creator.GroupCreator;
import ru.karachev.sqlforschool.creator.StudentCreator;
import ru.karachev.sqlforschool.dao.CourseDao;
import ru.karachev.sqlforschool.dao.GroupDao;
import ru.karachev.sqlforschool.dao.StudentDao;
import ru.karachev.sqlforschool.dao.impl.CourseDaoImpl;
import ru.karachev.sqlforschool.dao.impl.GroupDaoImpl;
import ru.karachev.sqlforschool.dao.impl.StudentDaoImpl;
import ru.karachev.sqlforschool.datasource.DataSource;
import ru.karachev.sqlforschool.service.DBConnector;
import ru.karachev.sqlforschool.service.DataBaseGenerator;
import ru.karachev.sqlforschool.service.FileReader;
import ru.karachev.sqlforschool.view.ViewProvider;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class ContextInjector {

    private static final String CONNECTION_PROPS_FILEPATH = "src/main/resources/database.properties";

    private static final Random RANDOM = new SecureRandom();
    private static final FileReader FILE_READER = new FileReader();

    private static final CourseCreator COURSE_CREATOR = new CourseCreator();
    private static final GroupCreator GROUP_CREATOR = new GroupCreator(RANDOM);
    private static final StudentCreator STUDENT_CREATOR = new StudentCreator(RANDOM);
    private static final DataSourceCreator DATA_SOURCE_CREATOR = new DataSourceCreator(FILE_READER, COURSE_CREATOR,
            GROUP_CREATOR, STUDENT_CREATOR);
    private static final DataSource DATA_SOURCE = DATA_SOURCE_CREATOR.createDataSource();

    private static final DBConnector CONNECTOR = new DBConnector(CONNECTION_PROPS_FILEPATH);
    private static final DataBaseGenerator DATA_BASE_GENERATOR = new DataBaseGenerator(CONNECTOR);
    private static final GroupDao GROUP_DAO = new GroupDaoImpl(CONNECTOR);
    private static final CourseDao COURSE_DAO = new CourseDaoImpl(CONNECTOR);
    private static final StudentDao STUDENT_DAO = new StudentDaoImpl(CONNECTOR);

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ViewProvider VIEW_PROVIDER = new ViewProvider(SCANNER);
    private static final Controller CONTROLLER = new Controller(DATA_BASE_GENERATOR, DATA_SOURCE,
            FILE_READER, RANDOM, GROUP_DAO, COURSE_DAO, STUDENT_DAO, VIEW_PROVIDER);


    public Controller getController() {
        return CONTROLLER;
    }

    public DBConnector getConnector(){
        return CONNECTOR;
    }

}
