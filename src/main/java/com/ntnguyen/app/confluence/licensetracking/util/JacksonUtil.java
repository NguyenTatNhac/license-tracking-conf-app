package com.ntnguyen.app.confluence.licensetracking.util;

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

  private JacksonUtil() {
    // Util
  }
}
