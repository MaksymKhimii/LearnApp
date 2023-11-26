package com.epam.gymApp.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.Duration;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {

  private String traineeUsername;
  private String trainerUsername;
  private String trainingName;
  @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
  private LocalDate date;
  @JsonFormat(shape = Shape.NUMBER_FLOAT)
  private Duration duration;
  private String trainingType;
}
