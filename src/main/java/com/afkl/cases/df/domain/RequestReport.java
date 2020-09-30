package com.afkl.cases.df.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestReport {
    private final long executionTime;
    private final int httpCode;
}
