package com.trainer_workload_service.controller;


import com.trainer_workload_service.dto.TrainerWorkloadServiceDto;
import com.trainer_workload_service.dto.TrainerMonthWorkloadDto;
import com.trainer_workload_service.service.TrainerInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class TrainerController {

  //  @Autowired
  private TrainerInfoService trainerInfoService;
  @Autowired
  public TrainerController(TrainerInfoService trainerInfoService) { // Конструктор
    this.trainerInfoService = trainerInfoService;
  }




//we do not need this for ActiveMQ, we have the Listener method in service for this task

//  @PostMapping("/trainers/workload")
//  public ResponseEntity<Void> processTrainerTraining(@RequestBody TrainerWorkloadServiceDto dto,
//      HttpServletRequest request) {
//    trainerInfoService.processTrainingData(dto);
//    String transactionId = request.getHeader("Transaction-ID");
//    log.info(transactionId);
//    return ResponseEntity.ok().build();
//  }

  @GetMapping("/trainers/{trainerUsername}/workload")
  public ResponseEntity<TrainerMonthWorkloadDto> getTrainerLoadingOfMonth(
      @PathVariable String trainerUsername,
      @RequestParam int year,
      @RequestParam int month) {
    TrainerMonthWorkloadDto response = trainerInfoService.getTrainerMonthData(trainerUsername, year,
        month);
    return ResponseEntity.ok(response);
  }
}






