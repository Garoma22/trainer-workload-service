package com.trainer_workload_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearDto {
  private int year;
  private List<Map<String, Integer>> months;
}

