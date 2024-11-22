package com.trainer_workload_service.trainer_workload_service.model;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TrainerInfo {
  private String username;
  private String firstName;
  private String lastName;
  private String status;
  private List<Year> years= new ArrayList<>();
}
