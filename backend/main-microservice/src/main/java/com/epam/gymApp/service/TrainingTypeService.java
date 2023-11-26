package com.epam.gymApp.service;

import com.epam.gymApp.model.TrainingType;
import com.epam.gymApp.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingTypeService {

  private final TrainingTypeRepository trainingTypeRepository;

  @Autowired
  public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
    this.trainingTypeRepository = trainingTypeRepository;
  }

  public TrainingType findTrainingTypeByName(String name) {
    return trainingTypeRepository.findByName(name);
  }

  public void save(TrainingType trainingType) {
    if (trainingType == null) {
      throw new NullPointerException();
    }
    if (findTrainingTypeByName(trainingType.getName()) == null) {
      trainingTypeRepository.save(trainingType);
    }
  }

  public TrainingType createTrainingType(String trainingName) {
    if (trainingName == null) {
      return null;
    }
    TrainingType trainingType = TrainingType.builder()
        .name(trainingName)
        .build();
    trainingTypeRepository.save(trainingType);
    return trainingType;
  }
}
