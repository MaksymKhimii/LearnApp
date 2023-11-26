package com.epam.gymApp.repository;

import com.epam.gymApp.model.Trainee;
import com.epam.gymApp.model.Training;
import com.epam.gymApp.model.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeRepository implements GeneralRepository<Trainee> {

  private final SessionFactory sessionFactory;

  @Autowired
  public TraineeRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Trainee findById(long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.get(Trainee.class, id);
    }
  }

  @Override
  public void save(Trainee trainee) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.save(trainee);
      session.getTransaction().commit();
    }
  }

  @Override
  public void update(Trainee trainee) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.update(trainee);
      session.getTransaction().commit();
    }
  }

  @Override
  public void delete(Trainee trainee) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      session.delete(trainee);
      transaction.commit();
    }
  }

  @Override
  public List<Trainee> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM Trainee", Trainee.class).list();
    }
  }

  public User findUserByUsername(String username) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM User WHERE username = :username", User.class)
          .setParameter("username", username)
          .uniqueResult();
    }
  }

  public Trainee findTraineeByUsername(String username) {
    try (Session session = sessionFactory.openSession()) {
      User user = findUserByUsername(username);
      if (user != null) {
        return session.createQuery("FROM Trainee WHERE user = :user", Trainee.class)
            .setParameter("user", user)
            .uniqueResult();
      }
      return null;
    }
  }

  public void updatePassword(User user) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.update(user);
      session.getTransaction().commit();
    }
  }

  public List<Training> findTrainingsByTraineeAndSpecialization(Trainee trainee,
      String specialization) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery(
              "FROM Training WHERE trainee = :trainee AND trainingType = :specialization",
              Training.class)
          .setParameter("trainee", trainee)
          .setParameter("specialization", specialization)
          .list();
    }
  }

  public Trainee findByUser(User user) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM Trainee WHERE user = :user", Trainee.class)
          .setParameter("user", user)
          .uniqueResult();
    }
  }
}

