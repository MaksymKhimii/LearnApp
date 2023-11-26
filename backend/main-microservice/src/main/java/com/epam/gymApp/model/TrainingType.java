package com.epam.gymApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "training_type")
public class TrainingType {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_type_gen")
  @SequenceGenerator(name = "training_type_gen", sequenceName = "training_type_seq", allocationSize = 1)
  private Long id;

  @NotNull(message = "Training type name is required")
  @Column(name = "training_type")
  private String name;

  @OneToMany
  private List<Trainer> trainer;

  @OneToMany
  private List<Training> trainings;
}
