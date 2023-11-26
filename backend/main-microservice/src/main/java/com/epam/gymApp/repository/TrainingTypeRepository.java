package com.epam.gymApp.repository;

import com.epam.gymApp.model.TrainingType;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeRepository implements GeneralRepository<TrainingType> {

  private final SessionFactory sessionFactory;

  @Autowired
  public TrainingTypeRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public TrainingType findById(long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.get(TrainingType.class, id);
    }
  }

  @Override
  public void save(TrainingType trainingType) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.save(trainingType);
      session.getTransaction().commit();
    }
  }

  @Override
  public void update(TrainingType trainingType) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.update(trainingType);
      session.getTransaction().commit();
    }
  }

  @Override
  public void delete(TrainingType trainingType) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.delete(trainingType);
      session.getTransaction().commit();
    }
  }

  @Override
  public List<TrainingType> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM TrainingType", TrainingType.class).list();
    }
  }

  public TrainingType findByName(String name) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM TrainingType WHERE name = :training_type",
              TrainingType.class)
          .setParameter("training_type", name)
          .uniqueResult();
    }
  }
}
