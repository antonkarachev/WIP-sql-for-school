package ru.karachev.sqlforschool.dao;

import ru.karachev.sqlforschool.entity.Group;

import java.util.List;

public interface GroupDao extends CrudDao <Group, Integer>{

    List<Group> findGroupsWithSelectedCountOfStudents(Integer counter);

}
