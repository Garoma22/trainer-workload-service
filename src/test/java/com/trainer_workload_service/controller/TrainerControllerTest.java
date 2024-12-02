package com.trainer_workload_service.controller;

import com.trainer_workload_service.dto.TrainerMonthWorkloadDto;
import com.trainer_workload_service.dto.TrainerWorkloadServiceDto;
import com.trainer_workload_service.service.TrainerInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Slf4j
public class TrainerControllerTest {

  private TrainerController trainerController;

  @Mock
  private TrainerInfoService trainerInfoService;

  @Mock
  private HttpServletRequest request;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    trainerController = new TrainerController(trainerInfoService);
  }

  @Test
  public void testProcessTrainerTraining_logsTransactionId() {
    // Arrange
    TrainerWorkloadServiceDto dto = new TrainerWorkloadServiceDto();
    String transactionId = "12345-transaction-id";
    when(request.getHeader("Transaction-ID")).thenReturn(transactionId);

    // Act
    ResponseEntity<Void> response = trainerController.processTrainerTraining(dto, request);

    // Assert
    verify(trainerInfoService, times(1)).processTrainingData(dto);

    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testProcessTrainerTraining_missingTransactionId() {
    // Arrange
    TrainerWorkloadServiceDto dto = new TrainerWorkloadServiceDto();
    when(request.getHeader("Transaction-ID")).thenReturn(null);
    // Act
    ResponseEntity<Void> response = trainerController.processTrainerTraining(dto, request);
    // Assert
    verify(trainerInfoService).processTrainingData(dto);
    log.info("Transaction ID is missing");
    assertEquals(200, response.getStatusCodeValue());
  }

  //second controller method

  @Test
  public void testGetTrainerLoadingOfMonth_success() {
    // Arrange
    String trainerUsername = "john_doe";
    int year = 2024;
    int month = 11;
    double workloadHours = 3;
    TrainerMonthWorkloadDto expectedResponse = new TrainerMonthWorkloadDto();
    expectedResponse.setTrainerUsername(trainerUsername);
    expectedResponse.setYearNum(year);
    expectedResponse.setMonthNum(month);
    expectedResponse.setWorkloadHours(workloadHours);

    when(trainerInfoService.getTrainerMonthData(trainerUsername, year, month)).thenReturn(
        expectedResponse);

    // Act
    ResponseEntity<TrainerMonthWorkloadDto> response = trainerController.getTrainerLoadingOfMonth(
        trainerUsername, year, month);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponse, response.getBody());
  }
  @Test
  public void testGetTrainerLoadingOfMonth_yearAndMonthNotFound() {

    String trainerUsername = "john_doe";
    int year = 2024;
    int month = 11;
    double workloadHours = 3;

    TrainerMonthWorkloadDto expectedResponse = new TrainerMonthWorkloadDto();
    expectedResponse.setTrainerUsername(trainerUsername);
    expectedResponse.setYearNum(year);
    expectedResponse.setMonthNum(month);
    expectedResponse.setWorkloadHours(workloadHours);

    TrainerMonthWorkloadDto emptyResponse = new TrainerMonthWorkloadDto();
    emptyResponse.setTrainerUsername(trainerUsername);
    emptyResponse.setYearNum(year);
    emptyResponse.setMonthNum(month);
    emptyResponse.setWorkloadHours(0); //here 0 because no trainings in this month

    when(trainerInfoService.getTrainerMonthData(trainerUsername, year, month)).thenReturn(
        emptyResponse);

    // Act
    ResponseEntity<TrainerMonthWorkloadDto> response = trainerController.getTrainerLoadingOfMonth(trainerUsername, year, month);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(emptyResponse, response.getBody());
  }
}




