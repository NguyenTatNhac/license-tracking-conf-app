package com.ntnguyen.app.confluence.licensetracking.model;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class PartnerDetails {

  private String partnerName;
  private String partnerType;
  private Contact billingContact;
}
