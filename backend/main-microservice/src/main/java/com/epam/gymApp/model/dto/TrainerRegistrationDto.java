package com.epam.gymApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerRegistrationDto {

  private String firstName;
  private String lastName;
  private String specialization;
}
