package com.ntnguyen.app.confluence.licensetracking.exception;

public class AppNotFoundException extends RuntimeException {

  private final String appKey;

  public AppNotFoundException(String appKey) {
    this.appKey = appKey;
  }

  /**
   * Returns the detail message string of this throwable.
   *
   * @return the detail message string of this {@code Throwable} instance (which may be {@code
   * null}).
   */
  @Override
  public String getMessage() {
    return String.format("The app with Key [%s] could not be found", appKey);
  }
}
