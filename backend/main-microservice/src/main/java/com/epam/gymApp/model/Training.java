package com.epam.gymApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDate;
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
@Table(name = "training")
public class Training {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_gen")
  @SequenceGenerator(name = "training_gen", sequenceName = "training_seq", allocationSize = 1)
  private Long id;

  @NotNull(message = "Trainer is required")
  @ManyToOne
  @JoinColumn(name = "trainer_id", referencedColumnName = "id")
  private Trainer trainer;

  @NotNull(message = "Trainee is required")
  @ManyToOne
  @JoinColumn(name = "trainee_id", referencedColumnName = "id")
  private Trainee trainee;

  @NotNull(message = "Training name is required")
  @Column(name = "training_name")
  private String name;

  @NotNull(message = "Training type is required")
  @ManyToOne
  @JoinColumn(name = "training_type_id", referencedColumnName = "id")
  private TrainingType trainingType;

  @NotNull(message = "Date is required")
  @Column(name = "training_date")
  private LocalDate date;

  private Duration duration;
}
