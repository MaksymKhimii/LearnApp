package com.epam.gymApp.repository;

import com.epam.gymApp.model.Token;
import com.epam.gymApp.model.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

  private final SessionFactory sessionFactory;

  private final UserRepository userRepository;

  public void save(Token token) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.save(token);
      session.getTransaction().commit();
    }
  }

  public void update(Token token) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.update(token);
      session.getTransaction().commit();
    }
  }

  public List<Token> findAllValidTokensByUser(String username) {
    try (Session session = sessionFactory.openSession()) {
      User user = userRepository.findByUsername(username);
      if (user != null) {
        return session.createQuery(
                "FROM Token t WHERE t.user.username = :username AND t.revoked = false AND t.expired = false",
                Token.class)
            .setParameter("username", username)
            .getResultList();
      }
      return null;
    }
  }

  public Optional<Token> findByToken(String token) {
    try (Session session = sessionFactory.openSession()) {
      return Optional.ofNullable(session.unwrap(Session.class).createQuery(
              "FROM Token WHERE token = :token AND revoked = false AND expired = false", Token.class)
          .setParameter("token", token)
          .uniqueResult());
    }
  }


  @Transactional
  public void saveAll(List<Token> tokens) {
    for (Token token : tokens) {
      update(token);
    }
  }
}
