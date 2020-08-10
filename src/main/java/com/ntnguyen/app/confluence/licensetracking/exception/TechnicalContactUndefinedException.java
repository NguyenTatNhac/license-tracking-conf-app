package com.ntnguyen.app.confluence.licensetracking.exception;

public class TechnicalContactUndefinedException extends IllegalStateException {

  private final String licenseId;

  public TechnicalContactUndefinedException(String licenseId) {
    this.licenseId = licenseId;
  }

  @Override
  public String getMessage() {
    return String.format("The technical contact for the license [%s] is undefined", licenseId);
  }
}
