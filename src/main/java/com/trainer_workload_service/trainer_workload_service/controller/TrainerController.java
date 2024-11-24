package com.trainer_workload_service.trainer_workload_service.controller;


import com.trainer_workload_service.trainer_workload_service.dto.TrainerWorkloadServiceDto;
import com.trainer_workload_service.trainer_workload_service.model.TrainerInfo;
import com.trainer_workload_service.trainer_workload_service.service.TrainerInfoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

  @RestController
  public class TrainerController {

        @Autowired
    private TrainerInfoService trainerService;

    @PostMapping("/trainings")
    public ResponseEntity<List<TrainerInfo>> addTrainingSession(@RequestBody TrainerWorkloadServiceDto dto) {
    trainerService.processTrainingData(dto);
      return ResponseEntity.ok().build();
    }


  }




