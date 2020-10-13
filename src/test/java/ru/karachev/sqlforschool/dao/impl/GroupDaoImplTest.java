package ru.karachev.sqlforschool.dao.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.karachev.sqlforschool.service.DataBaseGenerator;
import ru.karachev.sqlforschool.dao.GroupDao;
import ru.karachev.sqlforschool.dao.impl.GroupDaoImpl;
import ru.karachev.sqlforschool.entity.Group;
import ru.karachev.sqlforschool.service.DBConnector;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GroupDaoImplTest {

    private final DBConnector connector = new DBConnector("src/test/resources/database.properties");
    private final DataBaseGenerator dataBaseGenerator = new DataBaseGenerator(connector);
    private final GroupDao groupDao = new GroupDaoImpl(connector);

    @BeforeEach
    void createDataBase() {
        dataBaseGenerator.generateDataBase("tables.sql");
    }

    @Test
    void saveShouldSaveGroupIntoDataBase() {
        Group expected = Group.builder()
                .withGroupName("third group")
                .withGroupId(3)
                .build();
        groupDao.save(expected);
        Group actualGroup = groupDao.findById(3).orElse(null);

        assertThat(actualGroup).isEqualTo(expected);
    }

    @Test
    void saveAllShouldSaveCoursesWhenGettingListOfCourses() {
        List<Group> groupsForSave = new ArrayList<>();
        Group group1 = Group.builder()
                .withGroupId(3)
                .withGroupName("third group")
                .build();
        Group group2 = Group.builder()
                .withGroupId(4)
                .withGroupName("gourth group")
                .build();

        groupsForSave.add(group1);
        groupsForSave.add(group2);
        groupDao.saveAll(groupsForSave);
        List<Group> actual = groupDao.findAll();
        assertThat(actual)
                .contains(group1)
                .contains(group2);
    }

    @Test
    void findShouldReturnGroupWithDesiredId() {
        Group expected = Group.builder()
                .withGroupName("first group")
                .withGroupId(1)
                .build();
        Group actual = groupDao.findById(1).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllShouldReturnListOfGroupsWhenGettingPaginationParameters() {
        List<Group> expected = new ArrayList<>();
        expected.add(groupDao.findById(1).orElse(null));
        List<Group> actual = groupDao.findAll(1, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllShouldReturnListOfAllGroupsWhenNotGettingPaginationParameters() {
        List<Group> expected = new ArrayList<>();
        expected.add(groupDao.findById(1).orElse(null));
        List<Group> actual = groupDao.findAll(1, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateShouldUpdateGroupInDataBaseWhenGettingGroupWithSameId() {
        Group expected = Group.builder()
                .withGroupName("updated group")
                .withGroupId(1)
                .build();
        groupDao.update(expected);
        Group actual = groupDao.findById(1).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteByIdShouldRemoveGroupFromDataBaseWhenGettingItsId() {
        groupDao.deleteById(2);
        Group actual = groupDao.findById(2).orElse(null);
        assertThat(actual).isNull();
    }

    @Test
    void findAllByStudentCountShouldReturnListOfGroupsWithSpecificQuantityOfStudents() {
        List<Group> expected = new ArrayList<>();
        expected.add(groupDao.findById(2).orElse(null));
        List<Group> actual = groupDao.findAllByStudentCount(3);
        assertThat(expected).isEqualTo(actual);
    }

}

