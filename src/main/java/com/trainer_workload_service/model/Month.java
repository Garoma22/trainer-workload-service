//package com.trainer_workload_service.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//@AllArgsConstructor
//@Data
//public class Month {
//    private int month;
//    private double trainingSummaryDuration;
//
//}


package com.trainer_workload_service.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Month {
    private Map<String, Integer> monthDurations;

}
