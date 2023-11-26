package com.epam.gymApp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainer")
public class Trainer {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trainer_gen")
  @SequenceGenerator(name = "trainer_gen", sequenceName = "trainer_seq", allocationSize = 1)
  private Long id;

  @NotNull(message = "User is required")
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @NotNull(message = "Specialization is required")
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "specialization", referencedColumnName = "id")
  private TrainingType specialization;

  @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Training> trainings;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(
      name = "trainee_trainer_relation",
      joinColumns = @JoinColumn(name = "trainer_id"),
      inverseJoinColumns = @JoinColumn(name = "trainee_id")
  )
  private List<Trainee> trainees;
}
