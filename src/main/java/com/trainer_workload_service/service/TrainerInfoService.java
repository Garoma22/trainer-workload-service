package com.trainer_workload_service.service;

import com.trainer_workload_service.dto.TrainerMonthWorkloadDto;
import com.trainer_workload_service.dto.TrainerWorkloadServiceDto;
import com.trainer_workload_service.model.Month;
import com.trainer_workload_service.model.TrainerInfo;
import com.trainer_workload_service.model.Year;
import com.trainer_workload_service.utils.TrainerStatus;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrainerInfoService {
  private final List<TrainerInfo> trainers = Collections.synchronizedList(new ArrayList<>());

  @PostConstruct
  public void initTestData() {
    TrainerInfo testTrainer = new TrainerInfo();
    testTrainer.setUsername("test_trainer");
    testTrainer.setFirstName("John");
    testTrainer.setLastName("Doe");
    testTrainer.setStatus(TrainerStatus.ACTIVE);

    Year year2024 = new Year(2024);
    Month november = new Month(11, 10); //for example 10 hours of trainings in november
    year2024.getMonths().add(november);
    testTrainer.getYears().add(year2024);

    trainers.add(testTrainer);
    log.info("Test data initialized: {}", testTrainer);
  }

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

      trainer.setStatus(dto.isActive() ? TrainerStatus.ACTIVE : TrainerStatus.INACTIVE);

      if (isValidTrainer(trainer)) {
        trainers.add(trainer);
      } else {
        throw new IllegalArgumentException("Invalid trainer data (empty username)");
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

    trainingMonth.setTrainingSummaryDuration(
        trainingMonth.getTrainingSummaryDuration() + dto.getTrainingDuration());
    log.info(trainers.toString());
  }

  public boolean isValidTrainer(TrainerInfo trainer) {
    return trainer.getUsername() != null && !trainer.getUsername().trim().isEmpty();
  }

  public TrainerMonthWorkloadDto getTrainerMonthData(String trainerUsername, int year, int month) {
    TrainerInfo trainer = getTrainer(trainerUsername);

    if (trainer == null) {
      throw new IllegalArgumentException("Trainer not found: " + trainerUsername);
    }

    // Проверяем валидность тренера
    if (!isValidTrainer(trainer)) {
      throw new IllegalArgumentException("Invalid trainer data (empty username)");
    }


      Year targetYear = trainer.getYears().stream()
          .filter(y -> y.getYear() == year)
          .findFirst()
          .orElseGet(() -> {
            Year newYear = new Year(year);
            trainer.getYears().add(newYear);
            return newYear;
          });

      Month targetMonth = targetYear.getMonths().stream()
          .filter(m -> m.getMonth() == month)
          .findFirst()
          .orElseGet(() -> {
            Month newMonth = new Month(month, 0);
            targetYear.getMonths().add(newMonth);
            return newMonth;
          });

      TrainerMonthWorkloadDto trainerMonthWorkloadDto = new TrainerMonthWorkloadDto();
      trainerMonthWorkloadDto.setTrainerUsername(trainerUsername);
      trainerMonthWorkloadDto.setYearNum(year);
      trainerMonthWorkloadDto.setMonthNum(month);
      trainerMonthWorkloadDto.setWorkloadHours(targetMonth.getTrainingSummaryDuration());

      log.info(String.valueOf(trainerMonthWorkloadDto));
      return trainerMonthWorkloadDto;
  }

  public List<TrainerInfo> getTrainers() {
    return trainers;
  }




}