package com.trainer_workload_service.dto;

import com.trainer_workload_service.model.Year;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TrainerInfoResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private String status;
    private List<Year> years= new ArrayList<>();
}

