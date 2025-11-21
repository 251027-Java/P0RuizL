package com.fantasy.Stats;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MedianLuck {
    private Map<Long, List<Double>> medianLuckScoresByWeek;

    private Map<Long, Double> totalMedianLuckScores;
    
}
