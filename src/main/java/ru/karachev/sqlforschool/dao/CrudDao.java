package ru.karachev.sqlforschool.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<E, ID> {

    void save(E entity);

    void saveAll(List<E> entities);

    Optional<E> findById(ID id);

    void update(E entity);

    List<E> findAll(int page, int itemPerPage);

    List<E> findAll();

    void deleteById(ID id);

}
