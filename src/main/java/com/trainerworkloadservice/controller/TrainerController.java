package com.trainerworkloadservice.controller;

import com.trainerworkloadservice.dto.TrainerInfoResponseDto;
import com.trainerworkloadservice.TrainerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class TrainerController {

  private final TrainerInfoService trainerInfoService;
  @Autowired
  public TrainerController(TrainerInfoService trainerInfoService) {
    this.trainerInfoService = trainerInfoService;
  }



  //we are not using this control
//  @PostMapping("/trainers/workload")
//  public ResponseEntity<Void> processTrainerTraining(@RequestBody TrainerWorkloadServiceDto dto,
//      HttpServletRequest request) {
//    trainerInfoService.processTrainingData(dto);
//    String transactionId = request.getHeader("Transaction-ID");
//    log.info(transactionId);
//    return ResponseEntity.ok().build();
//  }

  @GetMapping("/trainers/{trainerUsername}/workload")
  public ResponseEntity<TrainerInfoResponseDto> getTrainerLoadingOfMonth(
      @PathVariable String trainerUsername) {
    TrainerInfoResponseDto response = trainerInfoService.getTrainerMonthData(trainerUsername);
    return ResponseEntity.ok(response);
  }

}






