package com.example.client;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface OPhimGateway {
    JsonNode get(String path, Map<String, String> queryParams);
}
