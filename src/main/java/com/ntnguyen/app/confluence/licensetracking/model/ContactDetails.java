package com.ntnguyen.app.confluence.licensetracking.model;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class ContactDetails {

  private String company;
  private String country;
  private String region;
  private Contact technicalContact;
  private Contact billingContact;
}
