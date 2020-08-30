package com.ntnguyen.app.confluence.licensetracking.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class ContactDto {

  private String company;
  private String country;
  private String region;
  private UserDto technicalContact;
  private UserDto billingContact;
}
