package com.ntnguyen.app.confluence.licensetracking.exception;

public class LicenseStatusNotFoundException extends RuntimeException {

  private final String licenseStatusKey;

  public LicenseStatusNotFoundException(String appKey) {
    this.licenseStatusKey = appKey;
  }

  /**
   * Returns the detail message string of this throwable.
   *
   * @return the detail message string of this {@code Throwable} instance (which may be {@code
   * null}).
   */
  @Override
  public String getMessage() {
    return String.format("The license status with Key [%s] could not be found", licenseStatusKey);
  }
}
