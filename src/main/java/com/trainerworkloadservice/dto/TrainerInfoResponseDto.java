package com.trainerworkloadservice.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TrainerInfoResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private String status;
    private List<YearDto> years= new ArrayList<>();
}

