package com.afkl.cases.df.controller;

import com.afkl.cases.df.constant.Constant;
import com.afkl.cases.df.domain.AirportDetail;
import com.afkl.cases.df.service.AirTravelApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@Slf4j
@AllArgsConstructor
public class AirTravelApiController {

    private AirTravelApiService airTravelApiService;

    @RequestMapping(method = GET, value = "/airports", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Callable<ResponseEntity<AirportDetail[]>> fetchAllAirports(@RequestParam Map<String, Object> params) {

        return airTravelApiService.fetchAllAirports(params);
    }

    @RequestMapping(method = GET, value = "/airports/{key}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Callable<ResponseEntity<String>> getAirportDetailsByValue(@RequestParam Map<String, Object> params,
                                                 @PathVariable(Constant.KEY) String key) {
        return airTravelApiService.getAirportDetailsByValue(params, key);
    }

    @RequestMapping(method = GET, value = "/fares/{origin}/{destination}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Callable<ResponseEntity<String>> getAirfare(@RequestParam Map<String, Object> params,
                                                          @PathVariable(Constant.ORIGIN) String origin,
                                                          @PathVariable(Constant.DESTINATION) String destination) {
        log.info("Request Id : {} started processing for origin={},destination={}", UUID.randomUUID().toString(), origin, destination);
        return airTravelApiService.getAirfare(params, origin, destination);
    }
}
