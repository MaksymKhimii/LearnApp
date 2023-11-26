package com.epam.gymApp.repository;

import com.epam.gymApp.model.Training;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingRepository implements GeneralRepository<Training> {

  private final SessionFactory sessionFactory;

  @Autowired
  public TrainingRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Training findById(long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.get(Training.class, id);
    }
  }

  @Override
  public void save(Training training) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.save(training);
      session.getTransaction().commit();
    }
  }

  @Override
  public void update(Training training) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.update(training);
      session.getTransaction().commit();
    }
  }

  @Override
  public void delete(Training training) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.delete(training);
      session.getTransaction().commit();
    }
  }

  @Override
  public List<Training> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM Training", Training.class).list();
    }
  }

  public Training findByTrainingName(String trainingName) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM Training WHERE name = :name", Training.class)
          .setParameter("name", trainingName)
          .uniqueResult();
    }
  }
}
