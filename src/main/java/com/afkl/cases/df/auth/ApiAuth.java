package com.afkl.cases.df.auth;

import com.mashape.unirest.http.exceptions.UnirestException;

public interface ApiAuth {
    String getToken() throws UnirestException;
}
