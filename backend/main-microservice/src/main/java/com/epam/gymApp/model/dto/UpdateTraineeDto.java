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
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeDto {

  private String username;
  private String password;
  private String firstName;
  private String lastName;
  @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
  private LocalDate dateOfBirth;
  private String address;
  private boolean isActive;
}
