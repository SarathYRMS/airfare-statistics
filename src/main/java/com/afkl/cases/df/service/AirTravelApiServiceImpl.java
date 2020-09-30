package com.afkl.cases.df.service;

import com.afkl.cases.df.auth.ApiAuth;
import com.afkl.cases.df.config.AppConfigurations;
import com.afkl.cases.df.constant.Constant;
import com.afkl.cases.df.domain.AirportDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static java.util.Arrays.sort;

@Service
@Slf4j
@AllArgsConstructor
public class AirTravelApiServiceImpl implements AirTravelApiService {

    private ApiAuth apiAuth;
    private AppConfigurations appConfigurations;
    private ObjectMapper objectMapper;

    @Override
    public HttpRequest unirestGetWithToken(String url) throws UnirestException {
        log.info("Endpoint Url: "+url);
        return Unirest.get(url).header(Constant.AUTHORIZATION, "Bearer " + apiAuth.getToken());
    }

    @Override
    public Callable<ResponseEntity<AirportDetail[]>> fetchAllAirports(Map<String, Object> params) {
        return () -> {
            HttpRequest getRequest = unirestGetWithToken(appConfigurations.getAirportsUrl()).queryString(params);
            com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(getRequest.asString().getBody());
            com.fasterxml.jackson.databind.JsonNode locationsNode = jsonNode.get(Constant.EMBEDDED).get(Constant.LOCATIONS);

            AirportDetail[] airportDetails = objectMapper.convertValue(locationsNode, AirportDetail[].class);
            sort(airportDetails, Comparator.comparing(AirportDetail::getCode));

            return new ResponseEntity<>(airportDetails, HttpStatus.OK);
        };
    }

    @Override
    public Callable<ResponseEntity<String>> getAirportDetailsByValue(Map<String, Object> params, String key) {
        return () -> {
            HttpRequest getRequest = unirestGetWithToken(appConfigurations.getAirportKeyUrl()).routeParam(Constant.KEY, key).queryString(params);
            return new ResponseEntity<>(getRequest.asString().getBody(), HttpStatus.OK);
        };
    }

    @Override
    public Callable<ResponseEntity<String>> getAirfare(Map<String, Object> params, String origin, String destination) {
        return () -> {
            log.info("Request Id : {} started processing for origin={},destination={}", UUID.randomUUID().toString(), origin, destination);
            HttpRequest faresRequest = unirestGetWithToken(appConfigurations.getAirportOriginDestinationUrl())
                    .routeParam(Constant.ORIGIN, origin)
                    .routeParam(Constant.DESTINATION, destination)
                    .queryString(params);
            Future<HttpResponse<JsonNode>> faresResponseFuture = faresRequest.asJsonAsync();

            HttpRequest originRequest = unirestGetWithToken(appConfigurations.getAirportKeyUrl())
                    .routeParam(Constant.KEY, origin).queryString(params);
            Future<HttpResponse<JsonNode>> originResponseFuture = originRequest.asJsonAsync();


            HttpRequest destinationRequest = unirestGetWithToken(appConfigurations.getAirportKeyUrl())
                    .routeParam(Constant.KEY, destination)
                    .queryString(params);
            Future<HttpResponse<JsonNode>> destinationResponseFuture = destinationRequest.asJsonAsync();

            HttpResponse<JsonNode> faresResponse = faresResponseFuture.get();
            HttpResponse<JsonNode> originResponse = originResponseFuture.get();
            HttpResponse<JsonNode> destinationResponse = destinationResponseFuture.get();

            JSONObject result = faresResponse.getBody().getObject();
            result.put(Constant.ORIGIN, originResponse.getBody().getObject());
            result.put(Constant.DESTINATION, destinationResponse.getBody().getObject());

            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        };
    }
}
