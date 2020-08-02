package com.ntnguyen.app.confluence.licensetracking.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {

  private static ObjectMapper objectMapper;

  public static ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
    }
    return objectMapper;
  }

  private JacksonUtil() {
    // Util
  }
}
