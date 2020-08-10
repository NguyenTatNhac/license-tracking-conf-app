package com.ntnguyen.app.confluence.licensetracking.persistent.entity;

import net.java.ao.Accessor;
import net.java.ao.Entity;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.Table;
import net.java.ao.schema.Unique;

@Table(value = "MP_LICENSE_TYPE")
public interface LicenseTypeEntity extends Entity {

  @NotNull
  @Unique
  @Accessor("LicenseTypeKey")
  String getKey();

  @NotNull
  @Accessor("LicenseTypeName")
  String getName();
}
