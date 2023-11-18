package edu.uoc.tfg.gateway.service;

import org.json.JSONObject;

public interface TokenService {
    JSONObject solveToken(String token);
}