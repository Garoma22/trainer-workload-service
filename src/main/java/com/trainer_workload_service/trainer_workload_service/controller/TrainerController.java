package com.trainer_workload_service.trainer_workload_service.controller;


import com.trainer_workload_service.trainer_workload_service.dto.TrainerWorkloadServiceDto;
import com.trainer_workload_service.trainer_workload_service.model.TrainerInfo;
import com.trainer_workload_service.trainer_workload_service.service.TrainerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//  @RequestMapping("/api/trainers")
//  public class TrainerController {
//
//    @Autowired
//    private TrainerInfoService trainerService;

//    @GetMapping("/{username}")
//    public TrainerInfo getTrainer(@PathVariable String username) {
//      return trainerService.getTrainer(username);
//    }
//
//    @PostMapping("/{username}/trainings")
//    public void addTrainingSession(@PathVariable String username, @RequestParam int year, @RequestParam int month, @RequestParam double duration) {
//      trainerService.addTrainingSession(username, year, month, duration);
//    }


//  @PostMapping("/add")
//  public void addTrainingSession(@RequestBody TrainerWorkloadServiceDto trainingData) {
//    trainerService.processTrainingData(trainingData);
//  }



  @RestController
  public class TrainerController {

        @Autowired
    private TrainerInfoService trainerService;

    @PostMapping("/trainings")
    public ResponseEntity<?> addTrainingSession(@RequestBody TrainerWorkloadServiceDto dto) {

    trainerService.processTrainingData(dto);
      return ResponseEntity.ok().build();
    }
  }




