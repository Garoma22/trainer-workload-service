package com.trainer_workload_service.trainer_workload_service.service;

import com.trainer_workload_service.trainer_workload_service.dto.TrainerWorkloadServiceDto;
import com.trainer_workload_service.trainer_workload_service.model.Month;
import com.trainer_workload_service.trainer_workload_service.model.TrainerInfo;
import com.trainer_workload_service.trainer_workload_service.model.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrainerInfoService {
  private final List<TrainerInfo> trainers = Collections.synchronizedList(new ArrayList<>());

  public TrainerInfo getTrainer(String username) {
    return trainers.stream()
        .filter(t -> t.getUsername().equals(username))
        .findFirst()
        .orElse(null);
  }

  public synchronized void processTrainingData(TrainerWorkloadServiceDto dto) {
    TrainerInfo trainer = getTrainer(dto.getTrainerUsername());

    if (trainer == null) {
      trainer = new TrainerInfo();
      trainer.setUsername(dto.getTrainerUsername());
      trainer.setFirstName(dto.getTrainerFirstName());
      trainer.setLastName(dto.getTrainerLastName());
      trainer.setStatus(dto.isActive() ? "Active" : "Inactive");
      if (isValidTrainer(trainer)) {
        trainers.add(trainer);
      } else {
        throw new IllegalArgumentException("Invalid trainer data");
      }
    }

    TrainerInfo finalTrainer = trainer;
    Year trainingYear = trainer.getYears().stream()
        .filter(y -> y.getYear() == dto.getTrainingDate().getYear())
        .findFirst()
        .orElseGet(() -> {
          Year newYear = new Year(dto.getTrainingDate().getYear());
          finalTrainer.getYears().add(newYear);
          return newYear;
        });

    Month trainingMonth = trainingYear.getMonths().stream()
        .filter(m -> m.getMonth() == dto.getTrainingDate().getMonthValue())
        .findFirst()
        .orElseGet(() -> {
          Month newMonth = new Month(dto.getTrainingDate().getMonthValue(), 0);
          trainingYear.getMonths().add(newMonth);
          return newMonth;
        });



    trainingMonth.setTrainingSummaryDuration(trainingMonth.getTrainingSummaryDuration() + dto.getTrainingDuration());
    log.info(trainers.toString());
  }

  private boolean isValidTrainer(TrainerInfo trainer) {
    return trainer.getUsername() != null && !trainer.getUsername().trim().isEmpty();
  }
}