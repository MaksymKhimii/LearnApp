package com.epam.gymApp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainerDto {

  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String specialization;
  @JsonProperty("isActive")
  private boolean isActive;
}
