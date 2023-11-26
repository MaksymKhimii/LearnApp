package com.epam.gymApp.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainersListDto {

  private String traineeUsername;
  private List<String> trainerUsernameList;
}
