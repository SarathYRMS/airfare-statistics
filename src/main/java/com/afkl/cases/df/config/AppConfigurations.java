package com.afkl.cases.df.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "simple-travel-api")
public class AppConfigurations {
    private Auth auth;
    private String url;
    private String airportsUrl;
    private String airportKeyUrl;
    private String airportOriginDestinationUrl;
}
