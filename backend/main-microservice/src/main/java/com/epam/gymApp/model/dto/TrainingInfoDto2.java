package com.epam.gymApp.model.dto;

import java.time.Duration;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingInfoDto2 {
  private String trainingName;
  private LocalDate trainingDate;
  private String trainingType;
  private Duration trainingDuration;
  private String traineeName;
}
