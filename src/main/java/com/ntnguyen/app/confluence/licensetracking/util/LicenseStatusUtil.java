package com.ntnguyen.app.confluence.licensetracking.util;

import java.util.HashMap;
import java.util.Map;

public class LicenseStatusUtil {

  private static final Map<String, String> licenseStatusMap = new HashMap<>();

  static {
    licenseStatusMap.put("active", "Active");
    licenseStatusMap.put("inactive", "Expired");
  }

  public static String getLicenseStatusName(String key) {
    return licenseStatusMap.get(key);
  }

  private LicenseStatusUtil() {
    // Util class
  }
}
