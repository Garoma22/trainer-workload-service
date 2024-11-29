package com.trainer_workload_service.trainer_workload_service.controller;


import com.trainer_workload_service.trainer_workload_service.dto.TrainerWorkloadServiceDto;
import com.trainer_workload_service.trainer_workload_service.service.TrainerInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
  @RestController
  public class TrainerController {

        @Autowired
    private TrainerInfoService trainerService;


    @PostMapping("/trainings")
    public ResponseEntity<Void> processTrainerTraining(@RequestBody TrainerWorkloadServiceDto dto, HttpServletRequest request) {
      trainerService.processTrainingData(dto);
      String transactionId = request.getHeader("Transaction-ID");
      log.info(transactionId);
      return ResponseEntity.ok().build();
    }

  }






