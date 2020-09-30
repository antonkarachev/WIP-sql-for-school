package ru.karachev.sqlforschool;

import ru.karachev.sqlforschool.context.ContextInjector;
import ru.karachev.sqlforschool.controller.Controller;
import ru.karachev.sqlforschool.creator.DataCreator;
import ru.karachev.sqlforschool.dao.CourseDao;
import ru.karachev.sqlforschool.dao.GroupDao;
import ru.karachev.sqlforschool.dao.StudentDao;
import ru.karachev.sqlforschool.dao.impl.CourseDaoImpl;
import ru.karachev.sqlforschool.dao.impl.GroupDaoImpl;
import ru.karachev.sqlforschool.dao.impl.StudentDaoImpl;
import ru.karachev.sqlforschool.service.DBConnector;
import ru.karachev.sqlforschool.service.Data;
import ru.karachev.sqlforschool.view.ViewProvider;

public class ApplicationRunner {

    public void runApplication() {

        String studentsNameFilePath = "src/main/resources/names.txt";
        String studentsLastNameFilePath = "src/main/resources/lastnames.txt";
        String coursesFilePath = "src/main/resources/courses.txt";
        String tablesFilePath = "tables.sql";
        String configFilePath = "src/main/resources/database.properties";

        DBConnector connector = new DBConnector(configFilePath);

        DataCreator dataCreator = new DataCreator(studentsNameFilePath,
                studentsLastNameFilePath, coursesFilePath);
        Data data = dataCreator.createData();

        ContextInjector contextInjector = new ContextInjector(connector,data, tablesFilePath);
        contextInjector.injectContext();

        GroupDao groupDao = new GroupDaoImpl(connector);
        CourseDao courseDao = new CourseDaoImpl(connector);
        StudentDao studentDao = new StudentDaoImpl(connector);
        Controller controller = new Controller(groupDao, courseDao, studentDao);
        ViewProvider viewProvider = new ViewProvider(controller);
        viewProvider.provideView();
    }

}
