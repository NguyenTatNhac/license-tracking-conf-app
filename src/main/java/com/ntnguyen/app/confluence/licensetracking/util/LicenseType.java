package com.ntnguyen.app.confluence.licensetracking.util;

import java.util.HashMap;
import java.util.Map;

public class LicenseType {

  private static final Map<String, String> licenseTypeMap = new HashMap<>();

  static {
    licenseTypeMap.put("EVALUATION", "Evaluation");
    licenseTypeMap.put("COMMERCIAL", "Paid");
    licenseTypeMap.put("OPEN_SOURCE", "Free");
  }

  public static String getLicenseTypeName(String key) {
    return licenseTypeMap.get(key);
  }

  private LicenseType() {
    // Util class
  }
}
