package com.ntnguyen.app.confluence.licensetracking.persistent.entity;

import net.java.ao.Accessor;
import net.java.ao.Entity;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.Table;
import net.java.ao.schema.Unique;

@Table("MP_LICENSE_STATUS")
public interface LicenseStatusEntity extends Entity {

  @NotNull
  @Unique
  @Accessor("StatusKey")
  String getKey();

  @Accessor("StatusKey")
  void setKey(String key);


  @NotNull
  @Accessor("StatusName")
  String getName();

  @Accessor("StatusName")
  void setName();
}
