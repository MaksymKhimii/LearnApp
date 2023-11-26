package com.epam.microservice.model;

import java.io.Serializable;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class TrainerWorkloadDto implements Serializable {

  private String trainerUsername;
  private String trainerFirstName;
  private String trainerLastName;
  private boolean isActive;
  private String trainingDate;
  private double trainingDuration;
  private String actionType;
}
