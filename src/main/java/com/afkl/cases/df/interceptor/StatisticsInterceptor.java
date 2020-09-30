package com.afkl.cases.df.interceptor;

import com.afkl.cases.df.constant.Constant;
import com.afkl.cases.df.domain.RequestReport;
import com.afkl.cases.df.statistic.StatisticsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class StatisticsInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private StatisticsBean statisticBean;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Optional<Object> startTimeOptional = Optional.ofNullable(request.getAttribute(Constant.START_TIME));
        if (!startTimeOptional.isPresent()) {
            request.setAttribute(Constant.START_TIME, System.currentTimeMillis());
        }
        return true;
    }


    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        long startTime = (Long) request.getAttribute(Constant.START_TIME);
        long executeTime = System.currentTimeMillis() - startTime;

        statisticBean.addRequestReport(new RequestReport(executeTime, response.getStatus()));

    }
}
