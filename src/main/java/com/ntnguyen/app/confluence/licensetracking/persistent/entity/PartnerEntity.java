package com.ntnguyen.app.confluence.licensetracking.persistent.entity;

import net.java.ao.Accessor;
import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table(value = "MP_PARTNER")
public interface PartnerEntity extends Entity {

  @Accessor("PartnerName")
  String getName();

  @Accessor("PartnerName")
  void setName(String name);

  @Accessor("PartnerType")
  String getType();

  @Accessor("PartnerType")
  void setType(String type);

  UserEntity getBillingContact();

  void setBillingContact(UserEntity user);
}
