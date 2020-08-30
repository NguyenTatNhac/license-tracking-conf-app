package com.ntnguyen.app.confluence.licensetracking.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class PartnerDto {

  private String partnerName;
  private String partnerType;
  private UserDto billingContact;
}
