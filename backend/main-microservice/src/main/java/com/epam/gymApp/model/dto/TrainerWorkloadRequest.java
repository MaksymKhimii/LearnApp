package com.epam.gymApp.model.dto;

import java.time.Duration;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkloadRequest {

  private String trainerUsername;
  private String trainerFirstName;
  private String trainerLastName;
  private boolean isActive;
  private LocalDate trainingDate;
  private Duration trainingDuration;
  private String actionType;
}

