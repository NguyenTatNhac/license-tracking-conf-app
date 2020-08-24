package com.ntnguyen.app.confluence.licensetracking.util;

import com.opensymphony.user.provider.ejb.util.Base64;

public class Base64Util {

  public static String encodeBasicCredential(String username, String pwd) {
    return new String(Base64.encode((username + ":" + pwd).getBytes()));
  }

  private Base64Util() {
    // Util
  }
}
