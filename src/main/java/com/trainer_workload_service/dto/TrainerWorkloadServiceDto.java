package com.trainer_workload_service.dto;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


@Data
public class TrainerWorkloadServiceDto {
  private String trainerUsername;
  private String trainerFirstName;
  private String trainerLastName;
  private boolean isActive;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate trainingDate;
  private int trainingDuration; // Suppose duration is in minutes
  private String actionType;
}
