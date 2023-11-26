package com.epam.gymApp.repository;

import java.util.List;

public interface GeneralRepository<T> {

  T findById(long id);

  void save(T entity);

  void update(T entity);

  void delete(T entity);

  List<T> findAll();
}
