package com.fantasy.Stats;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LuckData {
    private MedianLuck medianLuck;
    private Map<Long, AllPlayData> allPlayLuck;
}
