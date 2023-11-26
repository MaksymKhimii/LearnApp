package com.epam.gymApp.repository;

import com.epam.gymApp.model.Trainer;
import com.epam.gymApp.model.Training;
import com.epam.gymApp.model.TrainingType;
import com.epam.gymApp.model.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerRepository implements GeneralRepository<Trainer> {

  private final SessionFactory sessionFactory;

  @Autowired
  public TrainerRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Trainer findById(long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.get(Trainer.class, id);
    }
  }

  @Override
  public void save(Trainer trainer) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.save(trainer);
      session.getTransaction().commit();
    }
  }

  @Override
  public void update(Trainer trainer) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.update(trainer);
      session.getTransaction().commit();
    }
  }

  @Override
  public void delete(Trainer trainer) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.delete(trainer);
      session.getTransaction().commit();
    }
  }

  @Override
  public List<Trainer> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM Trainer", Trainer.class).list();
    }
  }

  public Trainer findByUser(User user) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM Trainer WHERE user = :user", Trainer.class)
          .setParameter("user", user)
          .uniqueResult();
    }
  }

  public void updatePassword(User user) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.update(user);
      session.getTransaction().commit();
    }
  }

  public User findUserByUsername(String username) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM User WHERE username = :username", User.class)
          .setParameter("username", username)
          .uniqueResult();
    }
  }

  public Trainer findTrainerByUsername(String username) {
    try (Session session = sessionFactory.openSession()) {
      User user = findUserByUsername(username);
      if (user != null) {
        return session.createQuery("FROM Trainer WHERE user = :user", Trainer.class)
            .setParameter("user", user)
            .uniqueResult();
      }
      return null;
    }
  }

  public List<Training> findTrainingsByTrainerAndSpecialization(Trainer trainer,
      TrainingType specialization) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery(
              "FROM Training WHERE trainer = :trainer AND trainingType = :specialization",
              Training.class)
          .setParameter("trainer", trainer)
          .setParameter("specialization", specialization)
          .list();
    }
  }

  public List<Trainer> findNotAssignedTrainersForTrainee(String traineeUsername) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery(
              "FROM Trainer t " +
                  "WHERE t NOT IN (" +
                  "SELECT tr FROM Trainer tr JOIN tr.trainees trainee "
                  + "WHERE trainee.user.username = :traineeUsername)",
              Trainer.class)
          .setParameter("traineeUsername", traineeUsername)
          .list();
    }
  }
}

