package com.epam.gymApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotAssignedTrainersDto {

  private String username;
  private String firstName;
  private String lastName;
  private String specialization;
}
