package com.afkl.cases.df.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatisticReport {
    private final long requestCount;
    private final long request200Count;
    private final long request4XXCount;
    private final long request5XXCount;
    private final long totalResponseTimeMillis;
    private final long avgResponseTimeMillis;
    private final long minResponseTimeMillis;
    private final long maxResponseTimeMillis;
}
