package com.trainer_workload_service.dto;


import lombok.Data;

@Data

public class TrainerMonthWorkloadDto {

  private String trainerUsername;
  private int yearNum;
  private int monthNum;
  private double workloadHours;

}
