package com.epam.gymApp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotBlank;
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
@Table(name = "trainee")
public class Trainee {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trainee_gen")
  @SequenceGenerator(name = "trainee_gen", sequenceName = "trainee_seq", allocationSize = 1)
  private Long id;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "address")
  private String address;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Training> trainings;

  @ManyToMany(mappedBy = "trainees", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<Trainer> trainers;
}
