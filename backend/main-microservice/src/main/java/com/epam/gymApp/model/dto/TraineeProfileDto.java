package com.epam.gymApp.model.dto;

import com.epam.gymApp.model.Trainer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TraineeProfileDto {

  private String firstName;
  private String lastName;
  @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
  private LocalDate dateOfBirth;
  private String address;
  private boolean isActive;
  private List<Trainer> trainersList;
}
