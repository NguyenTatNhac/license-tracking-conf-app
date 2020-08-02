package com.ntnguyen.app.confluence.licensetracking.model;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class MarketplaceLicense {

  private String addonLicenseId;
  private String licenseId;
  private String addonKey;
  private String addonName;
  private String hosting;
  private Date lastUpdated;
  private String licenseType;
  private Date maintenanceStartDate;
  private Date maintenanceEndDate;
  private String status;
  private String tier;
  private ContactDetails contactDetails;
  private PartnerDetails partnerDetails;
}
