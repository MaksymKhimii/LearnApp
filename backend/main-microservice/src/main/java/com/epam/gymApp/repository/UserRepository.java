package com.epam.gymApp.repository;

import com.epam.gymApp.model.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository implements GeneralRepository<User> {

  private final SessionFactory sessionFactory;

  @Autowired
  public UserRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public User findById(long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.get(User.class, id);
    }
  }

  @Override
  public void save(User user) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.save(user);
      session.getTransaction().commit();
    }
  }

  @Transactional
  public User saveNewUser(User user) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.save(user);
      session.getTransaction().commit();
    }
    return findByUsername(user.getUsername());
  }

  @Override
  public void update(User user) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.update(user);
      session.getTransaction().commit();
    }
  }

  @Override
  public void delete(User user) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.delete(user);
      session.getTransaction().commit();
    }
  }

  @Override
  public List<User> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM User", User.class).list();
    }
  }

  public User findByUsername(String username) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM User WHERE username = :username", User.class)
          .setParameter("username", username)
          .uniqueResult();
    }
  }
}
