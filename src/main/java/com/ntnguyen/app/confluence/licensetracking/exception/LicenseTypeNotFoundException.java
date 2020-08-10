package com.ntnguyen.app.confluence.licensetracking.exception;

public class LicenseTypeNotFoundException extends RuntimeException {

  private final String licenseTypeKey;

  public LicenseTypeNotFoundException(String licenseTypeKey) {
    this.licenseTypeKey = licenseTypeKey;
  }

  /**
   * Returns the detail message string of this throwable.
   *
   * @return the detail message string of this {@code Throwable} instance (which may be {@code
   * null}).
   */
  @Override
  public String getMessage() {
    return String.format("The license type with Key [%s] could not be found", licenseTypeKey);
  }
}
