package com.epam.gymApp.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingDto {

  private String username;
  private String password;
  @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
  private LocalDate periodFrom;
  @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
  private LocalDate periodTo;
  private String traineeName;
  private String trainingType;
}
