package com.trainer_workload_service.servise;
import com.trainer_workload_service.dto.TrainerMonthWorkloadDto;
import com.trainer_workload_service.dto.TrainerWorkloadServiceDto;
import com.trainer_workload_service.model.Month;
import com.trainer_workload_service.model.TrainerInfo;
import com.trainer_workload_service.model.Year;
import com.trainer_workload_service.service.TrainerInfoService;
import com.trainer_workload_service.utils.TrainerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerInfoServiceTest {

  private TrainerInfoService trainerInfoService;

  @BeforeEach
  void setUp() {
    trainerInfoService = new TrainerInfoService();
    trainerInfoService.initTestData();
  }
  @Test
  public void testInitTestData() {

    // Assert
    assertEquals(1, trainerInfoService.getTrainers().size()); // check we have one trainer

    TrainerInfo testTrainer = trainerInfoService.getTrainers().get(0);
    assertEquals("test_trainer", testTrainer.getUsername());
    assertEquals(TrainerStatus.ACTIVE, testTrainer.getStatus());
    assertEquals(2024, testTrainer.getYears().get(0).getYear());
    assertEquals(11, testTrainer.getYears().get(0).getMonths().get(0).getMonth());
    assertEquals(10, testTrainer.getYears().get(0).getMonths().get(0).getTrainingSummaryDuration());
  }


  @Test
  public void testGetTrainer_existingTrainer() {
    // Arrange
    String username = "test_trainer";
    // Act
    TrainerInfo trainer = trainerInfoService.getTrainer(username);
    // Assert
    assertNotNull(trainer);
    assertEquals("test_trainer", trainer.getUsername());
    assertEquals("John", trainer.getFirstName());
    assertEquals("Doe", trainer.getLastName());
  }

  @Test
  public void testGetTrainer_nonExistingTrainer() {
    String username = "unknown_trainer";
    TrainerInfo trainer = trainerInfoService.getTrainer(username);
    assertNull(trainer);
  }

  @Test
  public void testProcessTrainingData_addTrainingForExistingTrainer() {
    // Arrange
    TrainerWorkloadServiceDto dto = new TrainerWorkloadServiceDto();
    dto.setTrainerUsername("test_trainer");
    dto.setTrainerFirstName("John");
    dto.setTrainerLastName("Doe");
    dto.setActive(true);
    dto.setTrainingDate(LocalDate.of(2024, 11, 20));
    dto.setTrainingDuration(5);

    // Act
    trainerInfoService.processTrainingData(dto);

    // Assert
    TrainerInfo trainer = trainerInfoService.getTrainer("test_trainer");
    assertNotNull(trainer);
    assertEquals(1, trainer.getYears().size());
    assertEquals(15, trainer.getYears().get(0).getMonths().get(0).getTrainingSummaryDuration()); // 10 (init) + 5
  }

  @Test
  public void testProcessTrainingData_createNewTrainer() {
    // Arrange
    TrainerWorkloadServiceDto dto = new TrainerWorkloadServiceDto();
    dto.setTrainerUsername("new_trainer");
    dto.setTrainerFirstName("Jane");
    dto.setTrainerLastName("Smith");
    dto.setActive(true);
    dto.setTrainingDate(LocalDate.of(2024, 12, 1));
    dto.setTrainingDuration(8);  // here we write 8 for example for new trainer

    // Act
    trainerInfoService.processTrainingData(dto);

    // Assert
    TrainerInfo trainer = trainerInfoService.getTrainer("new_trainer");
    assertNotNull(trainer);
    assertEquals("Jane", trainer.getFirstName());
    assertEquals(TrainerStatus.ACTIVE, trainer.getStatus());
    assertEquals(1, trainer.getYears().size());
    assertEquals(8, trainer.getYears().get(0).getMonths().get(0).getTrainingSummaryDuration()); //here we have 8
  }

  @Test
  public void testProcessTrainingData_addNewYearAndMonth() {
    // Arrange
    TrainerWorkloadServiceDto dto = new TrainerWorkloadServiceDto();
    dto.setTrainerUsername("test_trainer");
    dto.setTrainerFirstName("John");
    dto.setTrainerLastName("Doe");
    dto.setActive(true);
    dto.setTrainingDate(LocalDate.of(2025, 1, 15));
    dto.setTrainingDuration(6);

    // Act
    trainerInfoService.processTrainingData(dto);

    // Assert
    TrainerInfo trainer = trainerInfoService.getTrainer("test_trainer");
    assertNotNull(trainer);
    assertEquals(2, trainer.getYears().size()); // add new year
    assertEquals(6, trainer.getYears().get(1).getMonths().get(0).getTrainingSummaryDuration());
  }

  @Test
  public void testProcessTrainingData_invalidTrainerData() {
    // Arrange
    TrainerWorkloadServiceDto dto = new TrainerWorkloadServiceDto();
    dto.setTrainerUsername(""); // WRONG name!
    dto.setTrainerFirstName("Invalid");
    dto.setTrainerLastName("Trainer");
    dto.setActive(true);
    dto.setTrainingDate(LocalDate.of(2024, 11, 15));
    dto.setTrainingDuration(5);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      trainerInfoService.processTrainingData(dto);
    });
    assertEquals("Invalid trainer data (empty username)", exception.getMessage());
  }

  @Test
  public void testProcessTrainingData_threadSafety() throws InterruptedException {
    // Arrange
    Runnable task = () -> {
      TrainerWorkloadServiceDto dto = new TrainerWorkloadServiceDto();
      dto.setTrainerUsername("thread_safe_trainer");
      dto.setTrainerFirstName("Thread");
      dto.setTrainerLastName("Safe");
      dto.setActive(true);
      dto.setTrainingDate(LocalDate.of(2024, 12, 10));
      dto.setTrainingDuration(3);
      trainerInfoService.processTrainingData(dto);
    };

    // Act
    Thread thread1 = new Thread(task);
    Thread thread2 = new Thread(task);
    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();

    // Assert
    TrainerInfo trainer = trainerInfoService.getTrainer("thread_safe_trainer");
    assertNotNull(trainer);
    assertEquals(6, trainer.getYears().get(0).getMonths().get(0).getTrainingSummaryDuration()); // 6 is sum of treads
  }


  @Test
  public void testIsValidTrainer_validUsername() {
    // Arrange
    TrainerInfo trainer = new TrainerInfo();
    trainer.setUsername("valid_username");

    // Act
    boolean result = trainerInfoService.isValidTrainer(trainer);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsValidTrainer_nullUsername() {
    // Arrange
    TrainerInfo trainer = new TrainerInfo();
    trainer.setUsername(null);

    // Act
    boolean result = trainerInfoService.isValidTrainer(trainer);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsValidTrainer_emptyUsername() {
    // Arrange
    TrainerInfo trainer = new TrainerInfo();
    trainer.setUsername("");

    // Act
    boolean result = trainerInfoService.isValidTrainer(trainer);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsValidTrainer_blankUsername() {
    // Arrange
    TrainerInfo trainer = new TrainerInfo();
    trainer.setUsername("   ");

    // Act
    boolean result = trainerInfoService.isValidTrainer(trainer);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsValidTrainer_noUsernameSet() {

    TrainerInfo trainer = new TrainerInfo();
    boolean result = trainerInfoService.isValidTrainer(trainer);

    assertFalse(result);
  }


  @Test
  public void testGetTrainerMonthData_existingTrainerExistingYearAndMonth() {
    // Arrange
    String username = "test_trainer";
    int year = 2024;
    int month = 11;

    // Act
    TrainerMonthWorkloadDto result = trainerInfoService.getTrainerMonthData(username, year, month);

    // Assert
    assertNotNull(result);
    assertEquals("test_trainer", result.getTrainerUsername());
    assertEquals(2024, result.getYearNum());
    assertEquals(11, result.getMonthNum());
    assertEquals(10, result.getWorkloadHours()); // сheck that it is 10 hours acccording to initTestData
  }

  @Test
  public void testGetTrainerMonthData_existingTrainerNewYear() {
    // Arrange
    String username = "test_trainer";
    int year = 2025; //new Year
    int month = 1;

    // Act
    TrainerMonthWorkloadDto result = trainerInfoService.getTrainerMonthData(username, year, month);

    // Assert
    assertNotNull(result);
    assertEquals("test_trainer", result.getTrainerUsername());
    assertEquals(2025, result.getYearNum());
    assertEquals(1, result.getMonthNum());
    assertEquals(0, result.getWorkloadHours()); // new month and year, hours  = 0
  }

  @Test
  public void testGetTrainerMonthData_existingTrainerNewMonth() {
    // Arrange
    String username = "test_trainer";
    int year = 2024;
    int month = 12;

    // Act
    TrainerMonthWorkloadDto result = trainerInfoService.getTrainerMonthData(username, year, month);

    // Assert
    assertNotNull(result);
    assertEquals("test_trainer", result.getTrainerUsername());
    assertEquals(2024, result.getYearNum());
    assertEquals(12, result.getMonthNum());
    assertEquals(0, result.getWorkloadHours());
  }


  @Test
  public void testGetTrainerMonthData_trainerNotFound() {
    // Arrange
    String username = "non_existent_trainer";
    int year = 2024;
    int month = 11;

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      trainerInfoService.getTrainerMonthData(username, year, month);
    });

    assertEquals("Trainer not found: non_existent_trainer", exception.getMessage());
  }

  @Test
  public void testGetTrainerMonthData_correctWorkloadCalculation() {
    // Arrange
    String username = "test_trainer";
    int year = 2024;
    int month = 11;

    // add hours
    TrainerInfo trainer = trainerInfoService.getTrainer(username);
    Year targetYear = trainer.getYears().get(0);
    Month targetMonth = targetYear.getMonths().get(0);
    targetMonth.setTrainingSummaryDuration(targetMonth.getTrainingSummaryDuration() + 5);

    // Act
    TrainerMonthWorkloadDto result = trainerInfoService.getTrainerMonthData(username, year, month);

    // Assert
    assertNotNull(result);
    assertEquals(15, result.getWorkloadHours()); // 15
  }
}








