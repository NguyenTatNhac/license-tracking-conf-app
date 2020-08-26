package com.ntnguyen.app.confluence.licensetracking.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonUtil {

  private static ObjectMapper objectMapper;

  public static ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
    }
    return objectMapper;
  }

  public static ObjectNode getSinglePropJson(String key, Integer value) {
    ObjectNode json = getObjectMapper().createObjectNode();
    json.put(key, value);
    return json;
  }

  public static String toJson(Object o) throws JsonProcessingException {
    return getObjectMapper().writeValueAsString(o);
  }

  private JacksonUtil() {
    // Util
  }

  public static JsonNode readTree(String json) throws JsonProcessingException {
    return getObjectMapper().readTree(json);
  }
}
