package com.afkl.cases.df.service;

import com.afkl.cases.df.domain.AirportDetail;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.concurrent.Callable;

public interface AirTravelApiService {

    HttpRequest unirestGetWithToken(String url) throws UnirestException;
    Callable<ResponseEntity<AirportDetail[]>> fetchAllAirports(Map<String, Object> params);
    Callable<ResponseEntity<String>> getAirportDetailsByValue(Map<String, Object> params, String key);
    Callable<ResponseEntity<String>> getAirfare(Map<String, Object> params, String origin, String destination);
}
