package com.ntnguyen.app.confluence.licensetracking.exception;

public class ContactDetailsUndefinedException extends IllegalStateException {

  private final String licenseId;

  public ContactDetailsUndefinedException(String licenseId) {
    this.licenseId = licenseId;
  }

  @Override
  public String getMessage() {
    return String.format("The contact details for the license [%s] is undefined", licenseId);
  }
}
