package com.ntnguyen.app.confluence.licensetracking.persistent.entity;

import net.java.ao.Entity;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.Table;

@Table("MP_CONTACT")
public interface ContactEntity extends Entity {

  @NotNull
  CompanyEntity getCompany();

  void setCompany(CompanyEntity company);

  @NotNull
  UserEntity getTechnicalContact();

  void setTechnicalContact(UserEntity user);

  UserEntity getBillingContact();

  void setBillingContact(UserEntity user);
}
