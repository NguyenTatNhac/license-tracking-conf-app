package com.ntnguyen.app.confluence.licensetracking.persistent.entity;

import net.java.ao.Accessor;
import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("MP_COMPANY")
public interface CompanyEntity extends Entity {

  @Accessor("CompanyName")
  String getName();

  @Accessor("CompanyName") // Stupid AO, the getter and setter are belong to one column
  void setName(String name);

  // Country and Region should link to the other table, but this  is ok for now
  String getCountry();

  void setCountry(String country);

  String getRegion();

  void setRegion(String region);
}
