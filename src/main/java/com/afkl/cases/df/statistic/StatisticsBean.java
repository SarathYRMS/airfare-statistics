package com.afkl.cases.df.statistic;

import com.afkl.cases.df.constant.Constant;
import com.afkl.cases.df.domain.RequestReport;
import com.afkl.cases.df.domain.StatisticReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class StatisticsBean {

    private final AtomicReference<StatisticReport> statisticReport = new AtomicReference<>(
            new StatisticReport(0, 0, 0, 0, 0, 0, Long.MAX_VALUE, Long.MIN_VALUE));
    private final LinkedBlockingQueue<RequestReport> requestReports = new LinkedBlockingQueue<>();
    private final Thread statisticThread = new Thread(() -> {
        try {
            log.info("Statistics Handler was started");
            while (true) {
                RequestReport requestReport = requestReports.take();
                StatisticReport oldReport = statisticReport.get();
                long totalRequests = oldReport.getRequestCount() + 1;
                long totalResponseTime = oldReport.getTotalResponseTimeMillis() + requestReport.getExecutionTime();
                StatisticReport newReport = new StatisticReport(
                        totalRequests,
                        oldReport.getRequest200Count() + (requestReport.getHttpCode() == 200 ? 1 : 0),
                        oldReport.getRequest4XXCount() + (requestReport.getHttpCode() / 100 == 4 ? 1 : 0),
                        oldReport.getRequest5XXCount() + (requestReport.getHttpCode() / 100 == 5 ? 1 : 0),
                        totalResponseTime,
                        totalResponseTime / totalRequests,
                        Math.min(oldReport.getMinResponseTimeMillis(), requestReport.getExecutionTime()),
                        Math.max(oldReport.getMaxResponseTimeMillis(), requestReport.getExecutionTime()));
                statisticReport.set(newReport);
            }
        } catch (InterruptedException e) {
            log.error("Statistics Handler was interrupted", e.getMessage());
        }
    });

    {
        statisticThread.setName(Constant.STATISTICS_THREAD_NAMW);
    }

    @PostConstruct
    private void init() {
        statisticThread.start();
    }

    public void addRequestReport(RequestReport report) {
        requestReports.add(report);
    }

    public StatisticReport getStatisticReport() {
        return statisticReport.get();
    }
}
