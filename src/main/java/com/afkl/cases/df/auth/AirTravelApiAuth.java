package com.afkl.cases.df.auth;

import com.afkl.cases.df.config.AppConfigurations;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AirTravelApiAuth implements ApiAuth {

    @Autowired
    private AppConfigurations appConfigurations;
    private volatile String token;
    private volatile long tokenExpiresAtMillis = Long.MIN_VALUE;

    @Override
    public String getToken() throws UnirestException {
        if (System.currentTimeMillis() > tokenExpiresAtMillis) {
            obtainToken();
        }
        return token;
    }

    private void obtainToken() throws UnirestException {
        final String username = appConfigurations.getAuth().getUsername();
        final String password = appConfigurations.getAuth().getPassword();

        JSONObject tokenObject = Unirest.post(appConfigurations.getUrl() + "/oauth/token")
                .header("accept", "application/json")
                .header("content-type", "application/x-www-form-urlencoded")
                .basicAuth("travel-api-client", "psw")
                .queryString("grant_type", "client_credentials")
                .queryString("username", username)
                .queryString("password", password).asJson().getBody().getObject();

        token = tokenObject.get("access_token").toString();
        tokenExpiresAtMillis = System.currentTimeMillis() + tokenObject.getInt("expires_in") * 1000;

        log.debug("Auth token received " + token);
    }
}
