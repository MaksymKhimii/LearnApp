package com.epam.gymApp.model.dto;

import com.epam.gymApp.model.Trainee;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileDto {

  private String firstName;
  private String lastName;
  private String specialization;
  private boolean isActive;
  private List<Trainee> traineesList;
}
