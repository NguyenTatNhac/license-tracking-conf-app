package com.ntnguyen.app.confluence.licensetracking.persistent.entity;

import java.util.Date;
import net.java.ao.Accessor;
import net.java.ao.RawEntity;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.PrimaryKey;
import net.java.ao.schema.Table;

@Table("MP_LICENSE")
public interface LicenseEntity extends RawEntity<String> {

  @NotNull
  @PrimaryKey
  String getLicenseId();

  void setLicenseId(String licenseId);

  String getAppLicenseId();

  void setAppLicenseId(String appLicenseId);

  String getHosting();

  void setHosting(String hosting);

  Date getLastUpdated();

  void setLastUpdated(Date lastUpdated);

  @NotNull
  AppEntity getApp();

  void setApp(AppEntity app);

  @Accessor("LicenseType")
  LicenseTypeEntity getType();

  @Accessor("LicenseType")
  void setType(LicenseTypeEntity type);

  Date getMaintenanceStartDate();

  void setMaintenanceStartDate(Date maintenanceStartDate);

  Date getMaintenanceEndDate();

  void setMaintenanceEndDate(Date maintenanceEndDate);

  @Accessor("LicenseStatus")
  LicenseStatusEntity getStatus();

  @Accessor("LicenseStatus")
  void setStatus(LicenseStatusEntity status);

  String getTier();

  void setTier(String tier);

  ContactEntity getContactDetails();

  void setContactDetails(ContactEntity contactDetails);

  PartnerEntity getPartner();

  void setPartner(PartnerEntity partner);
}
