package com.afkl.cases.df.controller;

import com.afkl.cases.df.domain.StatisticReport;
import com.afkl.cases.df.statistic.StatisticsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class StatisticsController {

    @Autowired
    private StatisticsBean statisticBean;

    @RequestMapping(method = GET, value = "/statistics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Callable<StatisticReport> getStatistics() {
        return () -> statisticBean.getStatisticReport();
    }
}
