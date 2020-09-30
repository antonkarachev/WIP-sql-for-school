package ru.karachev.sqlforschool.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao <E, ID>{

    void save (E entity);

    Optional<E> findById (ID ID);

    void update (E entity);

    List<E> findAll(ID page, ID itemPerPage);

    void deleteById (ID ID);

}
