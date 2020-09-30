package ru.karachev.sqlforschool.context;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.creator.TableCreator;
import ru.karachev.sqlforschool.dao.impl.CourseDaoImpl;
import ru.karachev.sqlforschool.dao.impl.GroupDaoImpl;
import ru.karachev.sqlforschool.dao.impl.StudentDaoImpl;
import ru.karachev.sqlforschool.service.DBConnector;
import ru.karachev.sqlforschool.service.Data;

public class ContextInjector {

    private static final Logger LOGGER = Logger.getLogger(ContextInjector.class);
    private final Data data;
    private final String tablesFilePath;
    private final TableCreator tableCreator;
    private final CourseDaoImpl courseDao;
    private final GroupDaoImpl groupDao;
    private final StudentDaoImpl studentDao;

    public ContextInjector(DBConnector connector, Data data, String tablesFilePath) {
        this.data = data;
        this.tablesFilePath = tablesFilePath;
        this.tableCreator = new TableCreator(connector);
        this.courseDao = new CourseDaoImpl(connector);
        this.groupDao = new GroupDaoImpl(connector);
        this.studentDao = new StudentDaoImpl(connector);
    }

    public void injectContext() {
        System.out.println("Create tables...");
        tableCreator.createTables(tablesFilePath);
        System.out.println("Insert data into tables...");
        data.getGroups().forEach(groupDao::save);
        data.getCourses().forEach(courseDao::save);
        studentDao.saveFromList(data.getStudents());
        studentDao.assignStudentsFromListToCourses(data.getStudents());
        LOGGER.info("Injection complete");
        System.out.println("Injection complete\n");
    }

}
