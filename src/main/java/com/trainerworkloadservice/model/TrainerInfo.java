package com.trainerworkloadservice.model;
import com.trainerworkloadservice.utils.TrainerStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TrainerInfo {
  private String username;
  private String firstName;
  private String lastName;
  private TrainerStatus status;
  private List<Year> years= new ArrayList<>();
}
